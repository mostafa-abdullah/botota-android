package com.example.mostafa.botota;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by mostafa on 03/12/16.
 */

public class MessageAdapter extends BaseAdapter{
    ArrayList<Message> messages;
    Context messageContext;
    LayoutInflater layoutInflater;

    public MessageAdapter(Context messageContext) {
        this.messages = new ArrayList<Message>();
        this.messageContext = messageContext;
        layoutInflater = (LayoutInflater) messageContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int i) {
        return messages.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Message message = (Message) this.getItem(i);
        view = layoutInflater.inflate(message.getLayout(), null);

        if(message.getLayout() == R.layout.name) {
            NameViewHolder viewHolder = new NameViewHolder();
            viewHolder.avatar = (ImageView) view.findViewById(R.id.avatar);
            viewHolder.name = (TextView) view.findViewById(R.id.name);


            viewHolder.name.setText(message.getSender() == Sender.ME ? "Me" : "Botota");
            // TODO set profile pic here

            configureNameLayout(viewHolder, message.getSender());
        }
        else {
            MessageViewHolder viewHolder = new MessageViewHolder();
            viewHolder.messageImage = (ImageView) view.findViewById(R.id.image);
            viewHolder.messageBody = (TextView) view.findViewById(R.id.body);
            viewHolder.messageHighlight = (TextView) view.findViewById(R.id.highlight);

            setMessageViewComponents(viewHolder, message);

            configureMessageLayout(viewHolder, message.getSender());
        }

        return view;
    }

    private void setMessageViewComponents(MessageViewHolder viewHolder, Message message) {
        if(message.getImage() == null || message.getImage().trim().isEmpty()) {
            viewHolder.messageImage.setVisibility(View.GONE);
        }
        else {
            // TODO put image in body here
        }

        if(message.getText() == null || message.getText().trim().isEmpty()) {
            viewHolder.messageBody.setVisibility(View.GONE);
        }
        else {
            viewHolder.messageBody.setText(message.getText());
        }

        if(message.getHighlight() == null || message.getHighlight().trim().isEmpty()) {
            viewHolder.messageHighlight.setVisibility(View.GONE);
        }
        else {
            viewHolder.messageHighlight.setText(message.getText());
        }
    }

    public void add(Message message) {
        messages.add(message);
        this.notifyDataSetChanged();
    }

    private class NameViewHolder {
        ImageView avatar;
        TextView name;
    }

    private class MessageViewHolder {
        ImageView messageImage;
        TextView messageBody;
        TextView messageHighlight;
    }

    private void configureNameLayout(NameViewHolder viewHolder, Sender sender) {
        int marginLeft = 25;
        int marginTop = 15;
        int marginRight = 0;
        int marginBottom = 0;
        int layoutAlignment = RelativeLayout.ALIGN_PARENT_LEFT;

        if(sender == Sender.ME) {
            marginLeft = 0;
            marginRight = 25;
            layoutAlignment = RelativeLayout.ALIGN_PARENT_RIGHT;
        }

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)viewHolder.avatar.getLayoutParams();
        params.addRule(layoutAlignment);
        params.setMargins(marginLeft, marginTop, marginRight, marginBottom);
        viewHolder.avatar.setLayoutParams(params);

        params = (RelativeLayout.LayoutParams)viewHolder.name.getLayoutParams();
        params.addRule(layoutAlignment);
        params.setMargins(marginLeft, marginTop, marginRight, marginBottom);
        viewHolder.name.setLayoutParams(params);
    }

    private void configureMessageLayout(MessageViewHolder viewHolder, Sender sender) {
        int marginLeft = 25;
        int marginTop = 15;
        int marginRight = 0;
        int marginBottom = 0;
        int layoutAlignment = RelativeLayout.ALIGN_PARENT_LEFT;

        if(sender == Sender.ME) {
            marginLeft = 0;
            marginRight = 25;
            layoutAlignment = RelativeLayout.ALIGN_PARENT_RIGHT;
        }

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)viewHolder.messageHighlight.getLayoutParams();
        params.addRule(layoutAlignment);
        params.setMargins(marginLeft, marginTop, marginRight, marginBottom);
        viewHolder.messageHighlight.setLayoutParams(params);

        params = (RelativeLayout.LayoutParams)viewHolder.messageBody.getLayoutParams();
        params.addRule(layoutAlignment);
        params.setMargins(marginLeft, marginTop, marginRight, marginBottom);
        viewHolder.messageBody.setLayoutParams(params);

        params = (RelativeLayout.LayoutParams)viewHolder.messageImage.getLayoutParams();
        params.addRule(layoutAlignment);
        params.setMargins(marginLeft, marginTop, marginRight, marginBottom);
        viewHolder.messageImage.setLayoutParams(params);
    }
}
