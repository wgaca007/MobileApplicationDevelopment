package com.example.recipepuppy;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the

 * to handle interaction events.
 */
public class DisplayFragment extends Fragment {

    ArrayList<Recipe> recipes = new ArrayList();
    public void onDataReceived(ArrayList<Recipe> data){
        recipes = data;
        Log.d("Display recipe", recipes.toString());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RecyclerView mrecyclerview = (RecyclerView)getActivity().findViewById(R.id.rv_display);
        mrecyclerview.setHasFixedSize(true);
        RecyclerView.LayoutManager mlayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView.Adapter mAdapter = new DisplayAdapter(recipes);
        mrecyclerview.setLayoutManager(mlayoutManager);
        mrecyclerview.setAdapter(mAdapter);


        getActivity().findViewById(R.id.buttonFinish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.finshButtonPressed();
            }
        });

    }



    private onFinishDisplayIterface mListener;

    public DisplayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_display, container, false);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            mListener = (DisplayFragment.onFinishDisplayIterface) activity;
        }catch (ClassCastException e){
            throw new ClassCastException(activity.toString()+"Should implement");
        }
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface onFinishDisplayIterface{
        // TODO: Update argument type and name
        public void finshButtonPressed();
    }
}
