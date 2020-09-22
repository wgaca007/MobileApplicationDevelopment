package com.example.homework7a;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homework7a.TripsFragment.OnListFragmentInteractionListener;
import com.example.homework7a.dummy.DummyContent.DummyItem;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class TripsRecyclerViewAdapter extends FirestoreRecyclerAdapter<Trip, TripsRecyclerViewAdapter.ViewHolder> {

    //private final List<Trip> trips;
    private final OnListFragmentInteractionListener mListener;

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
        final LinearLayout cardviewbglayout = holder.cardviewbglayout;
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
        });

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


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView triptitle;
        public final TextView triplocationtext;
        public final LinearLayout cardviewbglayout;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            triptitle = view.findViewById(R.id.cardviewbglayout).findViewById(R.id.tripitemtitle);
            triplocationtext = view.findViewById(R.id.locationlayout).findViewById(R.id.location);
            cardviewbglayout = view.findViewById(R.id.cardviewbglayout);
        }


    }
}
