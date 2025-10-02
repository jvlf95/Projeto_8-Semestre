<template>
    <div>
        <Message v-if="errorMessage" severity="error" icon="pi pi-times-circle">{{ errorMessage }}</Message>

        <form id="login-form" @submit.prevent="doLogin">
            <div class="form-field">
                <FloatLabel variant="in">
                    <InputText id="username" v-model="name" />
                    <label for="username">Username</label>
                </FloatLabel>
            </div>
            
            <div class="form-field">
                <FloatLabel variant="in">
                    <Password v-model="password" toggleMask :feedback="false" inputId="password" />
                    <label for="password">Password</label>
                </FloatLabel>
            </div>
            
            <Button type="submit" label="SUBMIT" class="submit-button" />
        </form>
    </div>
</template>

<script>
    import InputText from 'primevue/inputtext';
    import FloatLabel from 'primevue/floatlabel';
    import Password from 'primevue/password';
    import Button from 'primevue/button';
    import Message from 'primevue/message';

    export default {
        name:"loginForm",
        components:{
            Message,
            Button,
            Password,
            FloatLabel,
            InputText
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
                    const req = await fetch("http://localhost:8080/login/home",{
                      method: "POST",
                      headers: {
                        "Content-Type": "application/json"
                      },
                      body: JSON.stringify({
                        username: this.name,
                        password: this.password
                      })
                    });


                    
                    if (!req.ok) {
                        throw new Error("Falha ao buscar os dados de login.");
                    }

                    const loginData = await req.json();

                    if (loginData.username === this.name && loginData.password === this.password) {
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
    #login-form {
        width: 350px;
    }

    .form-field {
        margin-bottom: 1rem;
    }

    :deep(.p-inputtext),
    :deep(.p-password) {
        width: 100%;
    }

    .submit-button {
        width: 100%; 
    }
</style>