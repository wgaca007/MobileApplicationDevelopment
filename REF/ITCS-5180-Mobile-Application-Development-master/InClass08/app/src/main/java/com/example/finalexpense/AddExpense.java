package com.example.finalexpense;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddExpense.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class AddExpense extends Fragment {

    private OnFragmentInteractionListener mListener;
    int expenseId = 0;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    public AddExpense() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_expense, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().findViewById(R.id.button_AddExpense).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExpenseDetails ed = new ExpenseDetails();

                EditText expenseName = getActivity().findViewById(R.id.editText_ExpenseNameValue);
                Spinner category = getActivity().findViewById(R.id.spinner_Category);
                EditText amountValue = getActivity().findViewById(R.id.editText_ExpenseAmountValue);

                String pattern = "MM/dd/yyyy";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

                String date = simpleDateFormat.format(new Date());

                if (expenseName.getText().toString().isEmpty() || category.getSelectedItem().toString().isEmpty() || amountValue.getText().toString().isEmpty()
                        || category.getSelectedItem().toString().compareTo("Select Category") == 0) {
                    Toast.makeText(getActivity(), "Some Fields are empty", Toast.LENGTH_LONG).show();
                } else {
                    ed.setExpenseName(expenseName.getText().toString());
                    ed.setCategory(category.getSelectedItem().toString());
                    ed.setAmount(amountValue.getText().toString());
                    ed.setDate(date);


                    DatabaseReference ref2 = mDatabase.child("users");
                    String key = ref2.push().getKey();
                    ed.setExpenseID(key);
                    ref2.child(key).setValue(ed);
                    mListener.sendExpense(ed);
                }
            }
        });

        getActivity().findViewById(R.id.button_Cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment fragment = ShowExpense.newInstance();
                ft.replace(R.id.container, fragment);
                ft.commit();
            }
        });
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void sendExpense(ExpenseDetails ed);
    }
}
