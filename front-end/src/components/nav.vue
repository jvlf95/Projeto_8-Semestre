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
    background-color: #ac6b0bea;
    margin-bottom: 2rem;
    box-shadow: 0 10px 20px rgba(0, 0, 0, 0.3)!important;
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