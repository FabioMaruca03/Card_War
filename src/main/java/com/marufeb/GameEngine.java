package com.marufeb;

import com.marufeb.model.*;
import com.marufeb.model.Stack;

import java.util.*;

public class GameEngine {
    private static final Stack STACK = new Stack(52);
    private static int max_moves;
    private static List<String> players;
    private static Variations v;

    public static void init(int moves, List<String> players, Variations variation) {
        //-----| SETTINGS |-----//
        GameEngine.max_moves = moves; GameEngine.players = players; v = variation;

        //-----| TABLE INIT |-----//
        Table.init(players, variation);

        //-----| DEALER SECTION |-----//
        Dealer.shuffle(STACK.getCards());
        Dealer.giveCards(Table.getCoverStacks(), STACK);

    }

    public static String play() {
        int moves = 0;
        StringBuilder builder = new StringBuilder();
        System.out.println("Playing");

        while (Dealer.checkWin(v).isEmpty() && max_moves > moves){
            Table.showCard();
            Optional<String> gameStatus = Dealer.checkGameStatus(v);
            System.out.println("Moves: "+moves);
            gameStatus.ifPresent(builder::append);
            moves++;
        }

        Dealer.checkWin(v).ifPresentOrElse(builder::append, () -> builder.append(Table.establishWinner().get()) );

        return builder.toString();
    }
}
