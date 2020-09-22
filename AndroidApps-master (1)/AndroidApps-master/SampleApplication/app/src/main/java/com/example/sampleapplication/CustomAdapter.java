package com.example.sampleapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.List;

public class CustomAdapter extends ArrayAdapter<String> {
    public CustomAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        TextView tv;
        String result = getItem(position);
        ViewHolder viewHolder;

        if(convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_item, parent, false);

            viewHolder = new ViewHolder();

            viewHolder.tv1 = convertView.findViewById(R.id.id1);
            viewHolder.tv2 = convertView.findViewById(R.id.id2);

            convertView.setTag(viewHolder);
        }
        else{
            viewHolder =  (ViewHolder)convertView.getTag();
        }

        viewHolder.tv1.setText(result);
        viewHolder.tv2.setText(result);

        return convertView;
    }

    class ViewHolder {
        TextView tv1, tv2;
    }

}
