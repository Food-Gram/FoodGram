package com.codepath.foodgram.details;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.foodgram.R;
import com.codepath.foodgram.models.Post;
import com.codepath.foodgram.models.RateStore;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity_Rating extends AppCompatActivity {

    public static final String TAG = "Rating";
    private RadioButton r1;
    private RadioButton r2;
    private RadioButton r3;
    private RadioButton r4;
    private RadioButton r5;
    private RadioGroup rating;
    private TextView errorMsg;
    private TextView submitMsg;
    private int rate;
    private ParseUser store;

    List<RateStore> rateObj;

    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_rating);
        r1 = findViewById(R.id.rb1);
        r2 = findViewById(R.id.rb2);
        r3 = findViewById(R.id.rb3);
        r4 = findViewById(R.id.rb4);
        r5 = findViewById(R.id.rb5);
        submit = findViewById(R.id.bnRatingSubmit);
        rating = findViewById(R.id.ratingGroup);
        errorMsg = findViewById(R.id.tvRatingMsg);
        submitMsg = findViewById(R.id.tvSubmitMsg);

        rateObj = new ArrayList<>();
        store = Parcels.unwrap(getIntent().getParcelableExtra("store"));

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int checkedId = rating.getCheckedRadioButtonId();
                //no button is checked
                if (checkedId == -1) {
                    Toast.makeText(DetailActivity_Rating.this, "You did not give a rate", Toast.LENGTH_SHORT).show();
                    errorMsg.setVisibility(TextView.VISIBLE);
                }
                else {
                    //get rating
                    findRadioButton(checkedId);
                    //update rating or create a new rating
                    queryRate();

                }
            }
        });
    }

    private void queryRate() {
        ParseQuery<RateStore> query = ParseQuery.getQuery(RateStore.class);
        query.whereEqualTo(RateStore.KEY_USER,ParseUser.getCurrentUser());
        query.whereEqualTo(RateStore.KEY_STORE,store);
        query.findInBackground(new FindCallback<RateStore>() {
            @Override
            public void done(List<RateStore> rates, ParseException e) {
                if(e != null){
                    Log.e(TAG, "Issue with getting rating", e);
                    return;
                }

                if(rates.size()>0){
                    //when user rated before
                    for(RateStore rating: rates) {
                        Log.i(TAG, "Rating:" + rating.getRate());
                        rating.setRate(rate);
                        rating.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e != null) {
                                    Log.e(TAG, "Error while saving", e);
                                }
                                Log.i(TAG, "Rating saved!!");
                                Toast.makeText(DetailActivity_Rating.this, "Rating Submitted", Toast.LENGTH_SHORT).show();
                                submitMsg.setVisibility(TextView.VISIBLE);
                                submitMsg.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        GoProfile();
                                    }
                                });
                            }
                        });
                    }
                }
                else{
                    // if user rating for the first time
                    RateStore newRate = new RateStore();
                    newRate.put("username", ParseUser.getCurrentUser());
                    newRate.put("FoodStore", store);
                    newRate.put("rating", rate);
                    newRate.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e != null) {
                                Log.e(TAG, "Error while saving", e);
                            }
                            Log.i(TAG, "Rating saved!!");
                            Toast.makeText(DetailActivity_Rating.this, "Rating Submitted", Toast.LENGTH_SHORT).show();
                            submitMsg.setVisibility(TextView.VISIBLE);
                            submitMsg.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    GoProfile();

                                }
                            });
                        }
                    });
                }
            }
        });
    }



    private void GoProfile(){
        Intent i = new Intent(this,DetailActivity_OtherStoreProd.class);
        i.putExtra("user", Parcels.wrap(store));
        startActivity(i);
    }

    private void findRadioButton(int checkedId) {
        switch(checkedId){
            case R.id.rb1:
                rate = 1;
                break;
            case R.id.rb2:
                rate = 2;
                break;
            case R.id.rb3:
                rate = 3;
                break;
            case R.id.rb4:
                rate = 4;
                break;
            case R.id.rb5:
                rate = 5;
                break;
        }
    }
}