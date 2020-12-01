package com.example.gymbox;


// Importing general functions
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;


// Importing Firebase functions
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QueryDocumentSnapshot;


/**
 * Functions related with writing, reading and validating information
 */
public class RegisterFunctions extends AppCompatActivity {


    // Defining internal variables
    private List<String> PINs;
    public String PIN;
    private Integer randomPIN;
    Context mContext;


    // Defining Firebase variables
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();


    public RegisterFunctions(Context context) {
        this.mContext = context;
    }


    /**
     * Register SharedPreferences and forwards the user to the correct activity
     *
     */
    public void enter() {

        String uID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        db.collection("users").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    if (document.getId().equals(uID)) {
                        writePreference("PIN", document.getString("PIN"));
                        writePreference("isStudent", Objects.requireNonNull(document.getBoolean("isStudent")).toString());
                        writePreference("email", document.getString("email"));
                        writePreference("accesses", Objects.requireNonNull(document.getLong("accesses")).toString());
                        writePreference("firstName", document.getString("firstName"));
                        writePreference("lastName", document.getString("lastName"));
                        writePreference("language", document.getString("language"));
                        changeLanguage(readPreference("language"));
                        if (Objects.requireNonNull(document.getBoolean("isStudent"))) {
                            if (Objects.equals(document.getString("firstName"), "") || TextUtils.isEmpty(document.getString("firstName"))) {
                                Intent intent = new Intent(mContext,  RegisterActivity.class);
                                mContext.startActivity(intent);
                                finish();
                            } else {
                                Intent intent = new Intent(mContext,  StudentActivity.class);
                                mContext.startActivity(intent);
                                finish();
                            }
                        } else {
                            if (Objects.equals(document.getString("firstName"), "") || TextUtils.isEmpty(document.getString("firstName"))) {
                                Intent intent = new Intent(mContext,  RegisterActivity.class);
                                mContext.startActivity(intent);
                                finish();
                            } else {
                                Intent intent = new Intent(mContext,  TeacherActivity.class);
                                mContext.startActivity(intent);
                                finish();
                            }
                        }
                        if (!readPreference("firstName").equals("") && !TextUtils.isEmpty(document.getString("firstName"))) {
                            createToast(mContext.getString(R.string.enterToast) + " " + readPreference("firstName") + "!");
                        } else {
                            createToast(mContext.getString(R.string.enterToastAlternative));
                        }
                    }
                }
            }
        });

    }


    /**
     * Sign in if email address and password are valid
     *
     * @param emailAddress email address to validate
     * @param password password to validate
     */
    public void signIn(String emailAddress, String password) {

        if (verifyEmail(emailAddress)) {
            if (!password.equals("") && !TextUtils.isEmpty(password)) {
                mAuth.signInWithEmailAndPassword(emailAddress, password).addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        createLog("SignIn");
                        createToast(mContext.getString(R.string.loginToast));
                        enter();
                    } else {
                        createToast(mContext.getString(R.string.loginToastFailed));
                    }
                });
            } else {
                createToast(mContext.getString(R.string.verifyPasswordToastPasswordFailed));
            }
        }

    }


    /**
     * Sign out
     *
     */
    public void signOut() {

        createLog("SignOut");
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(mContext,  LoginActivity.class);
        mContext.startActivity(intent);
        finish();
    }


    /**
     * Return if email address is valid
     *
     * @param emailAddress email address to validate
     * @return boolean
     */
    public boolean verifyEmail(String emailAddress) {

        if (emailAddress.equals("") || TextUtils.isEmpty(emailAddress)) {
            createToast(mContext.getString(R.string.verifyEmailToastEmailFailed));
            return false;
        } else {
            if (emailAddress.trim().matches("^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$")) {
                return true;
            } else {
                createToast(mContext.getString(R.string.verifyEmailToastEmailInvalid));
                return false;
            }
        }

    }


    /**
     * Return if password is valid
     *
     * @param password password to validate
     * @return boolean
     */
    public boolean verifyPassword(String password) {

        if (password.equals("") || TextUtils.isEmpty(password)) {
            createToast(mContext.getString(R.string.verifyPasswordToastPasswordFailed));
            return false;
        } else {
            if (password.trim().matches("^(?=.*[0-9]).+$")) {
                if (password.trim().matches("^(?=.*[a-zA-Z]).+$")) {
                    if (password.trim().matches("^(?=.*[0-9])(?=.*[a-zA-Z]).{6,}$")) {
                        return true;
                    } else {
                        createToast(mContext.getString(R.string.verifyPasswordToastPasswordCharFailed));
                        return false;
                    }
                } else {
                    createToast(mContext.getString(R.string.verifyPasswordToastPasswordLetterFailed));
                    return false;
                }
            } else {
                createToast(mContext.getString(R.string.verifyPasswordToastPasswordNumberFailed));
                return false;
            }
        }

    }


    /**
     * Return if first name and last name are valid
     *
     * @param firstName first name to validate
     * @param lastName last name to validate
     * @return boolean
     */
    public boolean verifyFirstLastName(String firstName, String lastName) {

        if (firstName.equals("")) {
            createToast(mContext.getString(R.string.verifyFirstLastNameEmptyFirstName));
            return false;
        } else if (firstName.length() > 24) {
            createToast(mContext.getString(R.string.verifyFirstLastNameLongFirstName));
            return false;
        } else if (firstName.trim().matches("^([A-Za-z]+([ ]?[a-z]?['-]?[A-Z][a-z]+)*)$")) {
            if (lastName.equals("")) {
                createToast(mContext.getString(R.string.verifyFirstLastNameEmptyLastName));
                return false;
            } else if (lastName.length() > 24) {
                createToast(mContext.getString(R.string.verifyFirstLastNameLongLastName));
                return false;
            } else if (lastName.trim().matches("^([A-Za-z]+([ ]?[a-z]?['-]?[A-Z][a-z]+)*)$")) {
                return true;
            } else {
                createToast(mContext.getString(R.string.verifyFirstLastNameInvalidLastName));
                return false;
            }
        } else {
            createToast(mContext.getString(R.string.verifyFirstLastNameInvalidFirstName));
            return false;
        }

    }


    /**
     * Return if PIN is valid
     *
     * @param PIN PIN to validate
     * @return boolean
     */
    public boolean verifyPIN(String PIN) {

        if (PIN.equals("") || TextUtils.isEmpty(PIN)) {
            createToast(mContext.getString(R.string.verifyPINToastPINFailed));
            return false;
        } else {
            if (PIN.trim().matches("^\\d+$")) {
                if (PIN.length() == 6) {
                    return true;
                } else {
                    createToast(mContext.getString(R.string.verifyPINToastPINLengthFailed));
                    return false;
                }
            } else {
                createToast(mContext.getString(R.string.verifyPINToastPINCharFailed));
                return false;
            }
        }

    }


    /**
     * Create a log on logs collection
     *
     * @param log log to be created
     */
    public void createLog(String log){

        Map<String, Object> info = new HashMap<>();
        info.put("log", log);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime now = LocalDateTime.now();
        DocumentReference documentRef = db.collection("logs").document(Objects.requireNonNull(mAuth.getUid())).collection("logs").document(dtf.format(now));
        documentRef.set(info);

    }


    /**
     * Verify email address and password, then register the user if valid
     *
     * @param emailAddress email address to validate and register
     * @param password password to validate and register
     */
    public void registerUser(final String emailAddress, String password) {
        if (verifyEmail(emailAddress) && verifyPassword(password)) {
            mAuth.createUserWithEmailAndPassword(emailAddress, password).addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {
                    registerData(Objects.requireNonNull(mAuth.getCurrentUser()).getUid(), emailAddress);
                } else {
                    createToast(mContext.getString(R.string.registerUserFailed));
                }
            });
        }

    }


    /**
     * Generate PIN and register more user data
     *
     * @param uID unique user ID
     * @param emailAddress email address to link
     */
    private void registerData(final String uID, final String emailAddress) {

        PIN = "000000";
        PINs = new ArrayList<>();
        DocumentReference DocumentRef = db.collection("users").document(uID);
        db.collection("pins").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    PINs.add(document.getId());
                }
                while (PINs.contains(PIN)) {
                    randomPIN = ThreadLocalRandom.current().nextInt(100000, 1000000);
                    PIN = randomPIN.toString();
                }
                Map<String, Object> info = new HashMap<>();
                info.put("PIN", PIN);
                info.put("isStudent", true);
                info.put("email", emailAddress);
                info.put("accesses", 0);
                info.put("firstName", "");
                info.put("lastName", "");
                info.put("language", "EN");
                DocumentRef.set(info);
                registerPIN(PIN);
            } else {
                createToast(mContext.getString(R.string.registerUserFailed));
            }
        });

    }


    /**
     * Register PIN
     *
     * @param PIN PIN to register
     */
    private void registerPIN(String PIN) {

        Map<String, Object> info = new HashMap<>();
        info.put("uID", Objects.requireNonNull(mAuth.getCurrentUser()).getUid());
        DocumentReference DocumentRef = db.collection("pins").document(PIN);
        DocumentRef.set(info).addOnSuccessListener(aVoid -> {
            createLog("Register");
            createToast(mContext.getString(R.string.registerUser));
            enter();
        }).addOnFailureListener(e -> createToast(mContext.getString(R.string.registerUserFailed)));

    }


    /**
     * Verify email address, then send reset mail
     *
     * @param emailAddress email address to validate and reset
     */
    public void sendResetMail(final String emailAddress) {

        if (verifyEmail(emailAddress)) {
            mAuth.sendPasswordResetEmail(emailAddress).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    createLog("Email");
                    createToast(mContext.getString(R.string.sendResetMail));
                } else {
                    createToast(mContext.getString(R.string.sendResetMailFailed));
                }
            });
        }

    }


    /**
     * Write key and value on SharedPreferences
     *
     * @param key SharedPreferences key to write
     * @param value SharedPreferences value to write
     */
    public void writePreference(String key, String value){

        SharedPreferences.Editor editor = mContext.getSharedPreferences("Gymbox", MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.apply();

    }


    /**
     * Change default language
     *
     * @param language language to be set as default
     */
    public void changeLanguage(String language){

        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.setLocale(locale);
        mContext.getResources().updateConfiguration(configuration, mContext.getResources().getDisplayMetrics());

    }


    /**
     * Read SharedPreferences key-value
     *
     * @param key SharedPreferences key to read
     */
    public String readPreference(String key) {

        SharedPreferences sharedPreferences = mContext.getSharedPreferences("Gymbox", MODE_PRIVATE);
        return sharedPreferences.getString(key,"");

    }


    /**
     * Verify user remains connected
     *
     * @return boolean
     */
    public boolean verifyConnection() {

        FirebaseUser currentUser = mAuth.getCurrentUser();
        return currentUser != null;

    }


    /**
     * Capitalize word
     *
     * @param word word to capitalize
     * @return String
     */
    public static String capitalize(String word) {

        if (word == null || word.isEmpty()) {
            return word;
        }
        return word.substring(0, 1).toUpperCase() + word.substring(1);

    }


    /**
     * Create a toast with message
     *
     * @param message toast message
     */
    public void createToast(String message) {

        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(mContext, message, duration);
        toast.show();

    }


    /**
     * Redeem PIN
     *
     * @param PIN PIN to redeem
     */
    public void redeem(String PIN) {
        if (verifyPIN(PIN)) {
            db.collection("pins").get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                        if (document.getId().equals(PIN)) {
                            String uID = document.getString("uID");
                            db.collection("users").get().addOnCompleteListener(task2 -> {
                                if (task2.isSuccessful()) {
                                    for (QueryDocumentSnapshot document2 : Objects.requireNonNull(task2.getResult())) {
                                        if (document2.getId().equals(uID)) {
                                            int accesses = Math.toIntExact(document2.getLong("accesses"));
                                            if (accesses > 0) {
                                                createToast(mContext.getString(R.string.redeemToastAccesses) + " " + document2.getString("firstName") + "!");
                                                DocumentReference DocumentRef = db.collection("users").document(uID);
                                                Map<String, Object> info = new HashMap<>();
                                                info.put("accesses", accesses - 1);
                                                DocumentRef.update(info);
                                                Map<String, Object> info2 = new HashMap<>();
                                                info2.put("log", "redeem");
                                                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
                                                LocalDateTime now = LocalDateTime.now();
                                                DocumentReference documentRef = db.collection("logs").document(uID).collection("logs").document(dtf.format(now));
                                                documentRef.set(info2);
                                                // Release access
                                            } else {
                                                createToast(mContext.getString(R.string.redeemToastAccessesFailed));
                                            }
                                        }
                                    }
                                }
                            });
                        }
                    }
                }
            });

        }

    }


}
