package com.uninove.projeto.projetojoao;


public class LoginDto {

    private String username;
    private String password;

    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }

    public void setUsername(String u){
        this.username = u;
    }

    public void setPassword(String p){
        this.password = p;
    }
}
