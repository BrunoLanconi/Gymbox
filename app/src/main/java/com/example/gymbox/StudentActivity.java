package com.example.gymbox;


// Importing general functions
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;


/**
 * Student activity
 */
public class StudentActivity extends AppCompatActivity {

    // Defining internal controllers
    private final RegisterFunctions registerFunctions = new RegisterFunctions(this);


    // Defining internal variables
    private TextView tvFirstName;
    private TextView tvLastName;
    private TextView tvPIN;
    private TextView tvRemainingAccesses;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        // Instancing objects
        tvFirstName = findViewById(R.id.tvFirstName);
        tvLastName = findViewById(R.id.tvLastName);
        tvPIN = findViewById(R.id.tvPIN);
        tvRemainingAccesses = findViewById(R.id.tvRemainingAccesses);
        Button bDisconnect = findViewById(R.id.bDisconnect);
        Button bChangeRegister = findViewById(R.id.bChangeRegister);
        Button bPurchaseAccesses = findViewById(R.id.bPurchaseAccesses);
        // Setting listeners
        bDisconnect.setOnClickListener(v -> registerFunctions.signOut());
        bChangeRegister.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(),  RegisterActivity.class);
            startActivity(intent);
            finish();
        });
        bPurchaseAccesses.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(),  BuyActivity.class);
            startActivity(intent);
            finish();        });
        load();
        registerFunctions.changeLanguage(registerFunctions.readPreference("language"));

    }


    /**
     * Loads user profile
     *
     */
    @SuppressLint("SetTextI18n")
    private void load() {

        tvFirstName.setText(registerFunctions.readPreference("firstName"));
        tvLastName.setText(registerFunctions.readPreference("lastName"));
        tvPIN.setText("PIN: " + registerFunctions.readPreference("PIN"));
        if (Integer.parseInt(registerFunctions.readPreference("accesses")) < 10) {
            tvRemainingAccesses.setText("0" + registerFunctions.readPreference("accesses"));
        } else if (Integer.parseInt(registerFunctions.readPreference("accesses")) > 999)  {
            tvRemainingAccesses.setText("999+");
        } else {
            tvRemainingAccesses.setText(registerFunctions.readPreference("accesses"));
        }

    }


}
