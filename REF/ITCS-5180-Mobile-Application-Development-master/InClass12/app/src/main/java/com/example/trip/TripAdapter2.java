package com.example.trip;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TripAdapter2 extends RecyclerView.Adapter<com.example.trip.TripAdapter2.ViewHolder> {
    ArrayList<Trips> myData = new ArrayList<>();

    public TripAdapter2(ArrayList<Trips> myData) {
        this.myData = myData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_trip, viewGroup, false);
        TripAdapter2.ViewHolder viewHolder = new TripAdapter2.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        Trips trips = myData.get(i);
        viewHolder.textView_TripNameValue2.setText(trips.getTripName());
        viewHolder.textView_DateTrip.setText(trips.getDate());

        viewHolder.this_trip = trips;
    }

    @Override
    public int getItemCount() {
        return myData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView_TripNameValue2;
        TextView textView_DateTrip;
        Trips this_trip;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            final Context context = itemView.getContext();
            textView_TripNameValue2 = itemView.findViewById(R.id.textView_TripNameValue2);
            textView_DateTrip = itemView.findViewById(R.id.textView_DateValue);
            this.this_trip = this_trip;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("/trips");
                    DatabaseReference ref2 = mDatabase.child(this_trip.getTripID());
                    mDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                Trips myexpense = (Trips) ds.getValue(Trips.class);
                                if (myexpense.getTripID() == this_trip.getTripID()) {
                                    Log.d("demo", "" + myexpense.getTripName());
                                    Intent intent = new Intent(context, TripMap.class);
                                    intent.putExtra(CreateTrip.MUSIC_LACK_KEY, myexpense);
                                    context.startActivity(intent);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            });
        }
    }
}
