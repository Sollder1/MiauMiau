package de.sollder1.skipposerver.game.state.lobby;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Card {

    private CardValue value;
    private CardFace face;

    public enum CardValue {
        SEVEN, EIGHTH, NINE, TEN, JACK, QUEEN, KING, ACE
    }

    public enum CardFace {
        HEART, PICK, DIAMOND, CROSS
    }

    public Card(CardValue value, CardFace face) {
        this.value = value;
        this.face = face;
    }

    public String getId(){
        return value.name() + "_" + face.name();
    }

    public boolean compatible(Card cardToPlay) {
        return this.face == cardToPlay.face || this.value == cardToPlay.value;
    }



    public static List<Card> getDefaultDeck(){

        List<Card> cards = new ArrayList<>();

        cards.add(new Card(CardValue.SEVEN,     CardFace.HEART));
        cards.add(new Card(CardValue.EIGHTH,    CardFace.HEART));
        cards.add(new Card(CardValue.NINE,      CardFace.HEART));
        cards.add(new Card(CardValue.TEN,       CardFace.HEART));
        cards.add(new Card(CardValue.JACK,      CardFace.HEART));
        cards.add(new Card(CardValue.QUEEN,     CardFace.HEART));
        cards.add(new Card(CardValue.KING,      CardFace.HEART));
        cards.add(new Card(CardValue.ACE,       CardFace.HEART));

        cards.add(new Card(CardValue.SEVEN,     CardFace.PICK));
        cards.add(new Card(CardValue.EIGHTH,    CardFace.PICK));
        cards.add(new Card(CardValue.NINE,      CardFace.PICK));
        cards.add(new Card(CardValue.TEN,       CardFace.PICK));
        cards.add(new Card(CardValue.JACK,      CardFace.PICK));
        cards.add(new Card(CardValue.QUEEN,     CardFace.PICK));
        cards.add(new Card(CardValue.KING,      CardFace.PICK));
        cards.add(new Card(CardValue.ACE,       CardFace.PICK));

        cards.add(new Card(CardValue.SEVEN,     CardFace.DIAMOND));
        cards.add(new Card(CardValue.EIGHTH,    CardFace.DIAMOND));
        cards.add(new Card(CardValue.NINE,      CardFace.DIAMOND));
        cards.add(new Card(CardValue.TEN,       CardFace.DIAMOND));
        cards.add(new Card(CardValue.JACK,      CardFace.DIAMOND));
        cards.add(new Card(CardValue.QUEEN,     CardFace.DIAMOND));
        cards.add(new Card(CardValue.KING,      CardFace.DIAMOND));
        cards.add(new Card(CardValue.ACE,       CardFace.DIAMOND));

        cards.add(new Card(CardValue.SEVEN,     CardFace.CROSS));
        cards.add(new Card(CardValue.EIGHTH,    CardFace.CROSS));
        cards.add(new Card(CardValue.NINE,      CardFace.CROSS));
        cards.add(new Card(CardValue.TEN,       CardFace.CROSS));
        cards.add(new Card(CardValue.JACK,      CardFace.CROSS));
        cards.add(new Card(CardValue.QUEEN,     CardFace.CROSS));
        cards.add(new Card(CardValue.KING,      CardFace.CROSS));
        cards.add(new Card(CardValue.ACE,       CardFace.CROSS));

        return cards;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return value == card.value && face == card.face;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, face);
    }
}
