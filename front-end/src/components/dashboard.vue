<template>
    <div>
        <navComp />
        <h1>Relatório de Reservas</h1>

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
                           :globalFilterFields="['id', 'userName', 'resourceName']">
                    
                    <Column field="id" header="ID Reserva" sortable></Column>
                    <Column field="userName" header="Usuário" sortable></Column>
                     <Column field="resourceName" header="Recurso" sortable></Column>
                    <Column field="quantity" header="Quantidade" sortable></Column>
                    
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
                <Dropdown v-model="reserved.id_user" :options="users" optionLabel="name" optionValue="id" placeholder="Selecione um usuário" class="w-full" />
            </div>
            <div class="form-group">
                <label>Recurso</label>
                <Dropdown v-model="reserved.id_resource" :options="resources" optionLabel="name" optionValue="id" placeholder="Selecione um recurso" class="w-full" />
            </div>
            <div class="form-group">
                <label>Quantidade</label>
                <InputNumber v-model="reserved.quantity" :min="1" class="w-full" />
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
                        fetch("http://localhost:8090/reserveds"),
                        fetch("http://localhost:8090/users"),
                        fetch("http://localhost:8090/resources")
                    ]);

                    if (!reservedsRes.ok || !usersRes.ok || !resourcesRes.ok) {
                        throw new Error("Falha ao carregar dados essenciais do relatório.");
                    }

                    const reservedsData = await reservedsRes.json();
                    const usersData = await usersRes.json();
                    const resourcesData = await resourcesRes.json();

                    const userMap = new Map(usersData.map(user => [user.id, user.name]));
                    const resourceMap = new Map(resourcesData.map(res => [res.id, res.name]));

                    const processedReserveds = reservedsData.map(reserved => {
                        return {
                            ...reserved,
                            userName: userMap.get(reserved.id_user) || `ID ${reserved.id_user} Inválido`,
                            resourceName: resourceMap.get(reserved.id_resource) || `ID ${reserved.id_resource} Inválido`
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
                    const usersReq = await fetch("http://localhost:8090/users");
                    const resourcesReq = await fetch("http://localhost:8090/resources");
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
                this.reserved = { quantity: 1 };
                this.originalReserved = {};
                this.reservedDialog = true;
            },
            editReserved(reservedData) {
                this.reserved = { 
                    id: reservedData.id,
                    id_user: reservedData.id_user,
                    id_resource: reservedData.id_resource,
                    quantity: reservedData.quantity
                };
                this.originalReserved = { ...reservedData };
                this.reservedDialog = true;
            },
            hideDialog() {
                this.reservedDialog = false;
            },

            async saveReserved() {
                if (!this.reserved.id_user || !this.reserved.id_resource || !this.reserved.quantity || this.reserved.quantity < 1) {
                    this.errorMessage = "Todos os campos são obrigatórios.";
                    return;
                }

                try {
                    const resourceReq = await fetch(`http://localhost:3000/resources/${this.reserved.id_resource}`);
                    if (!resourceReq.ok) throw new Error("Recurso selecionado não encontrado.");
                    const resourceData = await resourceReq.json();

                    const isEditing = !!this.reserved.id;
                    const quantityChange = isEditing ? this.reserved.quantity - this.originalReserved.quantity : this.reserved.quantity;

                    if (resourceData.quantity < quantityChange) {
                        throw new Error(`Estoque insuficiente. Disponível: ${resourceData.quantity}`);
                    }
                    
                    const newStock = resourceData.quantity - quantityChange;

                    await fetch(`http://localhost:3000/resources/${this.reserved.id_resource}`, {
                        method: 'PATCH',
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify({ quantity: newStock })
                    });

                    const method = isEditing ? 'PATCH' : 'POST';
                    const url = isEditing ? `http://localhost:3000/reserveds/${this.reserved.id}` : 'http://localhost:3000/reserveds';
                    
                    const saveReservedReq = await fetch(url, {
                        method,
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify({ id_user: this.reserved.id_user, id_resource: this.reserved.id_resource, quantity: this.reserved.quantity })
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
                if (!window.confirm(`Tem certeza que deseja deletar a reserva de "${reservedData.resourceName}" para "${reservedData.userName}"?`)) return;

                try {
                    const resourceReq = await fetch(`http://localhost:3000/resources/${reservedData.id_resource}`);
                    if (!resourceReq.ok) throw new Error("Recurso não encontrado para devolver ao estoque.");
                    const resourceData = await resourceReq.json();
                    const newStock = resourceData.quantity + reservedData.quantity;

                    await fetch(`http://localhost:3000/resources/${reservedData.id_resource}`, {
                        method: 'PATCH',
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify({ quantity: newStock })
                    });

                    const deleteReq = await fetch(`http://localhost:3000/reserveds/${reservedData.id}`, { method: 'DELETE' });
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