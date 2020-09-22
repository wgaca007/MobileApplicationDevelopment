package com.example.homework07;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.homework07.TripsFragment.OnListFragmentInteractionListener;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link } and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class TripsRecyclerViewAdapter extends FirestoreRecyclerAdapter<Trip, TripsRecyclerViewAdapter.ViewHolder> {

    //private final List<Trip> trips;
    private final TripsFragment.OnListFragmentInteractionListener mListener;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public TripsRecyclerViewAdapter(@NonNull FirestoreRecyclerOptions<Trip> options, OnListFragmentInteractionListener mListener) {
        super(options);
        this.mListener = mListener;
    }



    /*public TripsRecyclerViewAdapter(List<Trip> items, OnListFragmentInteractionListener listener) {
        trips = items;
        mListener = listener;
    }*/

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trip_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Trip model) {
        final Context context = holder.mView.getContext();
        holder.triptitle.setText(model.title);
        holder.triplocationtext.setText(model.location);
        Picasso.get().load(model.getCoverphotourl()).into(holder.tripcoverphoto);
        final Trip trip = model;
        /*final LinearLayout cardviewbglayout = holder.tripcoverphoto;
        final Trip trip = model;
        Picasso.get().load(model.getCoverphotourl()).into(new Target(){

            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                cardviewbglayout.setBackground(new BitmapDrawable(context.getResources(), bitmap));
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }


            @Override
            public void onPrepareLoad(final Drawable placeHolderDrawable) {
                Log.d("TAG", "Prepare Load");
            }
        });*/


        if(trip.creator.equals(userid)){
            holder.button.setText("Delete");
            holder.button.setBackgroundColor(Color.BLUE);
        }
        else if(trip.users.contains(userid)){
            holder.button.setText("leave");
            holder.button.setBackgroundColor(Color.BLUE);
        }
        else if(trip.triprequests.contains(userid)) {
            holder.button.setText("REQUESTED");
            holder.button.setBackgroundColor(Color.GRAY);
        }
        else{
            holder.button.setText("JOIN");
            holder.button.setBackgroundColor(Color.RED);
        }

        if(trip.users.contains(userid)){
             holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mListener) {
                        // Notify the active callbacks interface (the activity, if the
                        // fragment is attached to one) that an item has been selected.
                        mListener.onListFragmentInteraction(trip);
                    }
                }
            });
        }
        else{
            holder.mView.setOnClickListener(null);
        }



        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(trip.creator.equals(userid)){
                    db.collection("trips").document(trip.tripid).delete();
                    db.collection("Users").document(userid).update("trips", FieldValue.arrayRemove(trip.tripid));
                }
                else if(trip.users.contains(userid)) {
                    db.collection("trips").document(trip.tripid).update("users", FieldValue.arrayRemove(userid));
                    db.collection("Users").document(userid).update("trips", FieldValue.arrayRemove(trip.tripid));
                }
                else
                    db.collection("trips").document(trip.tripid).update("triprequests", FieldValue.arrayUnion(userid));
            }
        });
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView triptitle;
        public final TextView triplocationtext;
        public final ImageView tripcoverphoto;
        public final Button button;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            triptitle = view.findViewById(R.id.cardlayout).findViewById(R.id.tripitemtitle);
            triplocationtext = view.findViewById(R.id.locationlayout).findViewById(R.id.location);
            tripcoverphoto = view.findViewById(R.id.tripcoverphoto);
            button = view.findViewById(R.id.join);
        }

    }
}
