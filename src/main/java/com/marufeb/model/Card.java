package com.marufeb.model;

public class Card {
    private final int value;
    private final Type type;

    public enum Type {
        SPADES("SPADES"), CLUBS("CLUBS"), DIAMONDS("DIAMONDS"), HEART("HEART");

        Type(String names) {
            this.typeName = names;
        }

        private final String typeName;
        public static int types = 4;

        public String getName() {
            return typeName;
        }
    }

    public Card(int value, Type type) {
        if (value<0 || type == null)
            throw new IllegalArgumentException("Error occurred when creating a card");
        this.value = value == 1 ? 14 : value;
        this.type = type;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return (value == 11 ? "JACK" : value == 12 ? "QUEEN" : value == 13? "KING" : value == 14? "1" : value)  +
                " of " + type.typeName;
    }
}
