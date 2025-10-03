<template>
    <div>
        <navComp />

        <Message v-if="successMessage" severity="success">{{ successMessage }}</Message>
        <Message v-if="errorMessage" severity="error">{{ errorMessage }}</Message>
        
        <Card>
            <template #title>Lista de Usuários</template>
            <template #content>
                <Toolbar class="mb-4">
                    <template #start>
                        <Button label="Adicionar Usuário" icon="pi pi-plus" severity="success" @click="openNew" />
                    </template>
                    <template #end>
                        <span class="p-input-icon-left">
                            <i class="pi pi-search" />
                            <InputText v-model="filters['global'].value" placeholder="Buscar por ID, nome ou condição..." />
                        </span>
                    </template>
                </Toolbar>

                <DataTable :value="users" paginator :rows="10" v-model:filters="filters" stripedRows
                           :globalFilterFields="['userId', 'nome', 'condicao']" tableStyle="min-width: 50rem">
                    
                    <Column field="userId" header="ID" sortable></Column>
                    <Column field="nome" header="Nome" sortable></Column>
                    <Column field="idade" header="Idade" sortable></Column>
                    <Column field="condicao" header="Condição" sortable></Column>

                    <Column header="Ações" bodyClass="text-center" style="width: 15rem">
                        <template #body="slotProps">
                            <Button icon="pi pi-pencil" severity="warning" rounded class="mr-2" @click="editUser(slotProps.data)" />
                            <Button icon="pi pi-trash" severity="danger" rounded @click="deleteUser(slotProps.data)" />
                        </template>
                    </Column>
                </DataTable>
            </template>
        </Card>

        <Dialog v-model:visible="userDialog" :style="{width: '450px'}" :header="user.userId ? 'Editar Usuário' : 'Novo Usuário'" :modal="true">
            
            <div class="field">
                <FloatLabel>
                    <InputText id="nome" v-model.trim="user.nome" required="true" />
                    <label for="nome">Nome</label>
                </FloatLabel>
            </div>
             <div class="field">
                <FloatLabel>
                    <InputText id="condicao" v-model.trim="user.condicao" />
                    <label for="condicao">Condição</label>
                </FloatLabel>
            </div>
            <div class="field">
                <FloatLabel>
                    <InputNumber id="idade" v-model="user.idade" integeronly />
                    <label for="idade">Idade</label>
                </FloatLabel>
            </div>
            <div class="field">
                <FloatLabel>
                    <InputNumber id="peso" v-model="user.peso" :minFractionDigits="1" />
                    <label for="peso">Peso</label>
                </FloatLabel>
            </div>

            <template #footer>
                <Button label="Cancelar" icon="pi pi-times" text @click="hideDialog"/>
                <Button label="Salvar" icon="pi pi-check" @click="saveUser" />
            </template>
        </Dialog>
    </div>
</template>
<script>
    import DataTable from 'primevue/datatable';
    import Column from 'primevue/column';
    import Button from 'primevue/button';
    import Card from 'primevue/card';
    import InputText from 'primevue/inputtext';
    import InputNumber from 'primevue/inputnumber';
    import Dialog from 'primevue/dialog';
    import Toolbar from 'primevue/toolbar';
    import FloatLabel from 'primevue/floatlabel';
    import Message from 'primevue/message';
    import navComp from "./nav.vue";

    export default {
        components: { DataTable, Column, Button, Card, InputText, InputNumber, Dialog, Toolbar, FloatLabel, Message, navComp },
        data() {
            return {
                users: [],
                user: {},
                userDialog: false,
                filters: { global: { value: null, matchMode: 'contains' }},
                isLoading: false,
                successMessage: null,
                errorMessage: null,
            }
        },
        methods: {
            async getUsers() {
                this.isLoading = true;
                this.errorMessage = null;
                try {
                    const req = await fetch(`http://localhost:8080/users/all`);
                    if (!req.ok) throw new Error("Não foi possível carregar os usuários.");
                    this.users = await req.json();
                } catch (error) {
                    this.errorMessage = error.message;
                } finally {
                    this.isLoading = false;
                }
            },
            async deleteUser(user) {
                this.successMessage = null;
                if (!window.confirm(`Tem certeza que deseja deletar o usuário ${user.nome}?`)) {
                    return;
                }
                try {
                    const req = await fetch(`http://localhost:8080/users/${user.userId}`, { method: "DELETE" });
                    if (!req.ok) throw new Error("Falha ao deletar o usuário.");
                    this.successMessage = `Usuário "${user.nome}" deletado com sucesso!`;
                    this.getUsers();
                } catch (error) {
                    this.errorMessage = error.message || "Não foi possível deletar o usuário.";
                }
            },

            openNew() {
                this.user = {};
                this.userDialog = true;
            },
            editUser(userFromRow) {
                this.user = { ...userFromRow }; 
                this.userDialog = true;
            },
            hideDialog() {
                this.userDialog = false;
            },

            async saveUser() {
                this.successMessage = null;
                this.errorMessage = null;

                if (!this.user.nome || !this.user.idade || !this.user.peso) {
                    this.errorMessage = "Nome, idade e peso são campos obrigatórios.";
                    return;
                }
                
                const isEditing = !!this.user.userId;
                const method = isEditing ? 'PATCH' : 'POST';
                const url = isEditing ? `http://localhost:8080/users/${this.user.userId}` : 'http://localhost:8080/users';
                
                try {
                    const req = await fetch(url, {
                        method,
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify(this.user)
                    });
                    if (!req.ok) throw new Error("Falha ao salvar o usuário.");

                    this.successMessage = `Usuário salvo com sucesso!`;
                    this.getUsers();
                } catch (error) {
                    this.errorMessage = error.message;
                } finally {
                    this.hideDialog();
                }
            },
        },
        mounted() {
            this.getUsers();
        }
    }
</script>

<style scoped>
    .message { 
        margin-bottom: 1rem; 
    }
    .table-header { 
        display: flex; 
        justify-content: flex-end; 
    }

    .field { 
        margin-bottom: 2.5rem; 
    }
    :deep(.p-dialog-content) {
        padding-top: 1.5rem;
    }
</style>