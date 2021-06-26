package de.sollder1.skipposerver.messagehandler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.sollder1.skipposerver.game.state.lobby.Lobbies;
import de.sollder1.skipposerver.game.state.player.Players;
import de.sollder1.skipposerver.messagehandler.MessageHandler;
import de.sollder1.skipposerver.messagehandler.MessageHandlerApi;
import de.sollder1.skipposerver.messagemodel.Request;
import de.sollder1.skipposerver.messagemodel.impl.CreateJoinLobbyPayload;
import jakarta.websocket.Session;

public class CreateLobbyHandler implements MessageHandler {
    @Override
    public Object handle(Session session, Request data) throws JsonProcessingException {
        var payload = MessageHandlerApi.OBJECT_MAPPER.treeToValue(data.getData(), CreateJoinLobbyPayload.class);
        var lobbyId = Lobbies.createLobby(payload.getLobbyName());
        Lobbies.joinLobby(Players.getPlayerById(data.getUserId()).orElseThrow(), lobbyId);
        return "";
    }
}
