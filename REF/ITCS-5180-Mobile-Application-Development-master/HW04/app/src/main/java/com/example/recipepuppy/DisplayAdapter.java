package com.example.recipepuppy;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DisplayAdapter extends RecyclerView.Adapter<DisplayAdapter.ViewHolder> {

    ArrayList<Recipe> recipes;
    public DisplayAdapter(ArrayList<Recipe> recipes){
        this.recipes = recipes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recipe_display, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Recipe recipe = recipes.get(i);
        Log.d("Adapter", recipe.toString());
        viewHolder.myTitle.setText("Title: "+ recipe.name);
        viewHolder.myIngredients.setText("Ingredients: "+ recipe.ingredients);
        if(!recipe.image.equals("")){
            Picasso.get().load(recipe.image).into(viewHolder.myThumbnail);
        }
        viewHolder.myLink.setText("URL: "+ recipe.myurl);
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView myTitle, myIngredients, myLink;
        ImageView myThumbnail;



        public ViewHolder(@NonNull View itemView){
            super(itemView);

            myTitle = (TextView)itemView.findViewById(R.id.tvTitle);
            myIngredients = (TextView)itemView.findViewById(R.id.tvIngredients);
            myLink = (TextView)itemView.findViewById(R.id.tvUrl);
            myThumbnail = (ImageView)itemView.findViewById(R.id.ivThumbnail);



        }
    }
}
