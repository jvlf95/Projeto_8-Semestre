package com.uninove.projeto.projetojoao;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Entity
@Table(name = "resource")
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_resource")
    private int idResource;

    @Column(name = "nome")
    private String nome;

    @Column(name = "categoria")
    private String categoria;

    @Column(name = "quantidade")
    private int quantidade;

    @Column(name = "preco")
    private float preco;

    public int getIdResource(){
        return idResource;
    }

    public String getNome(){
        return nome;
    }

    public String getCategoria(){
        return categoria;
    }

    public int getQuantidade(){
        return quantidade;
    }

    public float getPreco(){
        return preco;
    }

    public void setIdResource(int i){
        this.idResource = i;
    }

    public void setNome(String n){
        this.nome = n;
    }

    public void setCategoria(String c){
        this.categoria = c;
    }

    public void setQuantidade(int q){
        this.quantidade = q;
    }

    public void setPeco(float p){
        this.preco = p;
    }

}
