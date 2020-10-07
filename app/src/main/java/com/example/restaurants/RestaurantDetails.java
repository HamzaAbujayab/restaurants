package com.example.restaurants;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;
import java.util.List;

public class RestaurantDetails extends AppCompatActivity  implements OnMapReadyCallback{
    Toolbar toolbar;
    ImageView imageViewRestaurantDetails;
    TextView addressRestaurantDetails;
    RecyclerView recyclerView;
    List<Meals> mealsList;
    RatingBar ratingBar;
    Restaurants intent;
    GoogleMap map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ratingBar = findViewById(R.id.ratingBar);
        imageViewRestaurantDetails = findViewById(R.id.imageViewRestaurantDetails);
        intent = (Restaurants) getIntent().getSerializableExtra("res");
        imageViewRestaurantDetails.setImageBitmap(decodeImage(intent.getImageViewRestaurant()));
        addressRestaurantDetails = findViewById(R.id.addressRestaurantDetails);
        addressRestaurantDetails.setText(intent.getAddressRestaurant());
        ratingBar.setRating(Float.parseFloat(intent.getRatingRestaurant()));
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(intent.getNameRestaurant());

        ratingBar = findViewById(R.id.ratingBar);
        ratingBar.setEnabled(false);

        mealsList = new ArrayList<>();
        mealsList.add(new Meals(R.drawable.m1, "همبرقر"));
        mealsList.add(new Meals(R.drawable.m2, "بيتزا"));
        mealsList.add(new Meals(R.drawable.m3, "تشكن بيتزا"));
        mealsList.add(new Meals(R.drawable.m4, "قطع لحمة"));
        mealsList.add(new Meals(R.drawable.m5, "سمك"));

        recyclerView = findViewById(R.id.recyclerViewTabMeals);
        AdapterMeals adapterMeals = new AdapterMeals(getApplicationContext(), mealsList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapterMeals);
        adapterMeals.notifyDataSetChanged();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        map.getUiSettings().setZoomControlsEnabled(true);
        LatLng latLng = new LatLng(Double.parseDouble(intent.getLatitudeRestaurant()),Double.parseDouble(intent.getLongitudeRestaurant()));
        map.addMarker(new MarkerOptions().position(latLng).title(intent.getNameRestaurant())).showInfoWindow();
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
    }

    private Bitmap decodeImage(String data)
    {
        byte[] b = Base64.decode(data,Base64.NO_WRAP);
        Bitmap bmp = BitmapFactory.decodeByteArray(b,0,b.length);
        return bmp;
    }
}
