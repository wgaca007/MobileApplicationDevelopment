package com.example.finalexpense;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditExpense.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class EditExpense extends Fragment {

    private OnFragmentInteractionListener mListener;
    private static final String DESCRIBABLE_KEY = "describable_key";
    private ExpenseDetails mDescribable;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    public EditExpense() {
        // Required empty public constructor
    }

    public static EditExpense newInstance(ExpenseDetails describable) {
        EditExpense fragment = new EditExpense();
        Bundle bundle = new Bundle();
        bundle.putSerializable(DESCRIBABLE_KEY, describable);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_edit_expense, container, false);

        mDescribable = (ExpenseDetails) getArguments().getSerializable(
                DESCRIBABLE_KEY);

        EditText expenseName = rootView.findViewById(R.id.etEditExpenseName);
        Spinner category = rootView.findViewById(R.id.spinnerEditCategory);
        EditText amount = rootView.findViewById(R.id.etEditExpenseAmout);

        expenseName.setText(mDescribable.getExpenseName());

        String myString = mDescribable.getCategory(); //the value you want the position for
        ArrayAdapter myAdap = (ArrayAdapter) category.getAdapter(); //cast to an ArrayAdapter
        int spinnerPosition = myAdap.getPosition(myString); //set the default according to value
        category.setSelection(spinnerPosition);

        amount.setText(mDescribable.getAmount());

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().findViewById(R.id.buttonEditSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExpenseDetails ed = new ExpenseDetails();
                ed = mDescribable;
                EditText expenseName = getActivity().findViewById(R.id.etEditExpenseName);
                Spinner category = getActivity().findViewById(R.id.spinnerEditCategory);
                EditText amount = getActivity().findViewById(R.id.etEditExpenseAmout);

                if (expenseName.getText().toString().isEmpty() || category.getSelectedItem().toString().isEmpty() || amount.getText().toString().isEmpty()
                        || category.getSelectedItem().toString().compareTo("Select Category") == 0) {
                    Toast.makeText(getActivity(), "Some Fields are empty", Toast.LENGTH_LONG).show();
                } else {
                    ed.setExpenseName(expenseName.getText().toString());
                    ed.setCategory(category.getSelectedItem().toString());
                    ed.setAmount(amount.getText().toString());


                    DatabaseReference ref2 = mDatabase.child("users");
                    String key = ed.getExpenseID();
                    ref2.child(key).setValue(ed);
                    mListener.sendExpense(ed);
                }
            }
        });

        getActivity().findViewById(R.id.buttonEditCancel).setOnClickListener(new View.OnClickListener() {
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
