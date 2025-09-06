<template>
    <div class="login-page-container">
        <loginForm/>
    </div>
</template>

<script>
    import loginForm from "../components/loginForm.vue";

    export default{
        name: "loginView",
        components:{
            loginForm
        }
        
    }
</script>

<style scoped>
    .login-page-container{
        display:flex;
        flex-direction:column;
        justify-content: center;
        align-items: center;
        width:100%;
        min-height: 40vh;
    }

    #page-login {
        color: white;
        margin-bottom: 2rem;
        font-weight: bold;
    }
</style>