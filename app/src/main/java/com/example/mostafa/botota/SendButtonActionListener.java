package com.example.mostafa.botota;

import android.view.View;
import android.widget.EditText;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;

public class SendButtonActionListener implements View.OnClickListener {

    EditText messageInput;
    BototaAPIHandler apiHandler;

    public SendButtonActionListener(EditText messageInput, BototaAPIHandler apiHandler) {
        this.messageInput = messageInput;
        this.apiHandler = apiHandler;
    }

    @Override
    public void onClick(View view) {
        this.sendMessage();
    }

    /**
     * Calls the API with the message entered by the user
     */
    private void sendMessage() {
        String text = this.messageInput.getText().toString();

        if (text.trim().equals("")) {
            return;
        }

        try {
            apiHandler.sendMessage(text);
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        this.messageInput.setText("");
    }
}