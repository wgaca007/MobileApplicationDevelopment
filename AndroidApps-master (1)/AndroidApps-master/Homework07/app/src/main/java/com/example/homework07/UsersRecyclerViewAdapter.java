package com.example.homework07;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.homework07.UsersFragment.OnListFragmentInteractionListener;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

/**
 * {@link RecyclerView.Adapter} that can display a {@link } and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class UsersRecyclerViewAdapter extends FirestoreRecyclerAdapter<User, UsersRecyclerViewAdapter.ViewHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    String tripid;
    UsersFragment.OnListFragmentInteractionListener mListener;

    public UsersRecyclerViewAdapter(@NonNull FirestoreRecyclerOptions<User> options, UsersFragment.OnListFragmentInteractionListener mListener, String tripid) {
        super(options);
        this.mListener = mListener;
        this.tripid = tripid;
    }





/*    public UsersRecyclerViewAdapter(List<DummyItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }*/

/*    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_users, parent, false);
        return new ViewHolder(view);
    }*/

/*    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.user.setText(mValues.get(position).id);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }*/

    @NonNull
    @Override
    public UsersRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_users, parent, false);
        return new UsersRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull final User model) {
        holder.user.setText(model.firstname + " " + model.lastname);

        if(this.tripid == null){
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    model.setSelected(!model.isSelected());
                    v.setBackgroundColor(model.isSelected() ? Color.CYAN : Color.WHITE);
                    mListener.onListFragmentInteraction(model);
                }
            });
        }
        else
            holder.mView.setOnClickListener(null);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView user;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            user = view.findViewById(R.id.user);
        }
    }
}
