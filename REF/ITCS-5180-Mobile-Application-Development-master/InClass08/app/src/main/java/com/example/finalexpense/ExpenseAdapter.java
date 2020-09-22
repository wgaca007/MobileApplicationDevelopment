package com.example.finalexpense;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ExpenseAdapter extends ArrayAdapter<ExpenseDetails> {


    public ExpenseAdapter(@NonNull Context context, int resource, @NonNull List<ExpenseDetails> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ExpenseDetails expenseDetails = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_list_view, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.textView_expensename = convertView.findViewById(R.id.textView_ExpenseName);
            viewHolder.textView_amount = convertView.findViewById(R.id.textView_ExpenseAmount);

            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();

        viewHolder.textView_expensename.setText(expenseDetails.getExpenseName());
        viewHolder.textView_amount.setText(expenseDetails.getAmount());

        return convertView;
    }

    private static class ViewHolder {
        TextView textView_expensename;
        TextView textView_amount;
    }
}

