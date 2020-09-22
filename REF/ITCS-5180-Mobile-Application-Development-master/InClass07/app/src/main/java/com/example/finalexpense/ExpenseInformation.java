package com.example.finalexpense;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ExpenseInformation.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ExpenseInformation extends Fragment {

    private static int pos;
    private OnFragmentInteractionListener mListener;
    private static final String DESCRIBABLE_KEY = "expense_information";
    private ArrayList<ExpenseDetails> mDescribable;

    public ExpenseInformation() {
        // Required empty public constructor
    }

    public static ExpenseInformation newInstance(ArrayList<ExpenseDetails> describable, int i) {
        ExpenseInformation fragment = new ExpenseInformation();
        Bundle bundle = new Bundle();
        bundle.putSerializable(DESCRIBABLE_KEY, describable);
        fragment.setArguments(bundle);
        pos = i;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_expense_information, container, false);

        mDescribable = (ArrayList<ExpenseDetails>) getArguments().getSerializable(
                DESCRIBABLE_KEY);

        TextView nameValue = rootView.findViewById(R.id.textView_NameValue);
        TextView categoryValue = rootView.findViewById(R.id.textView_CategoryValue);
        TextView amountValue = rootView.findViewById(R.id.textView_AmountValue);
        TextView dateValue = rootView.findViewById(R.id.textView_DateValue);

        nameValue.setText(mDescribable.get(pos).getExpenseName());
        categoryValue.setText(mDescribable.get(pos).getCategory());
        amountValue.setText(mDescribable.get(pos).getAmount());
        dateValue.setText(mDescribable.get(pos).getDate());


        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().findViewById(R.id.button_Close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment fragment = ShowExpense.newInstance(mDescribable);
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
        void onFragmentInteraction(Uri uri);
    }
}
