package de.sollder1.skipposerver.messagehandler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.sollder1.skipposerver.game.state.lobby.Lobbies;
import de.sollder1.skipposerver.game.state.player.Players;
import de.sollder1.skipposerver.messagehandler.MessageHandler;
import de.sollder1.skipposerver.messagehandler.MessageHandlerApi;
import de.sollder1.skipposerver.messagemodel.Request;
import de.sollder1.skipposerver.messagemodel.impl.CreateJoinLobbyPayload;
import de.sollder1.skipposerver.messagemodel.impl.PlayCardPayload;
import de.sollder1.skipposerver.notifications.NotificationApi;
import jakarta.websocket.Session;

public class PlayCardHandler implements MessageHandler {

    @Override
    public Object handle(Session session, Request data) throws JsonProcessingException {
        var payload = MessageHandlerApi.OBJECT_MAPPER.treeToValue(data.getData(), PlayCardPayload.class);

        var player = Players.getPlayerById(data.getUserId()).orElseThrow();
        player.getCurrentLobby().playCard(player, payload.getCardId());
        player.getCurrentLobby().getPlayers().forEach(NotificationApi::sendGameUpdate);
        return "";
    }
}
