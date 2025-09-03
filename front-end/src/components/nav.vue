<template>
    <nav class="main-nav">
        <router-link to="/report" custom v-slot="{ navigate, isActive }">
            <Button 
                label="Relatório Geral" 
                text 
                :severity="isActive ? 'primary' : 'secondary'"
                @click="navigate" 
            />
        </router-link>

        <router-link to="/user/userManagement" custom v-slot="{ navigate, isActive }">
            <Button 
                label="Gerenciar Usuários" 
                text 
                :severity="isActive ? 'primary' : 'secondary'"
                @click="navigate" 
            />
        </router-link>

        <router-link to="/resource/resoucerManagement" custom v-slot="{ navigate, isActive }">
            <Button 
                label="Gerenciar Recursos" 
                text 
                :severity="isActive ? 'primary' : 'secondary'"
                @click="navigate" 
            />
        </router-link>
    </nav>
</template>

<script>
    import Button from 'primevue/button'

    export default {
        name: "navComp",
        components: {
            Button
        }
    }
</script>

<style scoped>
    .main-nav {
        padding: 1rem;
        background-color: #f8f9fa;
        border-bottom: 1px solid #dee2e6;
        margin-bottom: 2rem;
        display: flex;
        gap: 1rem;
        justify-content: center; 
    }
</style>