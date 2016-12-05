package com.example.mostafa.botota;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MessageAdapter extends BaseAdapter{
    ArrayList<Message> messages;
    Context messageContext;
    LayoutInflater layoutInflater;

    public MessageAdapter(Context messageContext) {
        this.messages = new ArrayList<Message>();
        this.messageContext = messageContext;
        layoutInflater = (LayoutInflater) messageContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        ListView messagesView = (ListView) ((Activity) messageContext).findViewById(R.id.messages_view);
        messagesView.setAdapter(this);
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
            viewHolder.avatar.setImageResource(message.getSender() == Sender.ME ? R.drawable.hiker : R.drawable.botota);

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

    /**
     * Maps the message contents to the corresponding UI elements in the activity
     * @param viewHolder The holder for the UI elements
     * @param message The message to be displayed
     */
    private void setMessageViewComponents(MessageViewHolder viewHolder, Message message) {
        if(message.getImage() == null || message.getImage().trim().isEmpty()) {
            // No message image
            viewHolder.messageImage.setVisibility(View.GONE);
        }
        else {
            Picasso.with(messageContext).
                    load(message.getImage()).
                    into(viewHolder.messageImage);

        }

        if(message.getText() == null || message.getText().trim().isEmpty()) {
            // No message body
            viewHolder.messageBody.setVisibility(View.GONE);
        }
        else {
            viewHolder.messageBody.setText(message.getText());
        }

        if(message.getHighlight() == null || message.getHighlight().trim().isEmpty()) {
            // No message highlight
            viewHolder.messageHighlight.setVisibility(View.GONE);
        }
        else {
            viewHolder.messageHighlight.setText(message.getHighlight());
        }
    }

    /**
     * Adds a new message to the ListView
     * @param message
     */
    public void add(Message message) {
        messages.add(message);
        this.notifyDataSetChanged();
    }

    /**
     * Holder for the name
     */
    private class NameViewHolder {
        ImageView avatar;
        TextView name;
    }

    /**
     * Holder for the message
     */
    private class MessageViewHolder {
        ImageView messageImage;
        TextView messageBody;
        TextView messageHighlight;
    }

    /**
     * Configure the layout for the name:
     * Botota: left aligned
     * User: Right aligned
     * @param viewHolder
     * @param sender
     */
    private void configureNameLayout(NameViewHolder viewHolder, Sender sender) {
        int marginLeft = 25;
        int marginTop = 15;
        int marginRight = 0;
        int marginBottom = 0;
        int layoutAlignment = RelativeLayout.ALIGN_PARENT_LEFT;
        int namePosition = RelativeLayout.RIGHT_OF;

        if(sender == Sender.ME) {
            marginLeft = 0;
            marginRight = 25;
            layoutAlignment = RelativeLayout.ALIGN_PARENT_RIGHT;
            namePosition = RelativeLayout.LEFT_OF;
        }

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)viewHolder.avatar.getLayoutParams();
        params.addRule(layoutAlignment);
        params.setMargins(marginLeft, marginTop, marginRight, marginBottom);
        viewHolder.avatar.setLayoutParams(params);

        params = (RelativeLayout.LayoutParams)viewHolder.name.getLayoutParams();
        params.setMargins(marginLeft, marginTop, marginRight, marginBottom);
        params.addRule(namePosition, R.id.avatar);
        viewHolder.name.setLayoutParams(params);
    }

    /**
     * Configure the layout for the message:
     * Botota: left aligned
     * User: Right aligned
     * @param viewHolder
     * @param sender
     */
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
