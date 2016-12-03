package com.example.mostafa.botota;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button sendButton = (Button) this.findViewById(R.id.send_button);
        EditText messageText = (EditText) this.findViewById(R.id.message_input);

        BototaAPIHandler apiHandler = new BototaAPIHandler(this);
        apiHandler.startNewChat();

        sendButton.setOnClickListener(new SendButtonActionListener(messageText, apiHandler));
    }
}
