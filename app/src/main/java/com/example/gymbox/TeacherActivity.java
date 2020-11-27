package com.example.gymbox;


// Importing general functions
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;


/**
 * Teacher activity
 */
public class TeacherActivity extends AppCompatActivity {


    // Defining internal controllers
    private final RegisterFunctions registerFunctions = new RegisterFunctions(this);


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);
        // Instancing objects and defining internal variables
        TextView tvFirstName = findViewById(R.id.tvFirstName);
        TextView tvLastName = findViewById(R.id.tvLastName);
        TextView tvPIN = findViewById(R.id.tvPIN);
        Button bDisconnect = findViewById(R.id.bDisconnect);
        Button bChangeRegister = findViewById(R.id.bChangeRegister);
        // Setting listeners
        bDisconnect.setOnClickListener(v -> registerFunctions.signOut());
        bChangeRegister.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(),  RegisterActivity.class);
            startActivity(intent);
            finish();
        });
        tvFirstName.setText(registerFunctions.readPreference("firstName"));
        tvLastName.setText(registerFunctions.readPreference("lastName"));
        tvPIN.setText("PIN: " + registerFunctions.readPreference("PIN"));
        registerFunctions.changeLanguage(registerFunctions.readPreference("language"));

    }


}
