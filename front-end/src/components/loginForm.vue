<template>
    <Card class="login-card">
        <template #title >
            Login
        </template>
        <template #content>
            <Message v-if="errorMessage" severity="error">{{ errorMessage }}</Message>

            <form @submit.prevent="doLogin">
                <div class="field">
                    <FloatLabel>
                        <InputText id="username" v-model="name" />
                        <label for="username">Username</label>
                    </FloatLabel>
                </div>
                
                <div class="field">
                    <FloatLabel>
                        <Password v-model="password" toggleMask :feedback="false" inputId="password" />
                        <label for="password">Password</label>
                    </FloatLabel>
                </div>
                
                <div class="button-container">
                    <Button type="submit" label="Entrar" icon="pi pi-sign-in" />
                </div>
            </form>
        </template>
    </Card>
</template>

<script>
    import InputText from 'primevue/inputtext';
    import FloatLabel from 'primevue/floatlabel';
    import Password from 'primevue/password';
    import Button from 'primevue/button';
    import Message from 'primevue/message';
    import Card from 'primevue/card';

    export default {
        name:"loginForm",
        components:{
            Message,
            Button,
            Password,
            FloatLabel,
            InputText,
            Card
        },
        data(){
            return{
                name: null,
                password: null,
                errorMessage: null
            }
        },
        methods: {
            async doLogin(){
                this.errorMessage = null;

                if (!this.name || !this.password) {
                    this.errorMessage = "Por favor, preencha o usuário e a senha.";
                    return;
                }

                try {
                    const req = await fetch("http://localhost:3000/login");
                    
                    if (!req.ok) {
                        throw new Error("Falha ao buscar os dados de login.");
                    }

                    const loginData = await req.json();

                    if (loginData[0].username === this.name && loginData[0].password === this.password) {
                        sessionStorage.setItem("isAuthenticated", "true")
                        this.$router.push("/report");
                    } else {
                        this.errorMessage = "Login ou Senha inválido.";
                    }

                } catch (error) {
                    console.error("Erro durante o login:", error);
                    this.errorMessage = "Não foi possível conectar ao servidor. Tente novamente mais tarde.";
                }
            }
        }
    }
</script>

<style scoped>
.login-card {
    width: 100%;
    max-width: 350px; /* Define a largura do painel de login */
}

:deep(.p-card-title) {
    text-align: center;
}

.field {
    margin-top: 1.5rem;
}

.button-container {
    margin-top: 2rem;
    display: flex;
    justify-content: flex-end; /* Botão à direita */
    margin-top: 1.5rem;
}

:deep(.p-inputtext),
:deep(.p-password) {
    width: 100%;
}

/* Garante que a mensagem de erro ocupe toda a largura */
:deep(.p-message) {
    width: 100%;
    box-sizing: border-box;
    margin-bottom: 1.5rem;
}
</style>