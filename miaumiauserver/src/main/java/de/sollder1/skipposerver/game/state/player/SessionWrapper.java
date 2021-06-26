package de.sollder1.skipposerver.game.state.player;

import jakarta.websocket.Session;

import java.io.IOException;

public class SessionWrapper{
    private Session session;

    public SessionWrapper(Session session) {
        this.session = session;
    }

    public void send(String data){
        try {
            session.getBasicRemote().sendText(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getId() {
        return session.getId();
    }
}
