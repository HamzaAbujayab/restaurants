package com.example.restaurants;

import java.io.Serializable;

public class Meals implements Serializable {
    int imageViewMeals;
    String nameMeals;

    public Meals(int imageViewMeals, String nameMeals) {
        this.imageViewMeals = imageViewMeals;
        this.nameMeals = nameMeals;
    }

    public int getImageViewMeals() {
        return imageViewMeals;
    }

    public void setImageViewMeals(int imageViewMeals) {
        this.imageViewMeals = imageViewMeals;
    }

    public String getNameMeals() {
        return nameMeals;
    }

    public void setNameMeals(String nameMeals) {
        this.nameMeals = nameMeals;
    }
}
