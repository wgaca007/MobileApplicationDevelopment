package com.example.homework06;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DisplayMyProfile.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class DisplayMyProfile extends Fragment {

    private OnFragmentInteractionListener mListener;

    String name, studentid,department, totaljson;
    Gson gson = new Gson();
    StudentInfo studentInfo;

    public DisplayMyProfile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_display_my_profile, container, false);
    }

    void setcomponents(SharedPreferences sharedPreferences){
        studentInfo = gson.fromJson(sharedPreferences.getString("studentinfojsonstring", ""), StudentInfo.class);
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View v = getView();

        ((ImageView)v.findViewById(R.id.myavatar)).setImageResource(studentInfo.imageresource);
        ((TextView)v.findViewById(R.id.nameval)).setText(studentInfo.fname+" " + studentInfo.lname);
        ((TextView)v.findViewById(R.id.studentidval)).setText(String.valueOf(studentInfo.studentid));

       if(studentInfo.department == R.id.cs)
           department="CS";
       else if(studentInfo.department == R.id.sis)
           department="SIS";
       else if(studentInfo.department == R.id.bio)
           department="BIO";
       else
           department="Other";


        ((TextView)v.findViewById(R.id.departmentval)).setText(department);

        view.findViewById(R.id.edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.EditProfile();
            }
        });

        getActivity().setTitle("Display My Profile");
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        //void onFragmentInteraction(Uri uri);
        void EditProfile();
    }
}
