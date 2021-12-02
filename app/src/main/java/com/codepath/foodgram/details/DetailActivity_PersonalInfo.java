package com.codepath.foodgram.details;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.foodgram.MainActivity;
import com.codepath.foodgram.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class DetailActivity_PersonalInfo extends AppCompatActivity {

    public static final String TAG = "ChangeInformation";
    private ImageView icon;
    private TextView username;
    private EditText email;
    private EditText phone;
    private EditText address;
    private TextView addressTitle;
    private Button update;
    private TextView msg;
    private TextView info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_personal_info);
        icon = findViewById(R.id.Icon);
        username = findViewById(R.id.Username);
        email = findViewById(R.id.Email);
        phone = findViewById(R.id.Phone);
        address = findViewById(R.id.Address);
        addressTitle = findViewById(R.id.Address1);
        update = findViewById(R.id.update);
        msg = findViewById(R.id.msg);
        info = findViewById(R.id.SuccessfulInfo);

        // Basic information of current user
        ParseUser currentUser = ParseUser.getCurrentUser();
        username.setText(currentUser.getUsername());

        String useremail = currentUser.getEmail();
        if (useremail != null) {
            email.setText(useremail);
        }

        String phoneNum = currentUser.getString("phoneNum");
        if (phoneNum != null) {
            phone.setText(phoneNum);
        }

        Glide.with(this).load(currentUser.getParseFile("icon").getUrl())
                .transform(new CenterInside(), new RoundedCorners(100)).into(icon);

        if (currentUser.getString("type").equals("FoodStore")) {
            address.setVisibility(TextView.VISIBLE);
            addressTitle.setVisibility(TextView.VISIBLE);
            address.setText(currentUser.getString("storeAddress"));

        }

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Uemail = email.getText().toString();
                String Uphone = phone.getText().toString();

                if (currentUser.getString("type").equals("FoodStore")) {
                    String Uaddress = address.getText().toString();
                    if (Uaddress.isEmpty()) {
                        Toast.makeText(DetailActivity_PersonalInfo.this, "Address cannot be empty", Toast.LENGTH_SHORT).show();
                        msg.setVisibility(TextView.VISIBLE);
                        msg.setText("Address cannot be empty");
                        msg.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.medium_red));
                        return;
                    }
                    if (Uphone.isEmpty()) {
                        Toast.makeText(DetailActivity_PersonalInfo.this, "Phone Number cannot be empty", Toast.LENGTH_SHORT).show();
                        msg.setVisibility(TextView.VISIBLE);
                        msg.setText("Phone Number cannot be empty");
                        msg.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.medium_red));
                        return;
                    }
                }

                currentUser.setEmail(Uemail);
                currentUser.put("phoneNum", Uphone);
                currentUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            Log.e(TAG, "Error while saving", e);
                        }
                        Log.i(TAG, "password changed successful!!");
                        info.setVisibility(TextView.VISIBLE);
                        info.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.medium_red));
                        info.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                GoMain();
                            }
                        });
                    }
                });
            }
        });
    }
    private void GoMain() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}