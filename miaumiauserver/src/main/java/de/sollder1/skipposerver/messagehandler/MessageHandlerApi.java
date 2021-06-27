package de.sollder1.skipposerver.messagehandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.sollder1.skipposerver.messagehandler.impl.*;
import de.sollder1.skipposerver.messagemodel.Request;
import de.sollder1.skipposerver.notifications.impl.IssueNotification;
import jakarta.websocket.Session;

import java.util.HashMap;
import java.util.Map;

public class MessageHandlerApi {

    public static ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Map<String, Class<? extends MessageHandler>> MESSAGE_HANDLERS = new HashMap<>();

    static {
        MESSAGE_HANDLERS.put("createLobby", CreateLobbyHandler.class);
        MESSAGE_HANDLERS.put("joinLobby", JoinLobbyHandler.class);
        MESSAGE_HANDLERS.put("startLobby", StartLobbyHandler.class);
        MESSAGE_HANDLERS.put("playCard", PlayCardHandler.class);
        MESSAGE_HANDLERS.put("drawCard", DrawCardHandler.class);
        MESSAGE_HANDLERS.put("newMessage", ChatMessageHandler.class);

    }

    public static void handle(String message, Session session) throws JsonProcessingException {
        try {
            var request = OBJECT_MAPPER.readValue(message, Request.class);
            var handler = MESSAGE_HANDLERS.get(request.getCommand()).getConstructor().newInstance();
            OBJECT_MAPPER.writeValueAsString(handler.handle(session, request));
        } catch (Exception e) {
            e.printStackTrace();
            session.getAsyncRemote().sendText(OBJECT_MAPPER.writeValueAsString(new IssueNotification(e.getMessage())));
        }
    }
}
