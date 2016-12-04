package com.example.mostafa.botota;

import android.content.Context;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

public class BototaAPIHandler {
    AsyncHttpClient client;
    Context applicationContext;
    MessageAdapter messageAdapter;
    boolean requestIsInProgress;
    String welcomeEndpoint;
    String chatEndpoint;

    BototaAPIHandler(Context applicationContext) {
        this.applicationContext = applicationContext;
        client = new AsyncHttpClient();
        messageAdapter = new MessageAdapter(applicationContext);
        this.requestIsInProgress = false;

        // Get the endpoints from strings.xml
        welcomeEndpoint = applicationContext.getString(R.string.welcome_endpoint);
        chatEndpoint = applicationContext.getString(R.string.chat_endpoint);
    }

    /**
     * Calls /welcome and starts new chat session
     */
    public void startNewChat() {
        this.requestIsInProgress = true;
        client.get(welcomeEndpoint, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    requestIsInProgress = false;
                    String uuid = (String) response.get("uuid");
                    client.addHeader("Authorization", uuid);

                    Message nameMessage = new Message("Botota", null, null, R.layout.name, Sender.BOTOTA);

                    Gson gson = new Gson();
                    Message message = gson.fromJson(response.get("message").toString(), Message.class);
                    message.setSender(Sender.BOTOTA);
                    message.setLayout(R.layout.message);

                    messageAdapter.add(nameMessage);
                    messageAdapter.add(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                requestIsInProgress = false;
                messageAdapter.add(new Message("Oops, something went wrong.",
                        responseString, null,
                        R.layout.message,
                        Sender.BOTOTA));
            }
        });
    }

    /**
     * Sends a message to the chatbot server-side, and provides the new response to the user
     * @param messageInput The message to be sent to the bot
     * @throws UnsupportedEncodingException
     * @throws JSONException
     */
    public void sendMessage(String messageInput) throws UnsupportedEncodingException, JSONException {
        if(requestIsInProgress) {
            return;
        }
        this.requestIsInProgress = true;

        Message myNameMessage = new Message("Me", null, null, R.layout.name, Sender.ME);
        Message myMessage = new Message(null, messageInput, null, R.layout.message, Sender.ME);
        this.messageAdapter.add(myNameMessage);
        this.messageAdapter.add(myMessage);

        JSONObject requestBody = new JSONObject();
        requestBody.put("message", messageInput);
        ByteArrayEntity entity = new ByteArrayEntity(requestBody.toString().getBytes("UTF-8"));
        entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

        client.post(this.applicationContext, chatEndpoint, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                requestIsInProgress = false;
                try {
                    Gson gson = new Gson();

                    Message nameMessage = new Message("Botota", null, null, R.layout.name, Sender.BOTOTA);
                    messageAdapter.add(nameMessage);

                    // Deserialize the recieved messages from the bot
                    Message[] messages = gson.fromJson((response.get("messages")).toString(), Message[].class);

                    // Adjust the layout for the messages and add to the ListView
                    for(Message message : messages) {
                        message.setLayout(R.layout.message);
                        message.setSender(Sender.BOTOTA);
                        messageAdapter.add(message);
                    }
                } catch (JSONException e) {
                    messageAdapter.add(new Message("Oops, something went wrong.",
                            null, null,
                            R.layout.message,
                            Sender.BOTOTA));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                requestIsInProgress = false;
                messageAdapter.add(new Message("Oops, something went wrong.",
                        responseString, null,
                        R.layout.message,
                        Sender.BOTOTA));
            }
        });

    }
}
