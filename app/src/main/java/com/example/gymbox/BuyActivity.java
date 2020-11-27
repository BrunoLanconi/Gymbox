package com.example.gymbox;


// Importing general functions
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


/**
 * Buy activity
 */
public class BuyActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);
        // Instancing objects and defining variables
        Button bReturn = (Button) findViewById(R.id.bReturn);
        // Setting listeners
        bReturn.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(),  StudentActivity.class);
            startActivity(intent);
            finish();
        });

    }


}
