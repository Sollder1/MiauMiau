package de.sollder1.miaumiauserver.messagehandler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.sollder1.miaumiauserver.game.state.lobby.Lobbies;
import de.sollder1.miaumiauserver.messagehandler.MessageHandler;
import de.sollder1.miaumiauserver.messagehandler.MessageHandlerApi;
import de.sollder1.miaumiauserver.messagemodel.Request;
import de.sollder1.miaumiauserver.messagemodel.impl.StartLobbyPayload;
import jakarta.websocket.Session;

public class StartLobbyHandler implements MessageHandler {


    @Override
    public Object handle(Session session, Request data) throws JsonProcessingException {
        var payload = MessageHandlerApi.OBJECT_MAPPER.treeToValue(data.getData(), StartLobbyPayload.class);
        Lobbies.startLobby(payload.getLobbyId());
        return "";
    }
}
