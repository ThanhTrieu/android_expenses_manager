package com.example.asm2_applicationdevelopment.Model;

import java.io.Serializable;

public class Budget implements Serializable {

    private int id;
    private String category;
    private double amount;

    public Budget(int id, String category, double amount) {
        this.id = id;
        this.category = category;
        this.amount = amount;
    }

    public Budget(String category, double amount) {
        this.category = category;
        this.amount = amount;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Budget{" +
                "id=" + id +
                ", category='" + category + '\'' +
                ", amount=" + amount +
                '}';
    }
}
