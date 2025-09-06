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
        components: { Button },
        computed: {
            // 3. Propriedade 'computada' que lê o título da rota atual
            pageTitle() {
                // Acessa o meta.title da rota atual e define um título padrão caso não encontre
                return this.$route.meta.title || 'Meu Projeto';
            }
        }
    }
</script>

<style scoped>
.main-nav {
    display: flex; /* Ativa o Flexbox */
    justify-content: space-between; /* Mágica: joga um item para cada ponta */
    align-items: center; /* Alinha verticalmente */
    padding: 1rem 2rem; /* Aumenta o padding para um visual mais 'arejado' */
    background-color: #f8f9fa;
    border-bottom: 1px solid #dee2e6;
    margin-bottom: 2rem;
}

.page-title {
    margin: 0; /* Remove a margem padrão do h2 */
    font-size: 1.5rem; /* Tamanho da fonte do título */
    color: var(--p-surface-900); /* Usa a cor de texto do tema */
}

.nav-buttons {
    display: flex;
    gap: 0.5rem; /* Adiciona um pequeno espaço entre os botões */
}
</style>