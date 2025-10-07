<template>
    <div>
        <navComp />

        <Message v-if="successMessage" severity="success">{{ successMessage }}</Message>
        <Message v-if="errorMessage" severity="error">{{ errorMessage }}</Message>
        
        <Card class="card-table">
            <template #title>Gerenciar Reservas</template>
            <template #content>
                <Toolbar class="mb-4">
                    <template #start>
                        <Button label="Adicionar Reserva" icon="pi pi-plus" severity="success" @click="openNew" />
                    </template>
                    <template #end>
                        <span class="p-input-icon-left">
                            <i class="pi pi-search" />
                            <InputText v-model="filters['global'].value" placeholder="Buscar..." />
                        </span>
                    </template>
                </Toolbar>

                <div v-if="isLoading" class="info-message">Carregando...</div>
                
                <DataTable v-else :value="reserveds" paginator :rows="10" stripedRows 
                           v-model:filters="filters"
                           :globalFilterFields="['id', 'userId', 'idResource']">
                    
                    <Column field="id" header="ID Reserva" sortable></Column>
                    <Column field="userId" header="Usuário" sortable></Column>
                     <Column field="idResource" header="Recurso" sortable></Column>
                    <Column field="quantidade" header="Quantidade" sortable></Column>
                    
                    <Column header="Ações" bodyClass="text-center">
                        <template #body="slotProps">
                            <Button icon="pi pi-pencil" severity="warning" rounded class="mr-2" @click="editReserved(slotProps.data)" />
                            <Button icon="pi pi-trash" severity="danger" rounded @click="deleteReserved(slotProps.data)" />
                        </template>
                    </Column>

                    <template #empty> Nenhuma reserva encontrada. </template>
                </DataTable>
            </template>
        </Card>

        <Dialog v-model:visible="reservedDialog" :style="{width: '450px'}" :header="reserved.id ? 'Editar Reserva' : 'Nova Reserva'" :modal="true">
            <div class="form-group">
                <label>Usuário</label>
                <Dropdown v-model="reserved.userId" :options="users" optionLabel="name" optionValue="id" placeholder="Selecione um usuário" class="w-full" />
            </div>
            <div class="form-group">
                <label>Recurso</label>
                <Dropdown v-model="reserved.idResource" :options="resources" optionLabel="name" optionValue="id" placeholder="Selecione um recurso" class="w-full" />
            </div>
            <div class="form-group">
                <label>Quantidade</label>
                <InputNumber v-model="reserved.quantidade" :min="1" class="w-full" />
            </div>
            
            <template #footer>
                <Button label="Cancelar" icon="pi pi-times" text @click="hideDialog"/>
                <Button label="Salvar" icon="pi pi-check" @click="saveReserved" />
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
    import Dropdown from 'primevue/dropdown';
    import Message from 'primevue/message';
    import navComp from "./nav.vue";

    export default {
        name: "dashboard",
        components: {
            DataTable, Column, Button, Card, InputText, InputNumber, Dialog, Toolbar, Dropdown, Message, navComp
        },
        data() {
            return {
                reserveds: [],
                reservedDialog: false,
                reserved: {},
                originalReserved: {},
                users: [],
                resources: [],
                filters: { global: { value: null, matchMode: 'contains' } },
                isLoading: false,
                successMessage: null,
                errorMessage: null
            }
        },
        methods: {
            async getReserveds() {
                this.isLoading = true;
                this.errorMessage = null;
                try {
                    const [reservedsRes, usersRes, resourcesRes] = await Promise.all([
                        fetch("http://localhost:8080/reserved/all"),
                        fetch("http://localhost:8080/users/all"),
                        fetch("http://localhost:8080/resource/all")
                    ]);

                    if (!reservedsRes.ok || !usersRes.ok || !resourcesRes.ok) {
                        throw new Error("Falha ao carregar dados essenciais do relatório.");
                    }

                    const reservedsData = await reservedsRes.json();
                    const usersData = await usersRes.json();
                    const resourcesData = await resourcesRes.json();

                    const userMap = new Map(usersData.map(user => [user.userId, user.nome]));
                    const resourceMap = new Map(resourcesData.map(res => [res.idResource, res.nome]));

                    const processedReserveds = reservedsData.map(reserved => {
                        return {
                            ...reserved,
                            userId: userMap.get(reserved.userId) || `ID ${reserved.userId} Inválido`,
                            idResource: resourceMap.get(reserved.idResource) || `ID ${reserved.idResource} Inválido`
                        };
                    });
                    
                    this.reserveds = processedReserveds;

                } catch (error) {
                    this.errorMessage = error.message;
                } finally {
                    this.isLoading = false;
                }
            },
            
            async loadFormDependencies() {
                try {
                    const usersReq = await fetch("http://localhost:8080/users/all");
                    const resourcesReq = await fetch("http://localhost:8080/resource/all");
                    if (!usersReq.ok || !resourcesReq.ok) {
                        throw new Error("Falha ao carregar as opções do formulário.");
                    }
                    
                    this.users = await usersReq.json();
                    this.resources = await resourcesReq.json();
                } catch (error) {
                    this.errorMessage = error.message;
                }
            },

            openNew() {
                this.reserved = { quantidade: 1 };
                this.originalReserved = {};
                this.reservedDialog = true;
            },
            editReserved(reservedData) {
                this.reserved = { 
                    id: reservedData.id,
                    userId: reservedData.userId,
                    idResource: reservedData.idResource,
                    quantidade: reservedData.quantidade
                };
                this.originalReserved = { ...reservedData };
                this.reservedDialog = true;
            },
            hideDialog() {
                this.reservedDialog = false;
            },

            async saveReserved() {
                if (!this.reserved.userId || !this.reserved.idResource || !this.reserved.quantidade || this.reserved.quantidade < 1) {
                    this.errorMessage = "Todos os campos são obrigatórios.";
                    return;
                }

                try {
                    const resourceReq = await fetch(`http://localhost:8080/resource/all/${this.reserved.idResource}`);
                    if (!resourceReq.ok) throw new Error("Recurso selecionado não encontrado.");
                    const resourceData = await resourceReq.json();

                    const isEditing = !!this.reserved.id;
                    const quantidadeChange = isEditing ? this.reserved.quantidade - this.originalReserved.quantidade : this.reserved.quantidade;

                    if (resourceData.quantidade < quantidadeChange) {
                        throw new Error(`Estoque insuficiente. Disponível: ${resourceData.quantidade}`);
                    }
                    
                    const newStock = resourceData.quantidade - quantidadeChange;

                    await fetch(`http://localhost:8080/resource/all/${this.reserved.idResource}`, {
                        method: 'PATCH',
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify({ quantidade: newStock })
                    });

                    const method = isEditing ? 'PATCH' : 'POST';
                    const url = isEditing ? `http://localhost:8080/reserveds/${this.reserved.id}` : 'http://localhost:8080/reserved/create';
                    
                    const saveReservedReq = await fetch(url, {
                        method,
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify({ userId: this.reserved.userId, idResource: this.reserved.idResource, quantidade: this.reserved.quantidade })
                    });
                    if (!saveReservedReq.ok) throw new Error("Falha ao salvar a reserva.");

                    this.successMessage = "Reserva salva com sucesso!";
                    this.getReserveds();

                } catch (error) {
                    this.errorMessage = error.message;
                } finally {
                    this.hideDialog();
                }
            },

            async deleteReserved(reservedData) {
                if (!window.confirm(`Tem certeza que deseja deletar a reserva de "${reservedData.idResource}" para "${reservedData.userId}"?`)) return;

                try {
                    const resourceReq = await fetch(`http://localhost:8080/resource/all/${reservedData.idResource}`);
                    if (!resourceReq.ok) throw new Error("Recurso não encontrado para devolver ao estoque.");
                    const resourceData = await resourceReq.json();
                    const newStock = resourceData.quantidade + reservedData.quantidade;

                    await fetch(`http://localhost:8080/resource/all/${reservedData.idResource}`, {
                        method: 'PATCH',
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify({ quantidade: newStock })
                    });

                    const deleteReq = await fetch(`http://localhost:8080/reserveds/${reservedData.id}`, { method: 'DELETE' });
                    if (!deleteReq.ok) throw new Error("Falha ao deletar a reserva.");

                    this.successMessage = "Reserva deletada e estoque atualizado!";
                    this.getReserveds();
                } catch (error) {
                    this.errorMessage = error.message;
                }
            }
        },
        mounted() {
            this.getReserveds();
            this.loadFormDependencies();
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

    .form-group {
        margin-bottom: 1.5rem;
    }
    .form-group label {
        display: block;
        margin-bottom: 0.5rem;
        font-weight: bold;
    }
    :deep(.p-dialog-content) {
        padding-top: 1.5rem;
    }
</style>