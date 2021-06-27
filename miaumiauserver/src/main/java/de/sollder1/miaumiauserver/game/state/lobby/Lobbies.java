package de.sollder1.miaumiauserver.game.state.lobby;

import de.sollder1.miaumiauserver.game.state.player.Player;
import de.sollder1.miaumiauserver.notifications.NotificationApi;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class Lobbies {

    private Lobbies() {
    }

    private static final ConcurrentHashMap<String, Lobby> LOBBIES = new ConcurrentHashMap<>();


    public static String createLobby(String lobbyName) {
        Lobby lobby = new Lobby(lobbyName);
        LOBBIES.put(lobby.getLobbyId(), lobby);
        return lobby.getLobbyId();
    }

    public static void joinLobby(Player player, String lobbyId) {

        var lobby = getLobbyById(lobbyId).orElseThrow();

        synchronized (lobby) {

            if (lobby.getPlayers().size() >= 4) {
                throw new RuntimeException("Voll!");
            }

            player.setCurrentLobby(lobby);
            lobby.addPlayer(player);

            NotificationApi.notifyAboutJoinedPlayer(player, lobby);
            lobby.getPlayers().forEach(p -> NotificationApi.notifyAboutJoinedPlayer(p, lobby, player.getSession()));
        }

    }

    public static void leaveLobby(Player player) {

        var lobby = player.getCurrentLobby();

        synchronized (lobby) {
            if (player.getCurrentLobby() != null) {
                player.getCurrentLobby().removePlayer(player);
                NotificationApi.notifyAboutLeavingPlayer(player);
                if (!player.getCurrentLobby().isStarted()) {
                    player.setCurrentLobby(null);
                }
            }
        }

    }

    public static void startLobby(String lobbyId) {
        getLobbyById(lobbyId).ifPresent(lobby -> {
            lobby.start();
            lobby.getPlayers().forEach(NotificationApi::sendGameUpdate);
        });
    }

    public static Optional<Lobby> getLobbyById(String lobbyId) {
        return Optional.ofNullable(LOBBIES.get(lobbyId));
    }


}

