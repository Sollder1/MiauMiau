package de.sollder1.miaumiauserver.messagehandler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.sollder1.miaumiauserver.game.state.lobby.Lobbies;
import de.sollder1.miaumiauserver.game.state.player.Players;
import de.sollder1.miaumiauserver.messagehandler.MessageHandler;
import de.sollder1.miaumiauserver.messagemodel.Request;
import de.sollder1.miaumiauserver.notifications.NotificationApi;
import jakarta.websocket.Session;

public class DrawCardHandler implements MessageHandler {

    @Override
    public Object handle(Session session, Request data) throws JsonProcessingException {
        var player = Players.getPlayerById(data.getUserId()).orElseThrow();
        Lobbies.getLobbyById(player.getCurrentLobby().getLobbyId()).orElseThrow().drawCardAndChangePlayer(player);
        player.getCurrentLobby().getPlayers().forEach(NotificationApi::sendGameUpdate);
        return "";
    }
}
