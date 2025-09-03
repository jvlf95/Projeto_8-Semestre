package com.uninove.projeto.projetojoao.entity;

import jakarta.persistence.Entity; // Define classes como tabelas
import jakarta.persistence.Id; // indica que determinado atributo é a PK da tabela
import jakarta.persistence.Table; // referencia o nome da tabela à classe
import jakarta.persistence.Column; // referencia o nome da coluna ao atributo

@Entity // O hibernate irá fazer uma tabela com essa classe
@Table(name = "login")
public class Login {

    @Id
    @Column(name = "username")
    private String username;

    @Column(name="password")
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
