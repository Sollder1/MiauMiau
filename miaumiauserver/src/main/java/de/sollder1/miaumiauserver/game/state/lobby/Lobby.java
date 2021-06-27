package de.sollder1.miaumiauserver.game.state.lobby;

import de.sollder1.miaumiauserver.game.state.player.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ThreadLocalRandom;

public class Lobby {

    private final String lobbyId;
    private final String lobbyName;
    private boolean started;

    private final List<Player> players;

    private int currentPlayerIndex;

    private List<Card> drawStack = new ArrayList<>();
    private final List<Card> layStack = new ArrayList<>();

    private final ConcurrentMap<Player, List<Card>> playerCards = new ConcurrentHashMap<>();


    public Lobby(String lobbyName) {
        this.lobbyId = UUID.randomUUID().toString();
        this.lobbyName = lobbyName;
        players = Collections.synchronizedList(new ArrayList<>());
    }

    //Syncronised(Writing Methods)
    public synchronized void start() {
        this.started = true;
        drawStack = Card.getDefaultDeck();
        Collections.shuffle(drawStack, ThreadLocalRandom.current());

        //Init player hands...
        players.forEach(player -> {
            var cards = new ArrayList<>(drawStack.subList(0, 5));
            drawStack.removeAll(cards);
            playerCards.put(player, cards);
        });

        this.layStack.add(drawStack.remove(0));
        currentPlayerIndex = 0;
    }

    public synchronized Card getLastLayedCard() {
        return layStack.get(layStack.size() - 1);
    }

    public synchronized void addPlayer(Player player) {

        if(players.contains(player)){
            return;
        }

        players.add(player);
    }

    public synchronized void removePlayer(Player player) {
        players.remove(player);
    }

    public synchronized List<Card> getMyCards(Player player) {
        return playerCards.get(player);
    }


    public synchronized void drawCardAndChangePlayer(Player player) {
        checkPlayersTurn(player);
        drawCard(player);
        changeTurn();
    }

    public synchronized void playCard(Player player, String cardId) {

        checkPlayersTurn(player);

        var playersCards = playerCards.get(player);
        var cardToPlay = playersCards.stream().filter(card -> card.getId().equals(cardId)).findAny().orElseThrow();
        if (this.getLastLayedCard().compatible(cardToPlay)) {
            this.layStack.add(cardToPlay);
            playersCards.remove(cardToPlay);
            changeTurn();
        } else {
            throw new RuntimeException("Karte passt nicht!");
        }
        executeSpecialFeatures(cardToPlay);
    }

    private void executeSpecialFeatures(Card playedCard) {

        switch (playedCard.getValue()){
            case SEVEN:
                drawCard(players.get(this.currentPlayerIndex));
                drawCard(players.get(this.currentPlayerIndex));
                break;
            case EIGHTH:
                changeTurn();
                break;
        }


    }

    private void drawCard(Player player) {
        if (drawStack.size() == 0) {
            var topCard = layStack.get(layStack.size()-1);
            drawStack.addAll(layStack);
            layStack.clear();
            drawStack.remove(topCard);
            layStack.add(topCard);
        }

        if (drawStack.size() == 0) {
            throw new RuntimeException("Kenn Karte meh!");
        }
        var card = drawStack.remove(0);
        playerCards.get(player).add(card);
    }

    private void changeTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % (players.size());
    }

    private void checkPlayersTurn(Player player) {
        if (!Objects.equals(player, players.get(currentPlayerIndex))) {
            throw new RuntimeException("Spieler net dran!");
        }
    }


    //Getter:
    public List<Player> getPlayers() {
        return players;
    }

    public boolean isStarted() {
        return started;
    }

    public Player getCurrenPlayer() {
        return this.players.get(currentPlayerIndex);
    }

    public String getLobbyId() {
        return lobbyId;
    }

    public String getLobbyName() {
        return lobbyName;
    }
}
