package com.example.restaurants;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {

    Toolbar toolbar;

    List<Restaurants> restaurantsList = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseFirestore db;
    AdapterRestaurants adapterRestaurants;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

       toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Admin");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressDialog = new ProgressDialog(this);

        db = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.recyclerViewRestaurants);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);
        showData();
    }
    private void showData() {
        progressDialog.setTitle("Loading Data...");
        progressDialog.show();
        db.collection("Restaurants")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        restaurantsList.clear();
                        progressDialog.dismiss();
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
                        adapterRestaurants = new AdapterRestaurants(AdminActivity.this, restaurantsList);
                        recyclerView.setAdapter(adapterRestaurants);
                        adapterRestaurants.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(AdminActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void deleteData(int index){
        progressDialog.setTitle("Delete Data...");
        progressDialog.show();

        db.collection("Restaurants").document(restaurantsList.get(index).getNameRestaurant())
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(AdminActivity.this, "Deleted...", Toast.LENGTH_SHORT).show();
                        showData();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(AdminActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.restaurant_details_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView)searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapterRestaurants.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item1:
                Intent item1 = new Intent(getApplicationContext(), ResAdd.class);
                startActivity(item1);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
