<template>
    <div>
        <navComp />
        <h1>Gerenciamento de Recursos</h1>

        <Message v-if="successMessage" severity="success">{{ successMessage }}</Message>
        <Message v-if="errorMessage" severity="error">{{ errorMessage }}</Message>
        
        <Card>
            <template #title>Lista de Recursos</template>
            <template #content>
                <Toolbar class="mb-4">
                    <template #start>
                        <Button label="Adicionar Recurso" icon="pi pi-plus" severity="success" @click="openNew" />
                    </template>
                    <template #end>
                        <span class="p-input-icon-left">
                            <i class="pi pi-search"  id="lupa"/>
                            <InputText v-model="filters['global'].value" placeholder="Buscar por ID, nome ou categoria..." />
                        </span>
                    </template>
                </Toolbar>

                <DataTable :value="resources" paginator :rows="10" v-model:filters="filters" stripedRows
                           :globalFilterFields="['id', 'name', 'category']" tableStyle="min-width: 50rem">

                    
                    <Column field="id" header="ID" sortable></Column>
                    <Column field="name" header="Nome" sortable></Column>
                    <Column field="category" header="Categoria" sortable></Column>
                    <Column field="quantity" header="Quantidade" sortable></Column>
                    <Column field="price" header="Preço" sortable>
                        <template #body="slotProps">
                            {{ formatCurrency(slotProps.data.price) }}
                        </template>
                    </Column>

                    <Column header="Ações" bodyClass="text-center" style="width: 15rem">
                        <template #body="slotProps">
                            <Button icon="pi pi-pencil" severity="warning" rounded class="mr-2" @click="editResource(slotProps.data)" />
                            <Button icon="pi pi-trash" severity="danger" rounded @click="deleteResource(slotProps.data)" />
                        </template>
                    </Column>
                </DataTable>
            </template>
        </Card>

        <Dialog v-model:visible="resourceDialog" :style="{width: '450px'}" :header="resource.id ? 'Editar Recurso' : 'Novo Recurso'" :modal="true">
            <div class="field">
                <FloatLabel>
                    <InputText id="name" v-model.trim="resource.name" required="true" />
                    <label for="name">Nome</label>
                </FloatLabel>
            </div>
             <div class="field">
                <FloatLabel>
                    <InputText id="category" v-model.trim="resource.category" />
                    <label for="category">Categoria</label>
                </FloatLabel>
            </div>
            <div class="field">
                <FloatLabel>
                    <InputNumber id="quantity" v-model="resource.quantity" integeronly />
                    <label for="quantity">Quantidade</label>
                </FloatLabel>
            </div>
            <div class="field">
                <FloatLabel>
                    <InputNumber id="price" v-model="resource.price" mode="currency" currency="BRL" locale="pt-BR" />
                    <label for="price">Preço</label>
                </FloatLabel>
            </div>
            <template #footer>
                <Button label="Cancelar" icon="pi pi-times" text @click="hideDialog"/>
                <Button label="Salvar" icon="pi pi-check" @click="saveResource" />
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
                resources: [],
                resource: {},
                resourceDialog: false,
                filters: { global: { value: null, matchMode: 'contains' }},
                successMessage: null,
                errorMessage: null,
            }
        },
        methods: {

            async getResources() {
                this.errorMessage = null;
                try {
                    const req = await fetch(`http://localhost:3000/resources`);
                    if (!req.ok) throw new Error("Não foi possível carregar os recursos.");
                    this.resources = await req.json();
                } catch (error) {
                    this.errorMessage = error.message;
                }
            },

            async deleteResource(resource) {
                this.successMessage = null;
                if (!window.confirm(`Tem certeza que deseja deletar ${resource.name}?`)) return;
                try {
                    const req = await fetch(`http://localhost:3000/resources/${resource.id}`, { method: "DELETE" });
                    if (!req.ok) throw new Error("Falha ao deletar o recurso.");
                    this.successMessage = `Recurso "${resource.name}" deletado com sucesso!`;
                    this.getResources();
                } catch (error) {
                    this.errorMessage = error.message || "Não foi possível deletar o recurso.";
                }
            },
            openNew() {
                this.resource = {};
                this.resourceDialog = true;
            },
            editResource(resource) {
                this.resource = { ...resource };
                this.resourceDialog = true;
            },
            hideDialog() {
                this.resourceDialog = false;
            },

            async saveResource() {
                if (!this.resource.name) {
                    this.errorMessage = "O nome é obrigatório.";
                    return;
                }
                
                const isEditing = !!this.resource.id;
                const method = isEditing ? 'PATCH' : 'POST';
                const url = isEditing ? `http://localhost:3000/resources/${this.resource.id}` : 'http://localhost:3000/resources';
                
                try {
                    const req = await fetch(url, {
                        method,
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify(this.resource)
                    });
                    if (!req.ok) throw new Error("Falha ao salvar o recurso.");
                    this.successMessage = `Recurso salvo com sucesso!`;
                    this.getResources();
                } catch (error) {
                    this.errorMessage = error.message;
                } finally {
                    this.hideDialog();
                }
            },

            formatCurrency(value) {
                if (value === undefined || value === null) return '';
                return new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(value);
            }
        },
        mounted() {
            this.getResources();
        }
    }
</script>

<style scoped>
    .field { 
        margin-bottom: 2.5rem; 
    }

    .table-header { 
        display: flex; 
        justify-content: flex-end; 
    }

    .message { 
        margin-bottom: 1rem; 
    }

    :deep(.p-dialog-content) { 
        padding-top: 1.5rem; 
    }

</style>