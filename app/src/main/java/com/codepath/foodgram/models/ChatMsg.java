package com.codepath.foodgram.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;


@ParseClassName("ChatMessage")
public class ChatMsg extends ParseObject {

    public static final String TAG = "ChatMessage";
    public static final String KEY_OBJECTID = "objectId";

    public static final String KEY_SENDER = "senderObject";
    public static final String KEY_RECEIVER= "receiverObject";
    public static final String KEY_CONTENT= "Content";
    public static final String KEY_CREATED = "createdAt";

    public String getObjectId() {
        return getString(KEY_OBJECTID);
    }


    public ParseUser getSenderObject() {
        return getParseUser("senderObject");
    }

    public ParseUser getReceiverObject() {
        return getParseUser("receiverObject");
    }


    public String getContent() {
        return getString(KEY_CONTENT);
    }





    public String getReceiver() {
        return getString(KEY_RECEIVER);
    }
    public String getSender() {
        return getString(KEY_SENDER);
    }



//    //get the twitter like time stamp
//    public String getFormattedTimestamp(){
//        return TimeFormatter.getTimeDifference(getCreatedAt());
//    }

    public void setSender(ParseUser user) {
        put(KEY_SENDER, user);
    }

    public void setReceiver(ParseUser user) {
        put(KEY_RECEIVER, user);
    }

    public void setContent(String content) {
        put(KEY_CONTENT, content);
    }

}