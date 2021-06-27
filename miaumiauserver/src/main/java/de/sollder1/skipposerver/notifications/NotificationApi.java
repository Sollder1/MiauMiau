package de.sollder1.skipposerver.notifications;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.sollder1.skipposerver.game.state.lobby.Card;
import de.sollder1.skipposerver.game.state.lobby.Lobby;
import de.sollder1.skipposerver.game.state.player.Player;
import de.sollder1.skipposerver.game.state.player.SessionWrapper;
import de.sollder1.skipposerver.messagehandler.MessageHandlerApi;
import de.sollder1.skipposerver.notifications.impl.ChatMessageNotification;
import de.sollder1.skipposerver.notifications.impl.GameUpdateNotification;
import de.sollder1.skipposerver.notifications.impl.LobbyNotification;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

public class NotificationApi {

    //Notify all in Lobby about the joined Player
    public static void notifyAboutJoinedPlayer(Player joinedPlayer, Lobby joinedLobby) {
        String value = getJoinedPlayerNotification(joinedPlayer, joinedLobby);
        joinedLobby.getPlayers().stream()
                .filter(player -> !player.getPlayerId().equals(joinedPlayer.getPlayerId()))
                .forEach(player -> player.getSession().send(value));
    }

    //Catch up the palyer that joined aboutr the peple in the lobby
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

    public static void sendChatMessage(Player player, String chatMessage) {
        var lobby = player.getCurrentLobby();

        if (lobby != null) {
            lobby.getPlayers().forEach(playerToSendTo -> playerToSendTo.getSession().send(generateChatMessageNotification(playerToSendTo, chatMessage)));
        }

    }

    private static String generateChatMessageNotification(Player playerToSendTo, String chatMessage) {
        ChatMessageNotification notification = new ChatMessageNotification();
        notification.setEvent("newMessage");
        notification.setId(UUID.randomUUID().toString());
        notification.setText(chatMessage);
        notification.setUsername(playerToSendTo.getPlayerName());
        notification.setTime((new SimpleDateFormat("hh:mm")).format(new Date()));
        return writeToString(notification);
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
