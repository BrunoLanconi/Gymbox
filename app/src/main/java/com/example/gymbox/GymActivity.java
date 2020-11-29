package com.example.gymbox;


// Importing general functions
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


/**
 * Gym activity
 */
public class GymActivity extends AppCompatActivity {

    // Defining internal controllers
    private final RegisterFunctions registerFunctions = new RegisterFunctions(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym);
        // Instancing objects and defining internal variables
        Button bRedeem = findViewById(R.id.bRedeem);
        Button bUser = findViewById(R.id.bUser);
        EditText etPIN = findViewById(R.id.etPIN);
        // Setting listeners
        bRedeem.setOnClickListener(v -> {
            registerFunctions.redeem(etPIN.getText().toString());
            etPIN.setText("");
        });
        bUser.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

    }


}
