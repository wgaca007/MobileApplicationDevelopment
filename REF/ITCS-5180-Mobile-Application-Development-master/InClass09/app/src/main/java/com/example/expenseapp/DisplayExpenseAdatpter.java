package com.example.expenseapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class DisplayExpenseAdatpter extends RecyclerView.Adapter<DisplayExpenseAdatpter.ViewHolder> {

    ArrayList<Expense> myData = new ArrayList<Expense>();

    public DisplayExpenseAdatpter(ArrayList<Expense> myData) {
        this.myData = myData;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_show_expense, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        Expense expense = myData.get(position);

        viewHolder.name.setText(viewHolder.name.getText() + " " + expense.getExpenseName());
        viewHolder.cost.setText(viewHolder.cost.getText() + " " + expense.getExpenseCost().toString());
        viewHolder.date.setText(viewHolder.date.getText() + " " + expense.getExpenseDate());
        //viewHolder.imageButton.setImageBitmap(decodeFromFirebaseBase64(expense.getImg()));

        viewHolder.expense = expense;
    }

    public Bitmap decodeFromFirebaseBase64(String image) {
        byte[] decodedByteArray = android.util.Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
    }

    @Override
    public int getItemCount() {
        return myData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, cost, date;
        ImageButton imageButton;
        Expense expense;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.expense = expense;
            name = itemView.findViewById(R.id.tv_name);
            cost = itemView.findViewById(R.id.textView4);
            date = itemView.findViewById(R.id.tv_date);

            imageButton = itemView.findViewById(R.id.imageButton);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), ShowExpense.class);
                    intent.putExtra(MainActivity.MUSIC_TRACK_KEY, expense);
                    v.getContext().startActivity(intent);
                }
            });

            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("demo", "clicked: " + expense.getExpenseName());
                    Intent intent = new Intent(v.getContext(), EditExpense.class);
                    intent.putExtra(MainActivity.MUSIC_TRACK_KEY, expense);
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}
