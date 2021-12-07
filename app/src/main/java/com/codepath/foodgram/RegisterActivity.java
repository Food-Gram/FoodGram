package com.codepath.foodgram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class RegisterActivity extends AppCompatActivity {

    public static final String TAG = "RegisterActivity";
    private EditText etUsername;
    private EditText etPassword;
    private EditText etEmail;
    private EditText etPhone;
    private EditText etAddress;
    private Button btnSignUp;
    private TextView tvUsernameCheck;
    private TextView tvPasswordCheck;
    private TextView tvEmailCheck;
    private TextView tvMsg;
    private TextView tvPhoneCheck;
    private TextView tvAddressCheck;
    private RadioGroup Utype;

    String type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        etUsername = findViewById(R.id.etUsername_SignUp);
        etPassword = findViewById(R.id.etPassword_SignUp);
        etEmail = findViewById(R.id.etEmail_SignUp);
        etPhone = findViewById(R.id.etPhone_SignUp);
        etAddress = findViewById(R.id.etAddress_SignUp);
        btnSignUp = findViewById(R.id.btnSignUp_SignUp);
        tvUsernameCheck = findViewById(R.id.tvUsernameCheck);
        tvPasswordCheck = findViewById(R.id.tvPassworkCheck);
        tvEmailCheck = findViewById(R.id.tvEmailCheck);
        tvPhoneCheck = findViewById(R.id.tvPhoneCheck);
        tvAddressCheck = findViewById(R.id.tvAddressCheck);
        Utype = findViewById(R.id.UserType);

        tvMsg= findViewById(R.id.tvMsg);;

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int checkedId = Utype.getCheckedRadioButtonId();
                //no button is checked
                if (checkedId == -1) {
                    Toast.makeText(RegisterActivity.this, "You must choose a sign up type", Toast.LENGTH_SHORT).show();
                    tvMsg.setVisibility(TextView.VISIBLE);
                    tvMsg.setText("You must choose a sign up type");
                    tvMsg.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.medium_red));
                } else {
                    //get sign up type
                    findRadioButton(checkedId);

                    tvUsernameCheck.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray));
                    tvPasswordCheck.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray));
                    tvEmailCheck.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray));
                    tvPhoneCheck.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray));
                    tvAddressCheck.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray));
                    String username = etUsername.getText().toString();
                    String password = etPassword.getText().toString();
                    String email = etEmail.getText().toString();
                    String phone = etPhone.getText().toString();
                    String address = etAddress.getText().toString();

                    if (username.isEmpty()) {
                        Toast.makeText(RegisterActivity.this, "Username cannot be empty", Toast.LENGTH_SHORT).show();
                        tvUsernameCheck.setText("Username cannot be empty");
                        tvUsernameCheck.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.medium_red));
                        return;
                    }
                    if (password.isEmpty()) {
                        Toast.makeText(RegisterActivity.this, "Password cannot be empty", Toast.LENGTH_SHORT).show();
                        tvPasswordCheck.setText("Password cannot be empty");
                        tvPasswordCheck.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.medium_red));
                        return;
                    }
                    if (email.isEmpty()) {
                        Toast.makeText(RegisterActivity.this, "Email cannot be empty", Toast.LENGTH_SHORT).show();
                        tvEmailCheck.setText("Email cannot be empty");
                        tvEmailCheck.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.medium_red));
                        return;
                    }
                    if (type.equals("FoodStore") && phone.isEmpty()){
                        Toast.makeText(RegisterActivity.this, "Phone number cannot be empty", Toast.LENGTH_SHORT).show();
                        tvPhoneCheck.setText("Phone Number cannot be empty");
                        tvPhoneCheck.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.medium_red));
                        return;
                    }
                    if (type.equals("FoodStore") && address.isEmpty()){
                        Toast.makeText(RegisterActivity.this, "Store Address cannot be empty", Toast.LENGTH_SHORT).show();
                        tvAddressCheck.setText("Store Address cannot be empty");
                        tvAddressCheck.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.medium_red));
                        return;
                    }
                    createNewUser(username, password, email, phone, address);
                }
            }
        });


    }

    private void createNewUser(String username, String password, String email, String phone, String address) {
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.put("phoneNum", phone);
        user.put("storeAddress", address);
        user.put("type",type);
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if( e != null){
                    Log.e(TAG, "Issue with Signing up", e);
                    tvMsg.setText(e.getMessage());
                    tvMsg.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.medium_red));
                    return;
                }
                Log.i(TAG, "New user save was successful!!");

                tvMsg.setText("Sign up successful! Go to Log in page ->");
                tvMsg.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.purple_200));
                tvMsg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        goLoginActivity();
                    }
                });
            }
        });
    }

    private void goLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        ParseUser.logOut();

    }



    private void findRadioButton(int checkedId) {
        switch(checkedId) {
            case R.id.rbUser:
                type = "user";
                break;
            case R.id.rbStore:
                type = "FoodStore";
                break;
        }
    }
}