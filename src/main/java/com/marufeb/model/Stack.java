package com.marufeb.model;

import java.util.ArrayList;
import java.util.List;

public class Stack {
    private final List<Card> cards;

    public Stack(int cardNumber) {
        if (cardNumber % 4 != 0)
            throw new IllegalArgumentException("Card number must be divisible by 4!");

        cards = new ArrayList<>(cardNumber);

        for (int i = 1; i <= cardNumber / 4; i++) {
            cards.add(new Card(i, Card.Type.CLUBS));
            cards.add(new Card(i, Card.Type.DIAMONDS));
            cards.add(new Card(i, Card.Type.HEART));
            cards.add(new Card(i, Card.Type.SPADES));
        }
    }

    public List<Card> getCards() {
        return cards;
    }
}
