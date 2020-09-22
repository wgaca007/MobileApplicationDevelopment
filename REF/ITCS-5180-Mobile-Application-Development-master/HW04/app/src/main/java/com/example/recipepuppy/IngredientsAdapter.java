package com.example.recipepuppy;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ViewHolder>{


    ArrayList<String> ingredients = new ArrayList();
    IngredientsInterface ingredientsListener;
    Context context;

    public IngredientsAdapter(Context context, ArrayList<String> ingredients, IngredientsInterface ingredientsListener){
        this.context = context;
        this.ingredients = ingredients;
        this.ingredientsListener = ingredientsListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.add_ingredient_recycler, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {

        viewHolder.buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int posititon = viewHolder.getAdapterPosition();
                ingredients.add(viewHolder.inputIngredient.getText().toString());
//                viewHolder.inputIngredient.setVisibility(View.INVISIBLE);
                viewHolder.buttonAdd.hide();
                viewHolder.buttonDelete.show();
                ingredientsListener.getIngredients(ingredients);
                viewHolder.inputIngredient.setText(ingredients.get(posititon));
            }
        });

        viewHolder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                ingredients.remove(position);
                ingredientsListener.getIngredients(ingredients);
                notifyItemRemoved(position);
                viewHolder.buttonAdd.show();
                viewHolder.buttonDelete.hide();
            }
        });

    }

    @Override
    public int getItemCount() {
        return ingredients.size() + 1;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        EditText inputIngredient;
        FloatingActionButton buttonAdd;
        FloatingActionButton buttonDelete;


        public ViewHolder(@NonNull View itemView){
            super(itemView);
            inputIngredient = (EditText)itemView.findViewById(R.id.etIngredient);
            buttonAdd = (FloatingActionButton)itemView.findViewById(R.id.floatingActionButtonAdd);
            buttonDelete = (FloatingActionButton)itemView.findViewById(R.id.floatingActionButtonDelete);
        }
    }

    public interface IngredientsInterface {
        public void getIngredients(ArrayList<String> ingredients);
    }

}
