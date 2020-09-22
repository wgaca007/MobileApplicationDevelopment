package com.example.homework06;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyProfile.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class MyProfile extends Fragment implements View.OnClickListener{

    private OnFragmentInteractionListener mListener;
    //Drawable drawable;
    String selectedimageresource = "";
    View view;

    EditText fname, lname, studentid;
    RadioGroup departmentgroup;

    StudentInfo studentInfo;

    Gson gson = new Gson();

    static HashMap<String,Integer> map = new HashMap<String, Integer>(){{
        put("f1", R.drawable.avatar_f_1);
        put("m1", R.drawable.avatar_m_1);
        put("f2", R.drawable.avatar_f_2);
        put("m2", R.drawable.avatar_m_2);
        put("f3", R.drawable.avatar_f_3);
        put("m3", R.drawable.avatar_m_3);
    }};

    public MyProfile() {
        // Required empty public constructor
    }

    void setcomponents(SharedPreferences sharedPreferences){
        studentInfo = gson.fromJson(sharedPreferences.getString("studentinfojsonstring",""), StudentInfo.class);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_profile, container, false);
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       // getActivity().getActionBar().setTitle("My Profile");
        if(!selectedimageresource.equals("") && map.get(selectedimageresource) != null) {
            ((ImageView) view.findViewById(R.id.selectavtar)).setImageResource(map.get(selectedimageresource));
        }

        if(MainActivity.isSharedPreferenceAvailabe){

            for(String key: map.keySet()) {
                if(map.get(key).equals(studentInfo.imageresource)) {
                    selectedimageresource = key;
                }
            }

            ((EditText) view.findViewById(R.id.fname)).setText(studentInfo.fname);
            ((EditText) view.findViewById(R.id.lname)).setText(studentInfo.lname);
            ((EditText) view.findViewById(R.id.studentid)).setText(String.valueOf(studentInfo.studentid));

            ((RadioGroup) view.findViewById(R.id.departmentgroup)).check(studentInfo.department);
            ((ImageView) view.findViewById(R.id.selectavtar)).setImageResource(studentInfo.imageresource);

            MainActivity.isSharedPreferenceAvailabe = false;

        }

        view.findViewById(R.id.selectavtar).setOnClickListener(this);
        view.findViewById(R.id.save).setOnClickListener(this);

        getActivity().setTitle("My Profile");

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.save){
            view = getView();
            fname = view.findViewById(R.id.fname);
            lname = view.findViewById(R.id.lname);
            studentid = view.findViewById(R.id.studentid);
            departmentgroup = view.findViewById(R.id.departmentgroup);



            if(fname.getText().toString().equals("")){
                fname.setError("Fill the First Name");
                return;
            }
            else if(lname.getText().toString().equals("")){
                lname.setError("Fill the Last Name");
                return;
            }
            else if(studentid.getText().toString().equals("")){
                studentid.setError("Fill the Student id");
                return;
            }
            else if(departmentgroup.getCheckedRadioButtonId() == -1){
                Toast.makeText(getContext(), "Please set the Department", Toast.LENGTH_SHORT).show();
                return;
            }
            else if(selectedimageresource.equals("")){
                Toast.makeText(getContext(), "Select the Avatar", Toast.LENGTH_SHORT).show();
                return;
            }

            studentInfo = new StudentInfo(fname.getText().toString(), lname.getText().toString(), Integer.valueOf(studentid.getText().toString()), departmentgroup.getCheckedRadioButtonId(), map.get(selectedimageresource));
            mListener.setInfo(studentInfo);
        }
        else {
            mListener.onFragmentInteraction(v);
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(View v);
        void setInfo(StudentInfo studentInfo);
    }
}
