package de.sollder1.skipposerver.game.state.player;

import de.sollder1.skipposerver.game.state.lobby.Lobbies;
import de.sollder1.skipposerver.notifications.NotificationApi;
import jakarta.websocket.Session;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class Players {

    private Players() {
    }

    private static final Set<Player> activePlayers = ConcurrentHashMap.newKeySet();

    public static void addOrReconnectPlayer(Session playerSession, String username, String id) {
        Optional<Player> optionalPlayer = getPlayerById(id);

        if (optionalPlayer.isPresent()) {
            var player = optionalPlayer.get();

            player.setActive(true);
            player.setPlayerName(username);
            player.setSession(playerSession);

            if (player.getCurrentLobby() != null) {
                Lobbies.joinLobby(player, player.getCurrentLobby().getLobbyId());
                if (player.getCurrentLobby().isStarted()){
                    NotificationApi.sendGameUpdate(player);
                }
            }

        } else {
            var player = new Player(username, playerSession, id);
            activePlayers.add(player);
        }

    }

    public static void disconnect(Session playerSession, String reasonPhrase) {
        var optionalPlayer = getPlayerByIdSessionId(playerSession.getId());
        optionalPlayer.ifPresent(player -> {
            player.setActive(false);
            Lobbies.leaveLobby(player);
        });
    }

    public static Optional<Player> getPlayerById(String id) {
        return activePlayers.stream()
                .filter(player -> player.getPlayerId().equals(id))
                .findAny();
    }

    private static Optional<Player> getPlayerByIdSessionId(String sessionId) {
        return activePlayers.stream()
                .filter(player -> player.getSession().getId().equals(sessionId))
                .findAny();
    }


}
