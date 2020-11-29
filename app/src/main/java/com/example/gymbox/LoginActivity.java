package com.example.gymbox;


// Importing general functions
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


/**
 * Login activity
 */
public class LoginActivity extends AppCompatActivity {

    // Defining internal controllers
    private final RegisterFunctions registerFunctions = new RegisterFunctions(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Instancing objects and defining internal variables
        Button bAccess = findViewById(R.id.bAccess);
        Button bRegister = findViewById(R.id.bRegister);
        Button bGym = findViewById(R.id.bGym);
        EditText etEmail = findViewById(R.id.etEmail);
        EditText etPassword = findViewById(R.id.etPassword);
        TextView tvForgotPassword = findViewById(R.id.tvForgotPassword);
        // Setting listeners
        bAccess.setOnClickListener(v -> registerFunctions.signIn(etEmail.getText().toString(), etPassword.getText().toString()));
        bRegister.setOnClickListener(v -> {
            registerFunctions.registerUser(etEmail.getText().toString(), etPassword.getText().toString());
        });
        bGym.setOnClickListener(v -> {
            Intent intent = new Intent(this,  GymActivity.class);
            startActivity(intent);
            finish();
        });
        tvForgotPassword.setOnClickListener(v -> registerFunctions.sendResetMail(etEmail.getText().toString()));

    }

}