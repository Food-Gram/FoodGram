package com.codepath.foodgram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class RegisterActivity extends AppCompatActivity {

    public static final String TAG = "RegisterActivity";
    private EditText createUsername;
    private EditText createPassword;
    private EditText etAddress;
    private EditText etPhone;
    private Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //if (ParseUser.getCurrentUser() != null)
          //  goMainActivity();

        createUsername = findViewById(R.id.createUsername);
        createPassword = findViewById(R.id.createPassword);
        etAddress = findViewById(R.id.etAddress);
        etPhone = findViewById(R.id.etPhone);
        btnSignUp = findViewById(R.id.btnSignUp);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = createUsername.getText().toString();
                String password = createPassword.getText().toString();
                String address = etAddress.getText().toString();
                String phone = etPhone.getText().toString();

                // if the phone and address are empty then it is a regular user
                if (phone.isEmpty() && address.isEmpty()) {
                    try {
                        signupUser(username, password);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else signupFoodStore(username, password, address, phone);
            }
        });
    }

    // regular users
    private void signupUser(String username, String password) throws ParseException {
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        user.signUp();
        goMainActivity();
    }

    // food store users when address and phone are filled
    private void signupFoodStore(String username, String password, String address, String phone) {
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        // user.setaddress
        // user.setphone

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with registration", e);
                    Toast.makeText(RegisterActivity.this, "Issue with registration", Toast.LENGTH_LONG).show();
                    return;
                }
                goMainActivity();
            }
        });
    }

    private void goMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}