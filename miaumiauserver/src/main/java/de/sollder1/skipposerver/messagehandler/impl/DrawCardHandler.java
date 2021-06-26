package de.sollder1.skipposerver.messagehandler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.sollder1.skipposerver.game.state.lobby.Lobbies;
import de.sollder1.skipposerver.game.state.player.Players;
import de.sollder1.skipposerver.messagehandler.MessageHandler;
import de.sollder1.skipposerver.messagemodel.Request;
import de.sollder1.skipposerver.notifications.NotificationApi;
import jakarta.websocket.Session;

public class DrawCardHandler implements MessageHandler {

    @Override
    public Object handle(Session session, Request data) throws JsonProcessingException {
        var player = Players.getPlayerById(data.getUserId()).orElseThrow();
        Lobbies.getLobbyById(player.getCurrentLobby().getLobbyId()).orElseThrow().drawCard(player);
        player.getCurrentLobby().getPlayers().forEach(NotificationApi::sendGameUpdate);
        return "";
    }
}
