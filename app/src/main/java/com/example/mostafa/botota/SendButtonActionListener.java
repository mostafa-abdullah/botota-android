package com.example.mostafa.botota;

import android.view.View;
import android.widget.EditText;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;

/**
 * Created by mostafa on 02/12/16.
 */

public class SendButtonActionListener implements View.OnClickListener {

    EditText messageInput;
    BototaAPIHandler apiHandler;

    public SendButtonActionListener(EditText messageInput, BototaAPIHandler apiHandler) {
        this.messageInput = messageInput;
        this.apiHandler = apiHandler;
    }

    @Override
    public void onClick(View view) {
        try {
            this.sendMessage();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage() throws UnsupportedEncodingException, JSONException {
        String text = this.messageInput.getText().toString();

        if (text.trim().equals("")) {
            return;
        }

        apiHandler.sendMessage(text);
        this.messageInput.setText("");
    }
}