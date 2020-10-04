package com.marufeb.model;

import java.util.*;
import java.util.stream.Collectors;

public class Dealer {
    public static void shuffle(List<Card> playingCards) {
        int i = playingCards.size(), j;
        Random r = new Random(System.nanoTime());
        while (i>0) {
            j = r.nextInt(playingCards.size());
            i--;
            Card temp = playingCards.get(i);
            playingCards.remove(temp);
            playingCards.add(j, temp);
        }
    }

    /**
     * The dealer <b>gives card</b> to every player.
     * @param playersStack The player's stack [ cover cards ].
     * @param stack The card stack.
     */
    public static void giveCards(List<Queue<Card>> playersStack, Stack stack) {
        playersStack.forEach(Queue::clear);
        for (int i = 0; i+playersStack.size() <= stack.getCards().size(); i+=playersStack.size()) {
            for (int j = 0; j < Table.getVisibleCards().keySet().size(); j++) {
                playersStack.get(j).add(stack.getCards().get(i+j));
            }
        }
    }

    /**
     * The dealer <b>checks</b> if anybody has won the game.
     * @param v The current game variation.
     * @return An optional value that contains winner's name. It contains null when the game has not finished.
     */
    public static Optional<String> checkWin(Variations v) {
        if (v == Variations.THIRD) {
            if (Table.getCoverStacks().stream().filter(it -> it.size() == 0).count() >= 2) {
                int pos = -1;
                for (int i = 0; i < Table.getCoverStacks().size(); i++)
                    if (Table.getCoverStacks().get(i).size() != 0)
                        pos = i;
                if (pos!=-1)
                    for (String player : Table.getVisibleCards().keySet()) {
                        if (pos == 0) return Optional.of(player);
                        pos--;
                    }
                else return Optional.of("TIE GAME\n");
            }
        } else {
            //-----| The game finishes when one player has no more cards
            if (Table.getCoverStacks().stream().anyMatch(it -> it.size() == 0)) {
                int iter = 0, pos = -1;
                for (int i = 0; i < Table.getCoverStacks().size(); i++) {
                    if (Table.getCoverStacks().get(i).size() == 0) {
                        if (iter != 0)
                            return Optional.of("TIE GAME\n");
                        else pos = i;
                        iter ++;
                    }
                }
                if (pos!=-1)
                    for (String player : Table.getVisibleCards().keySet()) {
                        if (pos == 0) return Optional.of(player);
                        pos--;
                    }
            }
        }
        return Optional.empty();
    }

    /**
     * The dealer <b>checks</b> if anybody has won the turn.
     * @return An optional value that contains winner's name. It contains null when the game has not
     * finished
     */
    public static Optional<String> checkGameStatus(Variations v) {
        StringBuilder builder = new StringBuilder();
        Card max = null; String playerName = "NOBODY";
        for (int i = 0; i< (v==Variations.THIRD?3:2); i++) {
            String player = (String) Table.getVisibleCards().keySet().toArray()[i];
            if (Table.getVisibleCards().get(player).peek() != null) {
                Card temp = Table.getVisibleCards().get(player).peek();

                builder.append(player).append(" plays ").append(temp.toString()).append("\n");

                if (max == null || temp.getValue() > max.getValue()) {
                    max = temp;
                    playerName = player;
                }

            } else if (v != Variations.THIRD) {
                String s = Table.getVisibleCards().keySet().stream()
                        .filter(it->!it.equals(player)).collect(Collectors.toList()).get(0);
                if (Table.getVisibleCards().get(s).size() != 0)
                    return Optional.of(s);
                else return Optional.of("TIE GAME");
            }
        }

        int counter = 0;
        for (int i = 0; i<(v==Variations.THIRD?3:2); i++) {
            String player = (String) Table.getVisibleCards().keySet().toArray()[i];
            if (Table.getVisibleCards().get(player).peek() != null){
                if (Table.getVisibleCards().get(player).peek().getValue() == max.getValue())
                    counter++;
                if (counter > 1) {
                    builder.append("***WAR***\n");
                    if (!Table.war()) {
                        return Optional.of(checkGameStatus(v).get());
                    } else break;
                }
            }
        }
        if (playerName.equals("NOBODY")) return Optional.of("TIE GAME");
        builder.append(playerName).append(" wins the round!\n");

        Table.collectCards(playerName);
        Iterator<String> names = Table.getVisibleCards().keySet().iterator();
        for (int i = 0; names.hasNext(); i++) {
            String name = names.next();
            builder.append(name).append(" has a score of ")
                    .append(v == Variations.FIRST ? Table.getCoverStacks().get(i).size() : Table.getPlayerPoints().get(name).size())
                    .append("\n");
        }


        return Optional.of(builder.toString());
    }

    public static void collectCards(String winner) {
        Table.collectCards(winner);
    }
}
