package com.uninove.projeto.projetojoao;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;


@Entity
@Table(name = "reserved")
public class Reserved {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "id_resource")
    private int resourceId;

    @Column(name = "quantidade")
    private int quantidade;

    public int getId(){
        return id;
    }

    public int getUserId(){
        return userId;
    }

    public int getResourceId(){
        return resourceId;
    }

    public int getQuantidade(){
        return quantidade;
    }

    public void setId(int i){
        this.id = i;
    }

    public void setUserId(int i){
        this.userId = i;
    }

    public void setResourceId(int i){
        this.resourceId = i;
    }

    public void setQuantidade(int q){
        this.quantidade = q;
    }




}
