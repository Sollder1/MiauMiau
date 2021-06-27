package de.sollder1.skipposerver.messagehandler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.sollder1.skipposerver.game.state.player.Players;
import de.sollder1.skipposerver.messagehandler.MessageHandler;
import de.sollder1.skipposerver.messagehandler.MessageHandlerApi;
import de.sollder1.skipposerver.messagemodel.Request;
import de.sollder1.skipposerver.messagemodel.impl.ChatMessagePayload;
import de.sollder1.skipposerver.notifications.NotificationApi;
import jakarta.websocket.Session;

public class ChatMessageHandler implements MessageHandler {


    @Override
    public Object handle(Session session, Request data) throws JsonProcessingException {
        var payload = MessageHandlerApi.OBJECT_MAPPER.treeToValue(data.getData(), ChatMessagePayload.class);
        Players.getPlayerById(data.getUserId()).ifPresent(player -> {
            NotificationApi.sendChatMessage(player, payload.getChatMessage());
        });
        return "";
    }
}
