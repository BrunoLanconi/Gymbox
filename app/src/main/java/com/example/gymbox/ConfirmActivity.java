package com.example.gymbox;


// Importing general functions
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;


/**
 * Confirm activity
 */
public class ConfirmActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);
        // Instancing objects
        // Defining variables
        Button bReturn = (Button) findViewById(R.id.bReturn);
        // Setting listeners
        bReturn.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(),  BuyActivity.class);
            startActivity(intent);
            finish();
        });

    }


}
