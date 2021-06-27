package de.sollder1.miaumiauserver.game.state.player;

import de.sollder1.miaumiauserver.game.state.lobby.Lobby;
import jakarta.websocket.Session;

import java.util.Objects;

public class Player {
    private final String playerId;
    private String playerName;
    private SessionWrapper session;
    private boolean active = true;

    private Lobby currentLobby;

    public Player(String playerName, Session session, String id) {
        this.playerName = playerName;
        this.session = new SessionWrapper(session);
        this.playerId = id;
    }

    public String getPlayerId() {
        return playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public SessionWrapper getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = new SessionWrapper(session);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Lobby getCurrentLobby() {
        return currentLobby;
    }

    public void setCurrentLobby(Lobby currentLobby) {
        this.currentLobby = currentLobby;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(playerId, player.playerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerId);
    }
}
