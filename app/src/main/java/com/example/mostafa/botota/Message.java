package com.example.mostafa.botota;

/**
 * Created by mostafa on 03/12/16.
 */

public class Message {
    private String highlight;
    private String text;
    private String image;
    private int layout;
    private Sender sender;

    public Message(String highlight, String text, String image, int layout, Sender sender) {
        this.highlight = highlight;
        this.text = text;
        this.image = image;
        this.layout = layout;
        this.sender = sender;
    }

    public String getHighlight() {
        return highlight;
    }

    public String getText() {
        return text;
    }

    public String getImage() {
        return image;
    }

    public int getLayout() {
        return layout;
    }

    public void setLayout(int layout) {
        this.layout = layout;
    }

    public Sender getSender() {
        return sender;
    }

    public void setSender(Sender sender) {
        this.sender = sender;
    }

}
