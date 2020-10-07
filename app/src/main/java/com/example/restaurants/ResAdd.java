package com.example.restaurants;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ResAdd extends AppCompatActivity {
    Toolbar toolbar;
    ImageView imageViewAddRestaurants;
    RatingBar ratingBarAddRestaurants;
    EditText nameAddRestaurants;
    EditText addressAddRestaurants;
    EditText latitudeAddRestaurants;
    EditText longitudeAddRestaurants;
    Button btnAddRestaurants;
    ProgressDialog progressDialog;
    FirebaseFirestore db;
    String imgenc;
    String pImageViewRestaurant, pNameAddRestaurants, pAddressAddRestaurants, pRatingRestaurant, pLatitudeRestaurant, pLongitudeRestaurant, pIdRestaurant;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_res_add);

        toolbar = findViewById(R.id.toolbarAddRestaurants);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Restaurants");

        imageViewAddRestaurants = findViewById(R.id.imageViewAddRestaurants);
        ratingBarAddRestaurants = findViewById(R.id.ratingBarAddRestaurants);
        nameAddRestaurants = findViewById(R.id.nameAddRestaurants);
        addressAddRestaurants = findViewById(R.id.addressAddRestaurants);
        latitudeAddRestaurants = findViewById(R.id.latitudeAddRestaurants);
        longitudeAddRestaurants = findViewById(R.id.longitudeAddRestaurants);
        btnAddRestaurants = findViewById(R.id.btnAddRestaurants);

        imageViewAddRestaurants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        progressDialog = new ProgressDialog(this);
        db = FirebaseFirestore.getInstance();

        // Update Data -----------------------------------------

        final Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            toolbar.setTitle("Update Restaurants");
            btnAddRestaurants.setText("Update Restaurants");
            // get data
            pImageViewRestaurant = bundle.getString("pImageViewRestaurant");
            pNameAddRestaurants = bundle.getString("pNameRestaurant");
            pAddressAddRestaurants = bundle.getString("pAddressRestaurant");
            pRatingRestaurant = bundle.getString("pRatingRestaurant");
            pLatitudeRestaurant = bundle.getString("pLatitudeRestaurant");
            pLongitudeRestaurant = bundle.getString("pLongitudeRestaurant");
            pIdRestaurant = bundle.getString("pIdsRestaurant");
            // set data
            imageViewAddRestaurants.setImageBitmap(decodeImage(pImageViewRestaurant));
            nameAddRestaurants.setText(pNameAddRestaurants);
            addressAddRestaurants.setText(pAddressAddRestaurants);
            ratingBarAddRestaurants.setRating(Float.parseFloat(pRatingRestaurant));
            latitudeAddRestaurants.setText(pLatitudeRestaurant);
            longitudeAddRestaurants.setText(pLongitudeRestaurant);
            imgenc=pImageViewRestaurant;
        }else {
            toolbar.setTitle("Add Restaurants");
            btnAddRestaurants.setText("Add Restaurants");
        }
        //----------------------------------


        btnAddRestaurants = findViewById(R.id.btnAddRestaurants);
        btnAddRestaurants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imgenc != null && !nameAddRestaurants.getText().toString().isEmpty() && !addressAddRestaurants.getText().toString().isEmpty() && !latitudeAddRestaurants.getText().toString().isEmpty() && !longitudeAddRestaurants.getText().toString().isEmpty()) {
                    Bundle bundle1 = getIntent().getExtras();
                    if(bundle != null){
                        // updating
                        String image = imgenc;
                        String rating = ratingBarAddRestaurants.getRating()+"";
                        String name = nameAddRestaurants.getText().toString().trim();
                        String address = addressAddRestaurants.getText().toString().trim();
                        String latitude = latitudeAddRestaurants.getText().toString().trim();
                        String longitude = longitudeAddRestaurants.getText().toString().trim();
                        updateData(pIdRestaurant,image, rating, name, address, latitude, longitude);
                        startActivity(new Intent(getApplicationContext(),AdminActivity.class));
                    }else {
                        // adding new
                        String image = imgenc;
                        String rating = ratingBarAddRestaurants.getRating()+"";
                        String name = nameAddRestaurants.getText().toString().trim();
                        String address = addressAddRestaurants.getText().toString().trim();
                        String latitude = latitudeAddRestaurants.getText().toString().trim();
                        String longitude = longitudeAddRestaurants.getText().toString().trim();

                        uploadData(image, rating, name, address, latitude, longitude);
                        startActivity(new Intent(getApplicationContext(), AdminActivity.class));
                    }
                }else {
                    Toast.makeText(ResAdd.this, "Fill out all the information", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    private void updateData(String id,String image, String rating, String name, String address, String latitude, String longitude) {
        progressDialog.setTitle("Updating Data...");
        progressDialog.show();

        db.collection("Restaurants").document(id)
                .update("image", image, "rating", rating, "name", name, "address", address, "latitude", latitude, "longitude", longitude)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        Toast.makeText(ResAdd.this, "Updated...", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(ResAdd.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadData(String image, String rating, String name, String address, String latitude, String longitude) {
        progressDialog.setTitle("Adding Data to Firestore");
        progressDialog.show();

        String id = UUID.randomUUID().toString();

        Map<String, Object> res = new HashMap<>();
        res.put("id", id);
        res.put("image", image);
        res.put("rating", rating);
        res.put("name", name);
        res.put("address", address);
        res.put("latitude", latitude);
        res.put("longitude", longitude);

        db.collection("Restaurants").document(id).set(res)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.dismiss();
                        Toast.makeText(ResAdd.this, "Uploaded...", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(ResAdd.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void openGallery()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK
                && null != data) {
            Uri selectedImage = data.getData();
            imgenc = encodeImage(selectedImage);
            imageViewAddRestaurants.setImageBitmap(decodeImage(imgenc));
        }
    }
    private String encodeImage(Uri uri)
    {
        InputStream fis = null;
        try{
            fis = getContentResolver().openInputStream(uri);
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
        Bitmap bm = BitmapFactory.decodeStream(fis);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);
        //Base64.de
        return encImage;
    }

    private Bitmap decodeImage(String data)
    {
        byte[] b = Base64.decode(data,Base64.NO_WRAP);
        Bitmap bmp = BitmapFactory.decodeByteArray(b,0,b.length);
        return bmp;
    }
}
