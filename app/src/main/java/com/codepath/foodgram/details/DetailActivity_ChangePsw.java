package com.codepath.foodgram.details;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.foodgram.MainActivity;
import com.codepath.foodgram.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class DetailActivity_ChangePsw extends AppCompatActivity {

    public static final String TAG = "ChangePassword";
    private EditText newPsw1;
    private EditText newPsw2;
    private TextView msg1;
    private TextView msg2;
    private Button update;
    private TextView info;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_change_psw);
        newPsw1 = findViewById(R.id.Password1);
        newPsw2 = findViewById(R.id.Password2);
        msg1 = findViewById(R.id.tvMsg1);
        msg2 = findViewById(R.id.tvMsg2);
        update = findViewById(R.id.bnUpdatepsw);
        info = findViewById(R.id.SuccessfulInfo);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                msg1.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray));
                msg2.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray));
                String oldpsw = newPsw1.getText().toString();
                String newpsw = newPsw2.getText().toString();
                ParseUser user = ParseUser.getCurrentUser();
                if (oldpsw.isEmpty()) {
                    Toast.makeText(DetailActivity_ChangePsw.this, "You did not enter a password", Toast.LENGTH_SHORT).show();
                    msg1.setText("Password cannot be empty");
                    msg1.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.medium_red));
                    return;
                }
                if (newpsw.isEmpty()) {
                    Toast.makeText(DetailActivity_ChangePsw.this, "You did not enter a password", Toast.LENGTH_SHORT).show();
                    msg2.setText("Password cannot be empty");
                    msg2.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.medium_red));
                    return;
                }
                if (!oldpsw.equals(newpsw)) {
                    Toast.makeText(DetailActivity_ChangePsw.this, "Password not match", Toast.LENGTH_SHORT).show();
                    msg2.setText("Password you entered is not match");
                    msg2.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.medium_red));
                    return;
                }

                user.setPassword(newpsw);
                user.saveInBackground(new SaveCallback() {
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
