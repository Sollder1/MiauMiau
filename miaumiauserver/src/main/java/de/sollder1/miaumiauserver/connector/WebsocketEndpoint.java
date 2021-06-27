package de.sollder1.miaumiauserver.connector;

import de.sollder1.miaumiauserver.game.state.player.Players;
import de.sollder1.miaumiauserver.messagehandler.MessageHandlerApi;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import java.io.IOException;

@ServerEndpoint("/websocket/{username}/{id}")
public class WebsocketEndpoint {

    private String username;
    private String id;

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username, @PathParam("id") String id) {
        this.id = id;
        this.username = username;
        Players.addOrReconnectPlayer(session, username, id);
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        MessageHandlerApi.handle(message, session);
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        Players.disconnect(session, closeReason.getReasonPhrase());
    }

    @OnError
    public void onError(Session session, Throwable issue) {
        Players.disconnect(session, issue.getMessage());
    }
}
