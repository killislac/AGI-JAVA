package com.example.agregadorDeInvestimentos.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "stocks")
public class Stock {

    @Id
    @Column(name = "stock_id")
    private String id;
    //o id sera inserido como o ticker
    @Column(name = "description")
    private String description;

    public Stock(String id, String description) {
        this.id = id;
        this.description = description;
    }

    public Stock() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "id='" + id + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
