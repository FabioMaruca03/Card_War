package com.marufeb;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Game extends Application {
    public static List<String> players = new ArrayList<>(); // Players list, automatically fetched by the controller

    public static void main(String[] args) {
        players.add("Dealer");
        players.add("Player 1");
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent p = FXMLLoader.load(getClass().getResource("/com/marufeb/View.fxml"));
        stage.setScene(new Scene(p));
        stage.setTitle("War Game");
        stage.show();
    }
}
