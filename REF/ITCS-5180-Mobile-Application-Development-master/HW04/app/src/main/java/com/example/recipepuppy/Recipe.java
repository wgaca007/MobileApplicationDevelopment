package com.example.recipepuppy;

public class Recipe {
    String name, myurl, ingredients, image;

    @Override
    public String toString() {
        return "Recipe{" +
                "name='" + name + '\'' +
                ", myurl='" + myurl + '\'' +
                ", ingredients='" + ingredients + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
