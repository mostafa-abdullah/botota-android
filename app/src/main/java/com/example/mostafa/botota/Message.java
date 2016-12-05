package com.example.mostafa.botota;

public class Message {
    private String highlight;
    private String value;
    private String image;
    private int layout;
    private Sender sender;

    public Message(String highlight, String text, String image, int layout, Sender sender) {
        this.highlight = highlight;
        this.value = text;
        this.image = image;
        this.layout = layout;
        this.sender = sender;
    }

    public void normalizeText() {
        this.value = this.value.replace("\n", "\r\n");
    }

    public String getHighlight() {
        return highlight;
    }

    public String getText() {
        return value;
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
