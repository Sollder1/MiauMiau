package de.sollder1.miaumiauserver.messagehandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.sollder1.miaumiauserver.messagemodel.Request;
import jakarta.websocket.Session;

public interface MessageHandler {

    Object handle(Session session, Request data) throws JsonProcessingException;

}
