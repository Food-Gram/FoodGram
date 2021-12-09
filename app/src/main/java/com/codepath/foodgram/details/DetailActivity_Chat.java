package com.codepath.foodgram.details;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.foodgram.R;
import com.codepath.foodgram.adapters.MessageListAdapter;
import com.codepath.foodgram.models.ChatMsg;
import com.codepath.foodgram.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DetailActivity_Chat extends AppCompatActivity {

    public static final String TAG = "DetailActivity_Chat";
    private RecyclerView rvMsgList;
    private MessageListAdapter adapter;
    private List<ChatMsg> allMsg;

    private TextView tvMsgInput;
    private Button btnSend;      //send button
    private ParseUser user;

    private TextView tvUsernameInChat;
    private ImageView imageViewChat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_chat);

        // Basic information of select user
        user = Parcels.unwrap(getIntent().getParcelableExtra("user"));

        tvUsernameInChat = findViewById(R.id.tvUsernameInChat);
        tvUsernameInChat.setText(user.getUsername());

        imageViewChat = findViewById(R.id.imageViewChat);


        rvMsgList= findViewById(R.id.rvCharList);
        tvMsgInput = findViewById(R.id.tvMessage);
        btnSend = findViewById(R.id.btnSendMsg);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!tvMsgInput.getText().toString().isEmpty()){
                    Log.i(TAG, tvMsgInput.getText().toString());
                    SendMessage(tvMsgInput.getText().toString());
                    tvMsgInput.setText("");
                    adapter.notifyDataSetChanged();
                }

            }
        });

        imageViewChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        allMsg = new ArrayList<>();
        adapter = new MessageListAdapter(this, allMsg);
        rvMsgList.setAdapter(adapter);
        rvMsgList.setLayoutManager(new LinearLayoutManager(this));

        queryMessageHistory();
        //refresh(1000);

    }

    private void refresh(int millisecons){
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                queryMessageHistory();
            }
        };
        handler.postDelayed(runnable,millisecons);
    }




    private void queryMessageHistory() {
        ParseQuery<ChatMsg> query1 = ParseQuery.getQuery(ChatMsg.class);
        query1.whereEqualTo(ChatMsg.KEY_RECEIVER, ParseUser.getCurrentUser() );
        query1.whereEqualTo(ChatMsg.KEY_SENDER, user);

        query1.findInBackground(new FindCallback<ChatMsg>() {
            @SuppressLint("LongLogTag")
            @Override
            public void done(List<ChatMsg> msgList, ParseException e) {
                if(e != null){
                    Log.e(TAG, "Issue with getting users", e);
                    return;
                }
                allMsg.addAll(msgList);
                Collections.sort(allMsg, new Comparator<ChatMsg>(){
                    @Override
                    public int compare(ChatMsg msg1, ChatMsg msg2){
                        return (msg1.getCreatedAt().compareTo(msg2.getCreatedAt()));
                    }
                });
                adapter.notifyDataSetChanged();
                if(allMsg.size()>4)
                    rvMsgList.smoothScrollToPosition(allMsg.size()-1);
            }
        });

        ParseQuery<ChatMsg> query2 = ParseQuery.getQuery(ChatMsg.class);
        query2.whereEqualTo(ChatMsg.KEY_SENDER, ParseUser.getCurrentUser() );
        query2.whereEqualTo(ChatMsg.KEY_RECEIVER, user);

        query2.findInBackground(new FindCallback<ChatMsg>() {
            @SuppressLint("LongLogTag")
            @Override
            public void done(List<ChatMsg> msgList, ParseException e) {
                if(e != null){
                    Log.e(TAG, "Issue with getting users", e);
                    return;
                }
                allMsg.addAll(msgList);
                Collections.sort(allMsg, new Comparator<ChatMsg>(){
                    @Override
                    public int compare(ChatMsg msg1, ChatMsg msg2){
                        return (msg1.getCreatedAt().compareTo(msg2.getCreatedAt()));
                    }
                });
                adapter.notifyDataSetChanged();
                if(allMsg.size()>4)
                    rvMsgList.smoothScrollToPosition(allMsg.size()-1);
            }
        });

    }


    private void SendMessage(String content) {
        ChatMsg message = new ChatMsg();
        message.setSender(ParseUser.getCurrentUser());
        message.setReceiver(user);
        message.setContent(content);

        Log.i(TAG, "send to   : " + user.getUsername());
        Log.i(TAG, "content: " + content);

        message.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.d(TAG, "Error while saving");
                    e.printStackTrace();
                    return;
                }
                Log.i(TAG, "Success");
            }
        });
        allMsg.add(message);
        adapter.notifyDataSetChanged();
        rvMsgList.smoothScrollToPosition(allMsg.size()-1);

    }

}