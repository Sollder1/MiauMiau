package de.sollder1.skipposerver.notifications.impl;

import de.sollder1.skipposerver.notifications.Notification;
import de.sollder1.skipposerver.notifications.NotificationApi;

import java.util.List;

public class GameUpdateNotification extends Notification {

    private boolean lobbyStarted;
    private String currentPlayerId;
    private String lastCardOnStack;

    private List<String> myCards;
    private List<String> myNewCards;

    public boolean isLobbyStarted() {
        return lobbyStarted;
    }

    public void setLobbyStarted(boolean lobbyStarted) {
        this.lobbyStarted = lobbyStarted;
    }

    public String getCurrentPlayerId() {
        return currentPlayerId;
    }

    public void setCurrentPlayerId(String currentPlayerId) {
        this.currentPlayerId = currentPlayerId;
    }

    public String getLastCardOnStack() {
        return lastCardOnStack;
    }

    public void setLastCardOnStack(String lastCardOnStack) {
        this.lastCardOnStack = lastCardOnStack;
    }

    public List<String> getMyCards() {
        return myCards;
    }

    public void setMyCards(List<String> myCards) {
        this.myCards = myCards;
    }

    public List<String> getMyNewCards() {
        return myNewCards;
    }

    public void setMyNewCards(List<String> myNewCards) {
        this.myNewCards = myNewCards;
    }
}
