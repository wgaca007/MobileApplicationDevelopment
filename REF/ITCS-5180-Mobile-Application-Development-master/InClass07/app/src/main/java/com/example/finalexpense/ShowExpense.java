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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ShowExpense.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ShowExpense extends Fragment {
    private static final String DESCRIBABLE_KEY = "describable_key";
    private ArrayList<ExpenseDetails> mDescribable;

    private OnFragmentInteractionListener mListener;

    public ShowExpense() {
        // Required empty public constructor
    }

    public static ShowExpense newInstance(ArrayList<ExpenseDetails> describable) {
        ShowExpense fragment = new ShowExpense();
        Bundle bundle = new Bundle();
        bundle.putSerializable(DESCRIBABLE_KEY, describable);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_show_expense, container, false);
        mDescribable = (ArrayList<ExpenseDetails>) getArguments().getSerializable(
                DESCRIBABLE_KEY);
        if(mDescribable.size() > 0) {
            TextView tv = rootView.findViewById(R.id.textView_EmptyListDescription);
            tv.setVisibility(View.INVISIBLE);
            ListView lv = (ListView) rootView.findViewById(R.id.MyListView);
            lv.setVisibility(View.VISIBLE);
            lv.setAdapter(new ExpenseAdapter(getActivity(), R.layout.fragment_show_expense, mDescribable));

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    Fragment fragment = ExpenseInformation.newInstance(mDescribable, position);
                    ft.replace(R.id.container, fragment);
                    ft.commit();
                }
            });
        }
        else
        {
            TextView tv = rootView.findViewById(R.id.textView_EmptyListDescription);
            tv.setVisibility(View.VISIBLE);
            ListView lv = (ListView) rootView.findViewById(R.id.MyListView);
            lv.setVisibility(View.INVISIBLE);
        }

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().findViewById(R.id.button_Add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().findViewById(R.id.textView_EmptyListDescription).setVisibility(View.INVISIBLE);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new AddExpense(), "AddExpense")
                        .commit();
            }
        });

        ListView lv = (ListView)getActivity().findViewById(R.id.MyListView);
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                mDescribable.remove(position);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment fragment = ShowExpense.newInstance(mDescribable);
                ft.replace(R.id.container, fragment);
                ft.commit();

                Toast.makeText(getActivity(), "Expense deleted", Toast.LENGTH_LONG).show();

                return true;
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
