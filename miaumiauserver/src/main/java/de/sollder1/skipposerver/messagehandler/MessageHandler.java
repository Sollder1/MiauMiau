package de.sollder1.skipposerver.messagehandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import de.sollder1.skipposerver.messagemodel.Request;
import jakarta.websocket.Session;

public interface MessageHandler {

    Object handle(Session session, Request data) throws JsonProcessingException;

}
