package com.example.mostafa.botota;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

/**
 * Created by mostafa on 03/12/16.
 */

public class BototaAPIHandler {
    AsyncHttpClient client;
    Context applicationContext;
    MessageAdapter messageAdapter;
    boolean requestIsInProgress;

    final String WELCOME_ENDPOINT = "https://botota.herokuapp.com/welcome";
    final String CHAT_ENDPOINT = "https://botota.herokuapp.com/chat";

    BototaAPIHandler(Context applicationContext) {
        this.applicationContext = applicationContext;
        client = new AsyncHttpClient();
        messageAdapter = new MessageAdapter(applicationContext);
        this.requestIsInProgress = false;
    }

    public void startNewChat() {
        this.requestIsInProgress = true;
        client.get(WELCOME_ENDPOINT, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    requestIsInProgress = false;
                    String uuid = (String) response.get("uuid");
                    client.addHeader("Authorization", uuid);

                    Gson gson = new Gson();

                    // TODO put Botota image
                    Message nameMessage = new Message("Botota", null, null, R.layout.name, Sender.BOTOTA);
                    Message message = gson.fromJson((JsonElement) response.get("message"), Message.class);
                    message.setLayout(R.layout.message);
                    message.setSender(Sender.BOTOTA);
                    messageAdapter.add(nameMessage);
                    messageAdapter.add(message);
                } catch (JSONException e) {
//                    messageAdapter.add(new Message("Botota", "Something went wrong."));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                requestIsInProgress = false;
//                messageAdapter.add(new Message("Botota", "Something went wrong."));
            }
        });
    }


    public void sendMessage(String messageInput) throws UnsupportedEncodingException, JSONException {
        if(requestIsInProgress) {
            return;
        }
        this.requestIsInProgress = true;

        // TODO put my picture
        Message myNameMessage = new Message("Me", null, null, R.layout.name, Sender.ME);
        Message myMessage = new Message(null, messageInput, null, R.layout.message, Sender.ME);
        this.messageAdapter.add(myNameMessage);
        this.messageAdapter.add(myMessage);

        JSONObject requestBody = new JSONObject();
        requestBody.put("message", messageInput);
        ByteArrayEntity entity = new ByteArrayEntity(requestBody.toString().getBytes("UTF-8"));
        entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

        client.post(this.applicationContext, CHAT_ENDPOINT, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                requestIsInProgress = false;
                try {
                    Gson gson = new Gson();

                    Message nameMessage = new Message("Botota", null, null, R.layout.name, Sender.BOTOTA);
                    messageAdapter.add(nameMessage);
                    Message[] messages = gson.fromJson((JsonElement) response.get("message"), Message[].class);

                    for(Message message : messages) {
                        message.setLayout(R.layout.message);
                        message.setSender(Sender.BOTOTA);
                        messageAdapter.add(message);
                    }
                } catch (JSONException e) {
//                    messageAdapter.add(new Message("Botota", "Something went wrong."));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                requestIsInProgress = false;
//                messageAdapter.add(new Message("Botota", responseString));
            }
        });

    }
}
