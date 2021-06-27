package de.sollder1.skipposerver.notifications.impl;

import de.sollder1.skipposerver.notifications.Notification;

public class ChatMessageNotification extends Notification {
    private String id;
    private String text;
    private String username;
    private String time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
