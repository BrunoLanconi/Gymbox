package com.example.gymbox;


// Importing general functions
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;


/**
 * Register activity
 */
public class RegisterActivity extends AppCompatActivity {


    // Defining internal controllers
    private final RegisterFunctions registerFunctions = new RegisterFunctions(this);


    // Defining variables
    private EditText etNewEmail;
    private EditText etFirstName;
    private EditText etLastName;
    private EditText etNewPassword;
    private EditText etPassword;
    private Button bView;
    private Button bView2;
    private Button bPT;
    private Button bEN;
    private Button bConfirm;
    private Button bReturn;
    private Boolean changeFirstName;
    private Boolean changeLastName;
    private Boolean changeEmail;
    private Boolean changePassword;


    // Defining Firebase variables
    private FirebaseAuth mAuth;
    private DocumentReference DocumentRef;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // Defining Firebase variables
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        // Instancing objects
        etNewEmail = (EditText) findViewById(R.id.etNewEmail);
        etFirstName = (EditText) findViewById(R.id.etFirstName);
        etLastName = (EditText) findViewById(R.id.etLastName);
        etNewPassword = (EditText) findViewById(R.id.etNewPassword);
        etPassword = (EditText) findViewById(R.id.etPassword);
        bView = (Button) findViewById(R.id.bView);
        bView2 = (Button) findViewById(R.id.bView2);
        bPT = (Button) findViewById(R.id.bPT);
        bEN = (Button) findViewById(R.id.bEN);
        bConfirm = (Button) findViewById(R.id.bConfirm);
        bReturn = (Button) findViewById(R.id.bReturn);
        // Defining variables
        changeFirstName = false;
        changeLastName = false;
        changeEmail = false;
        changePassword = false;
        load();
        registerFunctions.changeLanguage(registerFunctions.readPreference("language"));

    }


    /**
     * Loads user profile based on SharedPreferences
     *
     */
    private void load() {

        String loadFirstName = registerFunctions.readPreference("firstName");
        String loadLastName = registerFunctions.readPreference("lastName");
        String loadEmail = registerFunctions.readPreference("email");
        String loadLanguage = registerFunctions.readPreference("language");
        if (loadFirstName.equals("") || loadLastName.equals("")) {
            etNewEmail.setVisibility(View.INVISIBLE);
            etNewPassword.setVisibility(View.INVISIBLE);
            bReturn.setText(getString(R.string.disconnect));
            loadFirstListener();
        } else {
            if (!loadFirstName.equals("")) {
                etFirstName.setText(loadFirstName);
            }
            if (!loadLastName.equals("")) {
                etLastName.setText(loadLastName);
            }
            if (!loadEmail.equals("")) {
                etNewEmail.setText(loadEmail);
            }
            loadSecondListener();
        }
        switch (loadLanguage) {
            case "PT":
                bPT.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.colorButton));
                bPT.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                bEN.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.colorFadedButton));
                bEN.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                break;
            case "EN":
                bPT.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.colorFadedButton));
                bPT.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                bEN.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.colorButton));
                bEN.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                break;
        }
        bPT.setOnClickListener(v -> {
            if (!loadLanguage.equals("PT")) {
                Map<String, Object> info = new HashMap<>();
                info.put("language", "PT");
                DocumentRef = db.collection("users").document(mAuth.getUid());
                DocumentRef.update(info);
                registerFunctions.changeLanguage("PT");
                registerFunctions.writePreference("language", "PT");
                registerFunctions.createLog(registerFunctions.readPreference("language"));
                bPT.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.colorButton));
                bPT.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                bEN.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.colorFadedButton));
                bEN.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                recreate();
            }
        });
        bEN.setOnClickListener(v -> {
            if (!loadLanguage.equals("EN")) {
                Map<String, Object> info = new HashMap<>();
                info.put("language", "EN");
                DocumentRef = db.collection("users").document(mAuth.getUid());
                DocumentRef.update(info);
                registerFunctions.changeLanguage("EN");
                registerFunctions.writePreference("language", "EN");
                registerFunctions.createLog(registerFunctions.readPreference("language"));
                bPT.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.colorFadedButton));
                bPT.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                bEN.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.colorButton));
                bEN.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                recreate();
            }
        });

    }


    /**
     * Loads activity functions for the user first access
     *
     */
    private void loadFirstListener() {

        etFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (!etLastName.getText().toString().equals("")) {
                    bConfirm.setVisibility(View.VISIBLE);
                }
            }
        });
        etLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (!etFirstName.getText().toString().equals("")) {
                    bConfirm.setVisibility(View.VISIBLE);
                }
            }
        });
        bConfirm.setOnClickListener(v -> {
            if (registerFunctions.verifyFirstLastName(etFirstName.getText().toString(), etLastName.getText().toString())) {
                Map<String, Object> info = new HashMap<>();
                info.put("firstName", RegisterFunctions.capitalize(etFirstName.getText().toString()));
                info.put("lastName", RegisterFunctions.capitalize(etLastName.getText().toString()));
                DocumentRef = db.collection("users").document(mAuth.getUid());
                DocumentRef.update(info).addOnSuccessListener(aVoid -> {
                    registerFunctions.writePreference("firstName", RegisterFunctions.capitalize(etFirstName.getText().toString()));
                    registerFunctions.writePreference("lastName", RegisterFunctions.capitalize(etLastName.getText().toString()));
                    if (registerFunctions.readPreference("isStudent").equals("true")) {
                        Intent intent = new Intent(getApplicationContext(),  StudentActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(getApplicationContext(),  TeacherActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    registerFunctions.createLog("name");
                    Toast.makeText(getApplicationContext(), getString(R.string.registerActivityToastFirstConfirm) + " " + RegisterFunctions.capitalize(etLastName.getText().toString()) + ".", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), getString(R.string.registerActivityToastFirstConfirmFailed), Toast.LENGTH_SHORT).show());
            }
        });
        bReturn.setOnClickListener(v -> registerFunctions.signOut());

    }


    /**
     * Loads activity functions for the user accesses
     *
     */
    @SuppressLint("ClickableViewAccessibility")
    private void loadSecondListener() {

        etFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (!etFirstName.getText().toString().equals("") &&
                        !etLastName.getText().toString().equals("") &&
                        !etNewEmail.getText().toString().equals("")) {
                    if (!etFirstName.getText().toString().equals(registerFunctions.readPreference("firstName")) ||
                            !etLastName.getText().toString().equals(registerFunctions.readPreference("lastName")) ||
                            !etNewEmail.getText().toString().equals(registerFunctions.readPreference("email")) ||
                            !etNewPassword.getText().toString().equals("")) {
                        bConfirm.setVisibility(View.VISIBLE);
                        etPassword.setVisibility(View.VISIBLE);
                    } else {
                        bConfirm.setVisibility(View.INVISIBLE);
                        etPassword.setVisibility(View.INVISIBLE);
                    }
                } else {
                    bConfirm.setVisibility(View.INVISIBLE);
                    etPassword.setVisibility(View.INVISIBLE);
                }
                changeFirstName = !etFirstName.getText().toString().equals(registerFunctions.readPreference("firstName"));
            }
        });
        etLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (!etFirstName.getText().toString().equals("") &&
                        !etLastName.getText().toString().equals("") &&
                        !etNewEmail.getText().toString().equals("")) {
                    if (!etFirstName.getText().toString().equals(registerFunctions.readPreference("firstName")) ||
                            !etLastName.getText().toString().equals(registerFunctions.readPreference("lastName")) ||
                            !etNewEmail.getText().toString().equals(registerFunctions.readPreference("email")) ||
                            !etNewPassword.getText().toString().equals("")) {
                        bConfirm.setVisibility(View.VISIBLE);
                        etPassword.setVisibility(View.VISIBLE);
                    } else {
                        bConfirm.setVisibility(View.INVISIBLE);
                        etPassword.setVisibility(View.INVISIBLE);
                    }
                } else {
                    bConfirm.setVisibility(View.INVISIBLE);
                    etPassword.setVisibility(View.INVISIBLE);
                }
                changeLastName = !etLastName.getText().toString().equals(registerFunctions.readPreference("lastName"));
            }
        });
        etNewEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (!etFirstName.getText().toString().equals("") &&
                        !etLastName.getText().toString().equals("") &&
                        !etNewEmail.getText().toString().equals("")) {
                    if (!etFirstName.getText().toString().equals(registerFunctions.readPreference("firstName")) ||
                            !etLastName.getText().toString().equals(registerFunctions.readPreference("lastName")) ||
                            !etNewEmail.getText().toString().equals(registerFunctions.readPreference("email")) ||
                            !etNewPassword.getText().toString().equals("")) {
                        bConfirm.setVisibility(View.VISIBLE);
                        etPassword.setVisibility(View.VISIBLE);
                    } else {
                        bConfirm.setVisibility(View.INVISIBLE);
                        etPassword.setVisibility(View.INVISIBLE);
                    }
                } else {
                    bConfirm.setVisibility(View.INVISIBLE);
                    etPassword.setVisibility(View.INVISIBLE);
                }
                changeEmail = !etNewEmail.getText().toString().equals(registerFunctions.readPreference("email"));
            }
        });
        etNewPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (!etFirstName.getText().toString().equals("") &&
                        !etLastName.getText().toString().equals("") &&
                        !etNewEmail.getText().toString().equals("")) {
                    if (!etFirstName.getText().toString().equals(registerFunctions.readPreference("firstName")) ||
                            !etLastName.getText().toString().equals(registerFunctions.readPreference("lastName")) ||
                            !etNewEmail.getText().toString().equals(registerFunctions.readPreference("email")) ||
                            !etNewPassword.getText().toString().equals("")) {
                        bConfirm.setVisibility(View.VISIBLE);
                        etPassword.setVisibility(View.VISIBLE);
                    } else {
                        bConfirm.setVisibility(View.INVISIBLE);
                        etPassword.setVisibility(View.INVISIBLE);
                    }
                } else {
                    bConfirm.setVisibility(View.INVISIBLE);
                    etPassword.setVisibility(View.INVISIBLE);
                }
                if (!etNewPassword.getText().toString().equals("")) {
                    bView.setVisibility(View.VISIBLE);
                    changePassword = true;
                } else {
                    bView.setVisibility(View.INVISIBLE);
                    changePassword = false;
                }
            }
        });
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (!etPassword.getText().toString().equals("")) {
                    bView2.setVisibility(View.VISIBLE);
                } else {
                    bView2.setVisibility(View.INVISIBLE);
                }
            }
        });
        bView.setOnTouchListener((v, event) -> {
            if(event.getAction() == MotionEvent.ACTION_DOWN){
                etNewPassword.setTransformationMethod(null);
                bView.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.colorButton));
            }
            if(event.getAction() == MotionEvent.ACTION_UP){
                etNewPassword.setTransformationMethod(new PasswordTransformationMethod());
                bView.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.colorFadedButton));
            }
            return true;
        });
        bView2.setOnTouchListener((v, event) -> {
            if(event.getAction() == MotionEvent.ACTION_DOWN){
                etPassword.setTransformationMethod(null);
                bView2.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.colorButton));
            }
            if(event.getAction() == MotionEvent.ACTION_UP){
                etPassword.setTransformationMethod(new PasswordTransformationMethod());
                bView2.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.colorFadedButton));
            }
            return true;
        });
        bConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etPassword.getText().toString().equals("")) {
                    AuthCredential credential = EmailAuthProvider.getCredential(registerFunctions.readPreference("email"), etPassword.getText().toString());
                    mAuth.getCurrentUser().reauthenticate(credential).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            if ((changeFirstName || changeLastName) &&
                                    (registerFunctions.verifyFirstLastName(etFirstName.getText().toString(), etLastName.getText().toString()))) {
                                Map<String, Object> info = new HashMap<>();
                                info.put("firstName", RegisterFunctions.capitalize(etFirstName.getText().toString()));
                                info.put("lastName", RegisterFunctions.capitalize(etLastName.getText().toString()));
                                DocumentRef = db.collection("users").document(mAuth.getUid());
                                DocumentRef.update(info).addOnSuccessListener(aVoid -> {
                                    registerFunctions.writePreference("firstName", RegisterFunctions.capitalize(etFirstName.getText().toString()));
                                    registerFunctions.writePreference("lastName", RegisterFunctions.capitalize(etLastName.getText().toString()));
                                    registerFunctions.createLog("name");
                                    registerFunctions.createToast(getString(R.string.registerActivityToastSecondConfirm));
                                    if (!changeEmail && !changePassword) {
                                        if (registerFunctions.readPreference("isStudent").equals("true")) {
                                            Intent intent = new Intent(getApplicationContext(),  StudentActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Intent intent = new Intent(getApplicationContext(),  TeacherActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                }).addOnFailureListener(e -> registerFunctions.createToast(getString(R.string.registerActivityToastSecondConfirmFailed)));
                            }
                            if (changeEmail && registerFunctions.verifyEmail(etNewEmail.getText().toString())) {
                                mAuth.getCurrentUser().updateEmail(etNewEmail.getText().toString()).addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        Map<String, Object> info = new HashMap<>();
                                        info.put("email", etNewEmail.getText().toString());
                                        DocumentRef = db.collection("users").document(mAuth.getUid());
                                        DocumentRef.update(info).addOnSuccessListener(aVoid -> {
                                            registerFunctions.writePreference("email", etNewEmail.getText().toString());
                                            registerFunctions.createLog("email");
                                            registerFunctions.createToast(getString(R.string.registerActivityToastSecondConfirmEmail));
                                            if (changePassword && registerFunctions.verifyPassword(etNewPassword.getText().toString())) {
                                                mAuth.getCurrentUser().updatePassword(etNewPassword.getText().toString()).addOnCompleteListener(task11 -> {
                                                    if (task11.isSuccessful()) {
                                                        registerFunctions.createLog("password");
                                                        registerFunctions.createToast(getString(R.string.registerActivityToastSecondConfirmPassword));
                                                        if (registerFunctions.readPreference("isStudent").equals("true")) {
                                                            Intent intent = new Intent(getApplicationContext(),  StudentActivity.class);
                                                            startActivity(intent);
                                                            finish();
                                                        } else {
                                                            Intent intent = new Intent(getApplicationContext(),  TeacherActivity.class);
                                                            startActivity(intent);
                                                            finish();
                                                        }
                                                    } else {
                                                        registerFunctions.createToast(getString(R.string.registerActivityToastSecondConfirmPasswordFailed));
                                                    }
                                                });
                                            } else {
                                                if (registerFunctions.readPreference("isStudent").equals("true")) {
                                                    Intent intent = new Intent(getApplicationContext(),  StudentActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                } else {
                                                    Intent intent = new Intent(getApplicationContext(),  TeacherActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }
                                        }).addOnFailureListener(e -> registerFunctions.createToast(getString(R.string.registerActivityToastSecondConfirmEmailFailed)));
                                    } else {
                                        registerFunctions.createToast(getString(R.string.registerActivityToastSecondConfirmEmailFailed2));
                                    }
                                });
                            }
                            if (changePassword && !changeEmail && registerFunctions.verifyPassword(etNewPassword.getText().toString())) {
                                mAuth.getCurrentUser().updatePassword(etNewPassword.getText().toString()).addOnCompleteListener(task12 -> {
                                    if (task12.isSuccessful()) {
                                        registerFunctions.createLog("password");
                                        registerFunctions.createToast(getString(R.string.registerActivityToastSecondConfirmPassword));
                                        if (registerFunctions.readPreference("isStudent").equals("true")) {
                                            Intent intent = new Intent(getApplicationContext(),  StudentActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Intent intent = new Intent(getApplicationContext(),  TeacherActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    } else {
                                        registerFunctions.createToast(getString(R.string.registerActivityToastSecondConfirmPasswordFailed));
                                    }
                                });
                            }
                        } else {
                            registerFunctions.createToast(getString(R.string.registerActivityToastSecondConfirmAuthentication));
                        }
                    }).addOnFailureListener(e -> registerFunctions.createToast(getString(R.string.registerActivityToastSecondConfirmAuthentication)));
                } else {
                    registerFunctions.createToast(getString(R.string.registerActivityToastSecondConfirmAuthenticationFailed));
                }
            }
        });
        bReturn.setOnClickListener(v -> {
            if (registerFunctions.readPreference("isStudent").equals("true")) {
                Intent intent = new Intent(getApplicationContext(),  StudentActivity.class);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(getApplicationContext(),  TeacherActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }


}
