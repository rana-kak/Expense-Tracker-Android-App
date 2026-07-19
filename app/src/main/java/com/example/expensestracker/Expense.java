package com.example.expensestracker;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Expense {

    @PrimaryKey(autoGenerate = true)
    public int id;
    public double amount;
    public String category;
    public long date;
    public String note;

    public Expense(double amount, String category, long date, String note) {
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.note = note;
    }


    // Getters
    public String getNote() {
        return note;
    }

    public long getDate() {
        return date;
    }

    public double getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public int getId() {
        return id;
    }

}

