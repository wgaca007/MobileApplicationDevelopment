package com.example.expenseapp;

import android.util.Log;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class Expense implements Serializable {
    String expenseName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String id;
    Double expenseCost;
    String expenseDate;
    String img;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getExpenseName() {
        return expenseName;
    }

    public void setExpenseName(String expenseName) {
        this.expenseName = expenseName;
    }

    public Double getExpenseCost() {
        return expenseCost;
    }

    public void setExpenseCost(Double expenseCost) {
        this.expenseCost = expenseCost;
    }

    public String getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(String expenseDate) {
        this.expenseDate = expenseDate;
    }

    public static Comparator<Expense> CostComparator = new Comparator<Expense>() {
        @Override
        public int compare(Expense o1, Expense o2) {
            Log.d("demo", "" + o1.getExpenseCost() + "-" + o2.getExpenseCost());

            double price1 = o1.getExpenseCost();
            double price2 = o2.getExpenseCost();

            return (int) (price1 - price2);
        }
    };

    public static Comparator<Expense> DateComparator = new Comparator<Expense>() {
        @Override
        public int compare(Expense o1, Expense o2) {
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            Date Pdate = null;
            Date Qdate = null;
            try {
                Pdate = df.parse(o1.getExpenseDate());
                Qdate = df.parse(o2.getExpenseDate());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d("demo", "Date 1: " + Pdate);
            Log.d("demo", "Date 2: " + Qdate);
            Log.d("demo", "Comparison: " + Pdate.compareTo(Qdate));
            return Pdate.compareTo(Qdate);
        }
    };

    public static Comparator<Expense> NameComparator = new Comparator<Expense>() {
        @Override
        public int compare(Expense o1, Expense o2) {
            String Pdate = o1.getExpenseName();
            String Qdate = o2.getExpenseName();

            return Pdate.compareTo(Qdate);
        }
    };
}
