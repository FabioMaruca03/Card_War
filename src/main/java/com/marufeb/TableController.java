package com.marufeb;

import com.marufeb.model.Variations;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ResourceBundle;

import static com.marufeb.Game.players;

public class TableController implements Initializable {
    @FXML private TextArea area;
    @FXML private Button war;

    @FXML
    private void play(ActionEvent event) {
        war.setDisable(true);
        GameEngine.init(50, players, Variations.FIRST);
        area.setText(GameEngine.play());
        war.setDisable(false);
        event.consume();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        area.setText("");
    }
}
