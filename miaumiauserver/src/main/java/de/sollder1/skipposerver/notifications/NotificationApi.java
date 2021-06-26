package de.sollder1.skipposerver.notifications;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.sollder1.skipposerver.game.state.lobby.Card;
import de.sollder1.skipposerver.game.state.lobby.Lobby;
import de.sollder1.skipposerver.game.state.player.Player;
import de.sollder1.skipposerver.game.state.player.SessionWrapper;
import de.sollder1.skipposerver.messagehandler.MessageHandlerApi;
import de.sollder1.skipposerver.notifications.impl.GameUpdateNotification;
import de.sollder1.skipposerver.notifications.impl.LobbyNotification;

import java.util.stream.Collectors;

public class NotificationApi {

    public static void notifyAboutJoinedPlayer(Player joinedPlayer, Lobby joinedLobby) {
        String value = getJoinedPlayerNotification(joinedPlayer, joinedLobby);
        joinedLobby.getPlayers().forEach(player -> player.getSession().send(value));
    }

    //Used to catch up the user that joined about the current session state
    public static void notifyAboutJoinedPlayer(Player joinedPlayer, Lobby joinedLobby, SessionWrapper target) {
        String value = getJoinedPlayerNotification(joinedPlayer, joinedLobby);
        target.send(value);
    }

    private static String getJoinedPlayerNotification(Player joinedPlayer, Lobby joinedLobby) {
        var notification = new LobbyNotification();
        notification.setEvent("joinedLobby");
        notification.setPlayerId(joinedPlayer.getPlayerId());
        notification.setPlayerName(joinedPlayer.getPlayerName());
        notification.setLobbyId(joinedLobby.getLobbyId());
        notification.setLobbyName(joinedLobby.getLobbyName());

        return writeToString(notification);
    }

    public static void notifyAboutLeavingPlayer(Player leftPlayer) {

        if (leftPlayer.getCurrentLobby() == null) {
            return;
        }

        var leftLobby = leftPlayer.getCurrentLobby();
        var notification = new LobbyNotification();
        notification.setEvent("leftLobby");
        notification.setLobbyId(leftLobby.getLobbyId());
        notification.setPlayerId(leftPlayer.getPlayerId());
        var value = writeToString(notification);

        leftLobby.getPlayers().forEach(player -> player.getSession().send(value));
    }

    public static void sendGameUpdate(Player player) {

        var lobby = player.getCurrentLobby();

        var notification = new GameUpdateNotification();
        notification.setEvent("gameUpdate");
        notification.setLobbyStarted(true);
        notification.setCurrentPlayerId(lobby.getCurrenPlayer().getPlayerId());
        notification.setLastCardOnStack(lobby.getLastLayedCard().getId());
        notification.setMyCards(lobby.getMyCards(player).stream().map(Card::getId).collect(Collectors.toList()));

        player.getSession().send(writeToString(notification));

    }

    private static String writeToString(Notification notification) {
        try {
            return MessageHandlerApi.OBJECT_MAPPER.writeValueAsString(notification);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }


}
