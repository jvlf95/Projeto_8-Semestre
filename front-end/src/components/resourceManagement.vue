<template>
    <div>
        <navComp />

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
                           :globalFilterFields="['idResource', 'nome', 'categoria']" tableStyle="min-width: 50rem">

                    
                    <Column field="idResource" header="ID" sortable></Column>
                    <Column field="nome" header="Nome" sortable></Column>
                    <Column field="categoria" header="Categoria" sortable></Column>
                    <Column field="quantidade" header="Quantidade" sortable></Column>
                    <Column field="preco" header="Preço" sortable>
                        <template #body="slotProps">
                            {{ formatCurrency(slotProps.data.preco) }}
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

        <Dialog v-model:visible="resourceDialog" :style="{width: '450px'}" :header="resource.idResource ? 'Editar Recurso' : 'Novo Recurso'" :modal="true">
            <div class="field">
                <FloatLabel>
                    <InputText id="nome" v-model.trim="resource.nome" required="true" />
                    <label for="nome">Nome</label>
                </FloatLabel>
            </div>
             <div class="field">
                <FloatLabel>
                    <InputText id="categoria" v-model.trim="resource.categoria" />
                    <label for="categoria">Categoria</label>
                </FloatLabel>
            </div>
            <div class="field">
                <FloatLabel>
                    <InputNumber id="quantidade" v-model="resource.quantidade" integeronly />
                    <label for="quantidade">Quantidade</label>
                </FloatLabel>
            </div>
            <div class="field">
                <FloatLabel>
                    <InputNumber id="preco" v-model="resource.preco" mode="currency" currency="BRL" locale="pt-BR" />
                    <label for="preco">Preço</label>
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
                    const req = await fetch(`http://localhost:8080/resource/all`);
                    if (!req.ok) throw new Error("Não foi possível carregar os recursos.");
                    this.resources = await req.json();
                } catch (error) {
                    this.errorMessage = error.message;
                }
            },

            async deleteResource(resource) {
                this.successMessage = null;
                if (!window.confirm(`Tem certeza que deseja deletar ${resource.nome}?`)) return;
                try {
                    const req = await fetch(`http://localhost:8080/resource/delete/${resource.idResource}`, { method: "DELETE" });
                    if (!req.ok) throw new Error("Falha ao deletar o recurso.");
                    this.successMessage = `Recurso "${resource.nome}" deletado com sucesso!`;
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
                if (!this.resource.nome) {
                    this.errorMessage = "O nome é obrigatório.";
                    return;
                }
                
                const isEditing = !!this.resource.idResource;
                const method = isEditing ? 'PATCH' : 'POST';
                const url = isEditing ? `http://localhost:8080/resource/patch/${this.resource.idResource}` : 'http://localhost:8080/resource/create';
                
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