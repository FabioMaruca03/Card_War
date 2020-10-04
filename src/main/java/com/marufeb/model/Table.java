package com.marufeb.model;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class Table {
    private static final Map<String, Queue<Card>> visibleCards = new HashMap<>();
    private static final Map<String, Queue<Card>> playerPoints = new HashMap<>();
    private static final Queue<Card> war = new ArrayDeque<>();
    private static final List<Queue<Card>> coverStacks = new ArrayList<>();
    private static Variations variation;

    private static List<String> players;

    public static void init(List<String> players, Variations v) {
        Table.players = players; Table.variation = v;
        war.clear();
        coverStacks.clear();
        visibleCards.clear();
        playerPoints.clear();
        players.forEach(it->coverStacks.add(new ArrayDeque<>()));
        players.forEach(it->visibleCards.put(it, new ArrayDeque<>()));
        players.forEach(it->playerPoints.put(it, new ArrayDeque<>()));
    }

    /**
     * The dealer <b>shows</b> the card for each player that is playing
     * @return True when the game is not end if else returns false.
     */
    public static boolean showCard() {
        for (int i = 0; i < (variation == Variations.THIRD? 3 : 2); i++) {
            if (coverStacks.get(i).peek() != null) {
                visibleCards.get(players.get(i)).add(coverStacks.get(i).poll());
            } else return false;
        }
        return true;
    }

    /**
     * Makes the actual <b>war</b>
     * @return True when the war card placing is done. False if there's a winner
     */
    static boolean war() {
        for (String player : players) {
            if (showCard()) {
                war.addAll(visibleCards.get(player));
                visibleCards.get(player).clear();
            } else return false;
            if (!showCard())
                return false;
        }
        return true;
    }

    /**
     * Based on who is the winner, the dealer <b>collects</b> every card.
     * <ol>
     *     <li>The current game variation is <i>Variations.FIRST</i> the cards are collected in the points stack</li>
     *     <li>The current game variation is <i>Variations.SECOND</i> or <i>Variations.THIRD</i>
     *     the cards are placed at the bottom of the players current stack </li>
     * </ol>
     * @param winner Name of the current winner player
     */
    static void collectCards(String winner) {
        if (!players.contains(winner)) throw new IllegalArgumentException("Player: "+winner+" not found");

        for (int i = 0; i < (variation == Variations.THIRD? 3 : 2); i++) {
            //-----| Moves cards to points stack [ Second and Third Variation ]
            while (visibleCards.get(players.get(i)).peek() != null && variation != Variations.FIRST) {
                playerPoints.get(winner).addAll(visibleCards.get(players.get(i)));
                visibleCards.get(players.get(i)).clear();

                playerPoints.get(players.get(i)).addAll(war);
                war.clear();
            }

            //-----| Moves cards to the bottom of the cover stack of the winner player
            while (visibleCards.get(players.get(i)).peek() != null && variation == Variations.FIRST) {
                coverStacks.get(players.indexOf(winner)).addAll(visibleCards.get(players.get(i)));
                visibleCards.get(players.get(i)).clear();

                coverStacks.get(players.indexOf(winner)).addAll(war);
                war.clear();
            }

        }
    }

    public static Optional<String> establishWinner() {
        long max = coverStacks.stream().mapToLong(it-> (long) it.size()).summaryStatistics().getMax();
        String winnerName = null;
        if (variation == Variations.FIRST) {
            if (coverStacks.stream().filter(it->it.size() == max).count() != 1) {
                return Optional.of("TIE GAME");
            }
            for (int i = 0; i < players.size(); i++)
                if (coverStacks.get(i).size() == max) return Optional.of(players.get(i));
        }


        return Optional.of("TIE GAME");
    }

    //-----| GETTERS & SETTERS |-----//

    public static List<Queue<Card>> getCoverStacks() { return coverStacks; }

    public static Map<String, Queue<Card>> getVisibleCards() { return visibleCards; }

    public static Map<String, Queue<Card>> getPlayerPoints() { return playerPoints; }

    public static void setVariation(Variations variation) { Table.variation = variation; }

    public static Variations getVariation() { return variation; }

    public static void setCoverStacks(List<Queue<Card>> cover, List<String> players) {
        coverStacks.clear();
        coverStacks.addAll(cover);

        visibleCards.clear();
        players.forEach(it->visibleCards.put(it, new ArrayDeque<>()));
    }

}
