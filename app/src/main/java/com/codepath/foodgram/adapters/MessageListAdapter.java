package com.codepath.foodgram.adapters;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.foodgram.R;
import com.codepath.foodgram.models.ChatMsg;
import com.codepath.foodgram.models.TimeFormatter;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;



public class MessageListAdapter extends RecyclerView.Adapter{

    public static final String TAG = "MessageListAdapter";

    private List<ChatMsg> allMsg;
    private Context context;

    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;



    public MessageListAdapter(Context context, List<ChatMsg> allMsg){
        this.context = context;
        this.allMsg = allMsg;
    }


    // Determines the appropriate ViewType according to the sender of the message.
    @Override
    public int getItemViewType(int position) {
        ChatMsg message = (ChatMsg) allMsg.get(position);

        if (message.getSenderObject().getObjectId().equals(ParseUser.getCurrentUser().getObjectId())){
            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_SENT;
        }
        else {
            // If some other user sent the message
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_current_user_msg_item, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_other_user_msg_item, parent, false);
            return new ReceivedMessageHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMsg message = (ChatMsg) allMsg.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(message, position);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                try {
                    ((ReceivedMessageHolder) holder).bind(message, position);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
        }
    }

    @Override
    public int getItemCount() {
        return allMsg.size();
    }


    // use two view holder to hanlde the sender and receiver

    //the other user
    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText, nameText;
        ImageView profileImage;
        private ParseFile image;


        ReceivedMessageHolder(View itemView) {
            super(itemView);

            messageText = itemView.findViewById(R.id.text_gchat_message_other);
            timeText = itemView.findViewById(R.id.text_gchat_timestamp_other);
            nameText = itemView.findViewById(R.id.text_gchat_user_other);
            profileImage = itemView.findViewById(R.id.image_gchat_profile_other);
        }

        void bind(ChatMsg message, int position) throws ParseException {

            //Display information
            nameText.setText(message.getParseUser("senderObject").fetchIfNeeded().getUsername());

            image = message.getParseUser("senderObject").getParseFile("icon");
            if (image != null) {
                Glide.with(context).load(image.getUrl()).transform(new CenterInside(),
                        new RoundedCorners(20)).into(profileImage);
            }

            messageText.setText(message.getContent());

            // Format the stored timestamp into a readable String using method.
            timeText.setText(message.getCreatedAt().toString());

        }
    }


    //the current user
    private class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText;

        SentMessageHolder(View itemView) {
            super(itemView);

            messageText = (TextView) itemView.findViewById(R.id.text_gchat_message_me);
            timeText = (TextView) itemView.findViewById(R.id.text_gchat_timestamp_me);
        }

        void bind(ChatMsg message, int position) {
            messageText.setText(message.getContent());

            // Format the stored timestamp into a readable String using method.
            //timeText.setText(Utils.formatDateTime(message.getCreatedAt()));
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    timeText.setText(message.getCreatedAt().toString());
                }
            }, 700);
            //timeText.setText(message.getUpdatedAt().toString());


        }


    }
}
