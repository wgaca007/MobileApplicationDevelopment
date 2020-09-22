package com.example.finalexpense;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddExpense.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class AddExpense extends Fragment {

    private OnFragmentInteractionListener mListener;

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
                EditText amount = getActivity().findViewById(R.id.editText_ExpenseAmountValue);

                String pattern = "MM/dd/yyyy";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

                String date = simpleDateFormat.format(new Date());

                if (expenseName.getText().toString().isEmpty() || category.getSelectedItem().toString().isEmpty() || amount.getText().toString().isEmpty()
                || category.getSelectedItem().toString().compareTo("Select Category") == 0) {
                    Toast.makeText(getActivity(), "Some Fields are empty", Toast.LENGTH_LONG).show();
                } else {
                    ed.setExpenseName(expenseName.getText().toString());
                    ed.setCategory(category.getSelectedItem().toString());
                    ed.setAmount(amount.getText().toString());
                    ed.setDate(date);
                    mListener.sendExpense(ed);
                }
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
        void sendExpense(ExpenseDetails ed);
    }
}
