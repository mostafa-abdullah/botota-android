package com.example.mostafa.botota;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.graphics.PorterDuff.Mode;
import com.squareup.picasso.Picasso;
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
    /**
     * Holder for the message
     */
    private class MessageViewHolder {
        ImageView messageImage;
        TextView messageBody;
        TextView messageHighlight;
        LinearLayout bubble;
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
            viewHolder.avatar.setImageResource(message.getSender() == Sender.ME ? R.drawable.traveller : R.drawable.botota);

            configureNameLayout(viewHolder, message.getSender());
        }
        else {
            MessageViewHolder viewHolder = new MessageViewHolder();
            viewHolder.messageImage = (ImageView) view.findViewById(R.id.image);
            viewHolder.messageBody = (TextView) view.findViewById(R.id.body);
            viewHolder.messageHighlight = (TextView) view.findViewById(R.id.highlight);
            viewHolder.bubble = (LinearLayout) view.findViewById(R.id.bubble);
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
            message.normalizeText();
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
        int marginTop = 10;
        int marginRight = 0;
        int marginBottom = 0;
        int layoutAlignment = Gravity.LEFT;

        if(sender == Sender.ME) {
            layoutAlignment = Gravity.RIGHT;
            viewHolder.messageBody.setTextColor(messageContext.getResources().getColor(R.color.userText));
            viewHolder.messageHighlight.setTextColor(messageContext.getResources().getColor(R.color.userText));
            viewHolder.bubble.getBackground().setColorFilter(messageContext.getResources().getColor(R.color.userColor), Mode.SRC_ATOP);
        }
        else{
            viewHolder.bubble.getBackground().setColorFilter(messageContext.getResources().getColor(R.color.bototaColor), Mode.SRC_ATOP);
        }

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)viewHolder.bubble.getLayoutParams();
        params.setMargins(marginLeft, marginTop, marginRight, marginBottom);
        params.gravity = layoutAlignment;
        viewHolder.bubble.setLayoutParams(params);
    }
}
