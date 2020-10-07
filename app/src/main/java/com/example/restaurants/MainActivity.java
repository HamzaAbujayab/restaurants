package com.example.restaurants;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText emailEd = findViewById(R.id.emailEd);
        final EditText passwordEd = findViewById(R.id.passwordEd);
        Button signBtn = findViewById(R.id.signBtn);

        signBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emailEd.getText().toString().equals("1") && passwordEd.getText().toString().equals("1")){
                    Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                    startActivity(intent);
                    Toast.makeText(MainActivity.this, "Admin", Toast.LENGTH_SHORT).show();

                }else if (emailEd.getText().toString().equals("2") && passwordEd.getText().toString().equals("2")){
                    Intent intent = new Intent(getApplicationContext(), CustomerActivity.class);
                    startActivity(intent);
                    Toast.makeText(MainActivity.this, "Customer", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MainActivity.this, "The entry is wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
