package com.example.agregadorDeInvestimentos.entity;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "account_id")
    private UUID id;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")

    private User user;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "account")

    @PrimaryKeyJoinColumn
    private BillingAdress billingAdress;

    @OneToMany(mappedBy = "account")
    private List<AccountStock> accountStocks;



    public Account(UUID id, String description, User user, BillingAdress billingAdres, List<AccountStock> accountStocks){
        this.id = id;
        this.description = description;
        this.user = user;
        this.billingAdress = billingAdress;
        this.accountStocks = accountStocks;
    }

    public Account() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BillingAdress getBillingAdress() {
        return billingAdress;
    }

    public void setBillingAdress(BillingAdress billingAdress) {
        this.billingAdress = billingAdress;
    }

    public List<AccountStock> getAccountStocks() {
        return accountStocks;
    }

    public void setAccountStocks(List<AccountStock> accountStocks) {
        this.accountStocks = accountStocks;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", description='" + description + '\'' +
                '}';
    }
}
