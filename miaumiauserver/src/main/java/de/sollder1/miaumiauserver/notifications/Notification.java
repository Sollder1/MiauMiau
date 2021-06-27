package de.sollder1.miaumiauserver.notifications;

public abstract class Notification {

    private String event;

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}
