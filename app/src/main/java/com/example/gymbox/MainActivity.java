package com.example.gymbox;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.View;


/**
 * Wait activity
 */
public class MainActivity extends AppCompatActivity {


    // Defining internal controllers
    private final RegisterFunctions registerFunctions = new RegisterFunctions(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait);
        if (registerFunctions.verifyConnection()) {
            registerFunctions.enter();
        } else {
            Intent intent = new Intent(getApplicationContext(),  LoginActivity.class);
            startActivity(intent);
            finish();
        }

    }


    @Override
    protected void onStart() {
        super.onStart();

    }


}
