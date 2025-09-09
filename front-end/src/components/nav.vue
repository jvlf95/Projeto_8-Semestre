<template>
    <nav class="main-nav">
        <h2 class="page-title">{{ pageTitle }}</h2>

        <div class="nav-buttons">
            <router-link to="/report" custom v-slot="{ navigate, isActive }">
                <Button label="Relatório" text :severity="isActive ? 'primary' : 'secondary'" @click="navigate" />
            </router-link>
            <router-link to="/user/userManagement" custom v-slot="{ navigate, isActive }">
                <Button label="Usuários" text :severity="isActive ? 'primary' : 'secondary'" @click="navigate" />
            </router-link>
            <router-link to="/resource/resourceManagement" custom v-slot="{ navigate, isActive }">
                <Button label="Recursos" text :severity="isActive ? 'primary' : 'secondary'" @click="navigate" />
            </router-link>
        </div>
    </nav>
</template>

<script>
    import Button from 'primevue/button'

    export default {
        name: "navComp",
        components: { Button }
    }
</script>

<style scoped>
.main-nav {
    display: flex;
    justify-content: space-between; 
    align-items: center;
    padding: 1rem 2rem;
    background-color: #f8f9fa;
    border-bottom: 1px solid #dee2e6;
    margin-bottom: 2rem;
}

.page-title {
    margin: 0;
    font-size: 1.5rem; 
    color: var(--p-surface-900); 
}

.nav-buttons {
    display: flex;
    gap: 0.5rem; 
}
</style>