package com.example.gymbox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    // Defining internal controllers
    private final RegisterFunctions registerFunctions = new RegisterFunctions(this);


    // Defining variables
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Instancing objects and defining internal variables
        Button bAccess = findViewById(R.id.bAccess);
        Button bRegister = findViewById(R.id.bRegister);
        EditText etEmail = findViewById(R.id.etEmail);
        EditText etPassword = findViewById(R.id.etPassword);
        TextView tvForgotPassword = findViewById(R.id.tvForgotPassword);
        context = this;
        // Setting listeners
        bAccess.setOnClickListener(v -> registerFunctions.signIn(etEmail.getText().toString(), etPassword.getText().toString()));
        bRegister.setOnClickListener(v -> {
            registerFunctions.registerUser(etEmail.getText().toString(), etPassword.getText().toString());
        });
        tvForgotPassword.setOnClickListener(v -> registerFunctions.sendResetMail(etEmail.getText().toString()));

    }

}