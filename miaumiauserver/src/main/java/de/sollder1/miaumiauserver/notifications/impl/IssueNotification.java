package de.sollder1.miaumiauserver.notifications.impl;

import de.sollder1.miaumiauserver.notifications.Notification;

public class IssueNotification extends Notification {

    public IssueNotification(String text) {
        this.text = text;
        this.setEvent("issue");
    }

    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
