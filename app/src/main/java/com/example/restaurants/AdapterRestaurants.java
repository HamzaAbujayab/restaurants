package com.example.restaurants;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.miguelcatalan.materialsearchview.SearchAdapter;

import java.util.ArrayList;
import java.util.List;


public class AdapterRestaurants extends RecyclerView.Adapter<AdapterRestaurants.ViewHolder> implements Filterable {

    Context context;
    List<Restaurants> restaurantsList;
    ProgressDialog progressDialog2;
    FirebaseFirestore db;
    List<Restaurants> restaurantsSearch;
    public AdapterRestaurants(Context context, List<Restaurants> restaurantsList) {
        this.context = context;
        this.restaurantsList = restaurantsList;
        restaurantsSearch = new ArrayList<>(restaurantsList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.layout_restaurants, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.imageViewRestaurant.setImageBitmap(decodeImage(restaurantsList.get(position).getImageViewRestaurant()));
        //holder.imageViewRestaurant.setImageResource(restaurantsList.get(position).getImageViewRestaurant());
        holder.nameRestaurant.setText(restaurantsList.get(position).getNameRestaurant());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,RestaurantDetails.class);
                intent.putExtra("res", restaurantsList.get(position));
                context.startActivity(intent);
             }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                String[] options = {"Update", "Delete"};
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0){
                            // Update is clicked
                            String imageViewRestaurant = restaurantsList.get(position).getImageViewRestaurant();
                            String nameRestaurant = restaurantsList.get(position).getNameRestaurant();
                            String addressRestaurant = restaurantsList.get(position).getAddressRestaurant();
                            String idsRestaurant = restaurantsList.get(position).getIdsRestaurant();
                            String ratingRestaurant = restaurantsList.get(position).getRatingRestaurant();
                            String latitudeRestaurant = restaurantsList.get(position).getLatitudeRestaurant();
                            String longitudeRestaurant = restaurantsList.get(position).getLongitudeRestaurant();


                            Intent intent = new Intent(context,ResAdd.class);
                            intent.putExtra("pImageViewRestaurant", imageViewRestaurant);
                            intent.putExtra("pNameRestaurant", nameRestaurant);
                            intent.putExtra("pAddressRestaurant", addressRestaurant);
                            intent.putExtra("pIdsRestaurant", idsRestaurant);
                            intent.putExtra("pRatingRestaurant", ratingRestaurant);
                            intent.putExtra("pLatitudeRestaurant", latitudeRestaurant);
                            intent.putExtra("pLongitudeRestaurant", longitudeRestaurant);
                            context.startActivity(intent);
                        }
                        if (which == 1){
                            // Delete is clicked
                            progressDialog2 = new ProgressDialog(context);
                            progressDialog2.setTitle("Delete Data...");
                            progressDialog2.show();
                            db = FirebaseFirestore.getInstance();
                            db.collection("Restaurants").document(restaurantsList.get(position).getIdsRestaurant())
                                    .delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(context, "Deleted...", Toast.LENGTH_SHORT).show();

                                            db.collection("Restaurants")
                                                    .get()
                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                            restaurantsList.clear();
                                                            progressDialog2.dismiss();
                                                            for (DocumentSnapshot res: task.getResult()){
                                                                Restaurants restaurants = new Restaurants(res.getString("image"),
                                                                        res.getString("name"),
                                                                        res.getString("address"),
                                                                        res.getString("id"),
                                                                        res.getString("rating"),
                                                                        res.getString("latitude"),
                                                                        res.getString("longitude")
                                                                );
                                                                restaurantsList.add(restaurants);
                                                            }
                                                           notifyDataSetChanged();
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            progressDialog2.dismiss();
                                                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    });


                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog2.dismiss();
                                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }).create().show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return restaurantsList.size();
    }
// Search Filter -------------
    @Override
    public Filter getFilter() {
        return searchFilter;
    }

    private Filter searchFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Restaurants> filterList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0){
                filterList.addAll(restaurantsSearch);
            }else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Restaurants res: restaurantsSearch){
                    if (res.getNameRestaurant().toLowerCase().contains(filterPattern)){
                        filterList.add(res);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filterList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            restaurantsList.clear();
            if (restaurantsList != null) {
                restaurantsList.addAll((List) results.values);
            }else {
                Toast.makeText(context, "اللست فاضية", Toast.LENGTH_SHORT).show();
            }
            notifyDataSetChanged();
        }
    };

    private Bitmap decodeImage(String data)
    {
        byte[] b = Base64.decode(data,Base64.NO_WRAP);
        Bitmap bmp = BitmapFactory.decodeByteArray(b,0,b.length);
        return bmp;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageViewRestaurant;
        TextView nameRestaurant;
        RecyclerView recyclerViewRestaurants;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewRestaurant = itemView.findViewById(R.id.imageViewRestaurant);
            nameRestaurant = itemView.findViewById(R.id.nameRestaurant);
        }
    }
}

