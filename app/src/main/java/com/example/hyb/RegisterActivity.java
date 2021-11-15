package com.example.hyb;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.hyb.Model.UserInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import org.apache.commons.validator.routines.EmailValidator;

public class RegisterActivity extends AppCompatActivity {

    private static String passwordValidationError;
    private final static String emailValidationError = "Email Prefix Or Email Domain Is Not Valid!";
    private final static String missingFirstOrsLastName= "Please Enter Your First And Last Name!";
    private final static String missingPhoneNumber= "Please Enter 8 Digits Phone Number!";

    private final String TAG = "RegisterActivity";
    public static final String KEY_NAME = "UserInfo";
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            //TODO: RELOAD UI;
            Log.w(TAG, ": User already logged in");
        }
    }

    public void onClick(View arg0) {

        boolean invalidPasswordDefault = false;
        boolean invalidPassword;
        EditText emailInput = findViewById(R.id.editTextEmailRegister);
        EditText passwordInput = findViewById(R.id.editTextPasswordRegister);
        EditText firstNameInput = findViewById(R.id.editTextFirstName);
        EditText lastNameInput = findViewById(R.id.editTextLastName);
        EditText phonenumberInput = findViewById(R.id.editTextPhone);

        String txtEmail = emailInput.getText().toString();
        String txtPassword = passwordInput.getText().toString();
        String txtFirstName = firstNameInput.getText().toString();
        String txtLastName = lastNameInput.getText().toString();
        int phoneNumber = Integer.parseInt(phonenumberInput.getText().toString());
        String phoneNumber2 = Integer.toString(phoneNumber);

        // is password valid?
        invalidPassword = isInvalidPassword(txtPassword,invalidPasswordDefault);

        /*check if :
          1.first name and last name fields are empty
          2. phone number is less than 8 digits
          3. email is valid using Apache Commons Validator
          4.Password is valid : Contain 8-15 characters//contain at least one upper and lowercase//contain special characters among @#$% */

        if (txtFirstName.isEmpty() && txtLastName.isEmpty()){
            Toast.makeText(arg0.getContext(), missingFirstOrsLastName, Toast.LENGTH_SHORT).show();

        }
        else if(phoneNumber2.length() < 8){
            Toast.makeText(arg0.getContext(), missingPhoneNumber, Toast.LENGTH_SHORT).show();

        }
        else if (invalidPassword == true) {
            Toast.makeText(arg0.getContext(), passwordValidationError, Toast.LENGTH_SHORT).show();
        }

        else if(isValidEmail(txtEmail) == false){
            Toast.makeText(arg0.getContext(), emailValidationError, Toast.LENGTH_SHORT).show();

        }


        else if (invalidPassword == false ){
            mAuth.createUserWithEmailAndPassword(txtEmail, txtPassword)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            assert user != null;
                            UserInfo userInfo = new UserInfo(user.getUid(), txtFirstName, txtLastName, phoneNumber, null, null);
                            db.collection("users").document(user.getUid()).set(userInfo); //Legger til bruker med bruker info til en ny samling

                            //Ny bruker har ingen rom dem er medlemmer i så dem blir sendt videre til Join/Create
                            navigateToJoinCreateRoom(userInfo.getUid());

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });

        }
    }

    // Function for password validation
    public  boolean isInvalidPassword(String txtPassword, boolean invalidPassword) {

        String upperCaseChars = "(.*[A-Z].*)";
        String lowerCaseChars = "(.*[a-z].*)";
        String numbers = "(.*[0-9].*)";
        String specialChars = "(.*[@,#$%].*$)";
        if (txtPassword.length() > 15 || txtPassword.length() < 8) {
            passwordValidationError = "Password must be less than 15 and more than 8 characters in length.";
            invalidPassword = true;
            return invalidPassword ;
        }
        else if(!txtPassword.matches(upperCaseChars)) {
            passwordValidationError = "Password must have at least one uppercase character";
            invalidPassword = true;
            return invalidPassword ;
        }
        else if (!txtPassword.matches(lowerCaseChars)) {
            passwordValidationError = "Password must have at least one lowercase character";
            invalidPassword = true;
            return invalidPassword ;
        }
        else if (!txtPassword.matches(numbers)) {
            passwordValidationError = "Password must have at least one number";
            invalidPassword = true;
            return invalidPassword ;
        }
        else if (!txtPassword.matches(specialChars)) {
            passwordValidationError = "Password must have at least one special character among @#$%";
            invalidPassword = true;
            return invalidPassword ;
        }

        return invalidPassword;
    }


    // function for email validation
    public static boolean isValidEmail(String email) {
        //EmailValidator instance
        EmailValidator validator = EmailValidator.getInstance();
        // check email using isValid method
        return validator.isValid(email);
    }


    /**
     * Navigerer bruker til neste skjerm
     */
    private void navigateToJoinCreateRoom(String userId) {
        Intent intent = new Intent(this, LoginRegisterRoomActivity.class);
        intent.putExtra(KEY_NAME, userId);
        startActivity(intent);
    }


}
