package com.example.finalexpense;

import java.io.Serializable;

public class ExpenseDetails implements Serializable {
    String ExpenseName, Category;
    String Amount;

    public String getDate() {
        return Date;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public void setDate(String date) {
        Date = date;
    }

    String Date;

    public String getExpenseName() {
        return ExpenseName;
    }

    public void setExpenseName(String expenseName) {
        ExpenseName = expenseName;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }
}
