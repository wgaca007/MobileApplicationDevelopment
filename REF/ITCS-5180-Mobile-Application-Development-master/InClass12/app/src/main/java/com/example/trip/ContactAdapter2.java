package com.example.trip;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.trip.Places;
import com.example.trip.R;

import java.util.ArrayList;

public class ContactAdapter2 extends RecyclerView.Adapter<com.example.trip.ContactAdapter2.ViewHolder>  {
    private final OnItemCheckListener onItemClick;
    ArrayList<Places> myData = new ArrayList<Places>();
    ArrayList<Places> SelectedPlaces = new ArrayList<>();
    @NonNull
    private OnItemCheckListener onItemCheckListener;

    interface OnItemCheckListener {
        void onItemCheck(Places item);
        void onItemUncheck(Places item);
    }



    public ContactAdapter2(ArrayList<Places> myData, @NonNull OnItemCheckListener onItemCheckListener) {
        this.myData = myData;
        this.onItemClick = onItemCheckListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_places, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        if (viewHolder instanceof ViewHolder) {
            final Places places = myData.get(i);

            viewHolder.textView_PlaceName.setText(places.getPlaceName());
            viewHolder.textView_CategoryValue.setText(places.getPlaceCategory());
            viewHolder.textView_LatitudeValue.setText(places.getLati());
            viewHolder.textView_LongitudeValue.setText(places.getLogi());

            ((ViewHolder) viewHolder).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((ViewHolder) viewHolder).checkBox_Selected.setChecked(
                            !((ViewHolder) viewHolder).checkBox_Selected.isChecked());
                    if (((ViewHolder) viewHolder).checkBox_Selected.isChecked()) {
                        onItemClick.onItemCheck(places);
                    } else {
                        onItemClick.onItemUncheck(places);
                    }
                }
            });

            viewHolder.myplaces = places;
        }
    }

    @Override
    public int getItemCount() {
        return myData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView_LatitudeValue, textView_LongitudeValue, textView_CategoryValue,
                textView_PlaceName;
        CheckBox checkBox_Selected;
        Places myplaces;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_PlaceName = itemView.findViewById(R.id.textView_PlaceNameValue);
            textView_CategoryValue = itemView.findViewById(R.id.textView_CategoyValue);
            textView_LatitudeValue = itemView.findViewById(R.id.textView_LatitudeValue);
            textView_LongitudeValue = itemView.findViewById(R.id.textView_LongitudeValue);
            checkBox_Selected = itemView.findViewById(R.id.checkBox_Selected);
            checkBox_Selected.setClickable(false);
        }

        public void setOnClickListener(View.OnClickListener onClickListener) {
            itemView.setOnClickListener(onClickListener);
        }
    }
}
