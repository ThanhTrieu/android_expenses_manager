package com.example.asm2_applicationdevelopment.Model;

public class Budget {
    private int id;
    private String description;
    private String category;
    private double amount;
    private String startDate;  // New field for start date
    private String endDate;    // New field for end date

    public Budget(int id, String description, String category, double amount, String startDate, String endDate) {
        this.id = id;
        this.description = description;
        this.category = category;
        this.amount = amount;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }
}
