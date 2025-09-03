package com.uninove.projeto.projetojoao;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId;

    @Column(name = "nome")
    private String nome;

    @Column(name = "condicao")
    private String condicao;

    @Column(name = "idade")
    private int idade;

    @Column(name =  "peso")
    private float peso;

    public int getUserId(){
        return userId;
    }

    public String getNome(){
        return nome;
    }

    public String getCondicao(){
        return condicao;
    }

    public int getIdade(){
        return idade;
    }

    public float getPeso(){
        return peso;
    }

    public void setUserId(int u){
        this.userId = u;
    }

    public void setNome(String n){
        this.nome = n;
    }

    public void setCondicao(String c){
        this.condicao = c;
    }

    public void setIdade(int i){
        this.idade = i;
    }

    public void setPeso(float p){
        this.peso = p;
    }

}
