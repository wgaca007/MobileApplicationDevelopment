package com.example.recipepuppy;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchHandlerInterface} interface
 * to handle interaction events.
 */
public class SearchFragment extends Fragment {

    private SearchHandlerInterface mListener;

    ArrayList<String> ingredients = new ArrayList();

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle("Recipe Puppy");
        final EditText dish = (EditText)getActivity().findViewById(R.id.editTextDish);
        RecyclerView recyclerViewIngredients = (RecyclerView)getActivity().findViewById(R.id.rv_ingredients);
        recyclerViewIngredients.setHasFixedSize(true);

        ArrayList<String> ingrd = new ArrayList();
        IngredientsAdapter.IngredientsInterface ingredientsInterface = new IngredientsAdapter.IngredientsInterface() {
            @Override
            public void getIngredients(ArrayList<String> ingredient) {
                ingredients = ingredient;

            }
        };



        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        RecyclerView.Adapter adapter = new IngredientsAdapter(getActivity(), ingrd, ingredientsInterface);

        recyclerViewIngredients.setLayoutManager(layoutManager);
        recyclerViewIngredients.setAdapter(adapter);

        getActivity().findViewById(R.id.buttonSearch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ingredients.isEmpty() || dish.getText().toString().equals("")){
                    Toast.makeText(getActivity(), "Enter ingredient!", Toast.LENGTH_SHORT).show();
                }
                else{
                    String ingredientList = ingredients.get(0);
                    for(int idx = 0; idx < ingredients.size(); idx++){
                        ingredientList += ", "+ingredients.get(idx);
                    }

                    GetDataAsync.setDataInterface setDataInterface = new GetDataAsync.setDataInterface() {
                        @Override
                        public void setData(ArrayList<Recipe> myResult) {
                            ProgressBar progressBar = (ProgressBar)getActivity().findViewById(R.id.progressBar);
                            progressBar.setVisibility(View.INVISIBLE);
                            Log.d("search recipe", myResult.toString());
                            mListener.onGetRecipe(myResult);

                        }
                    };

                    String url = "http://www.recipepuppy.com/api/?i="+ingredientList+"&q="+dish.getText().toString();
                    new GetDataAsync(setDataInterface, getActivity()).execute(url);
                    ProgressBar progressBar = (ProgressBar)getActivity().findViewById(R.id.progressBar);
                    progressBar.setVisibility(View.VISIBLE);
                }

            }
        });


    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            mListener = (SearchHandlerInterface) activity;
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
    public interface SearchHandlerInterface {
        public void onGetRecipe(ArrayList<Recipe> myRecipe);

    }
}
