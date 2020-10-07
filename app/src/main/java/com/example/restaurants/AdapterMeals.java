package com.example.restaurants;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterMeals extends RecyclerView.Adapter<AdapterMeals.ViewHolder> {

    Context context;
    List<Meals> mealsList;

    public AdapterMeals(Context context, List<Meals> mealsList) {
        this.context = context;
        this.mealsList = mealsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.layout_meals, parent, false);
        AdapterMeals.ViewHolder viewHolder = new AdapterMeals.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imageViewMeals.setImageResource(mealsList.get(position).getImageViewMeals());
        holder.nameMeals.setText(mealsList.get(position).getNameMeals());
    }

    @Override
    public int getItemCount() {
        return mealsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageViewMeals;
        TextView nameMeals;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewMeals = itemView.findViewById(R.id.imageViewMeals);
            nameMeals = itemView.findViewById(R.id.nameMeals);
        }
    }
}
