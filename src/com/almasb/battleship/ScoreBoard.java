package com.almasb.battleship;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ScoreBoard implements Initializable {
    private static final int ITEM_FONT_SIZE = 20;
    private ArrayList<WinnerData> winnerList = new ArrayList<>();

    @FXML
    private Label lbName;

    @FXML
    private Label lbScore;

    @FXML
    private GridPane scoreBoardGrid;

    @FXML
    private Button btnBack_ScoreBoard;

    @FXML
    private Button btnResetScore;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        lbName.setFont(Font.font("Verdana", FontWeight.BOLD, 22));
        lbScore.setFont(Font.font("Verdana", FontWeight.BOLD, 22));

        winnerList.clear();
        winnerList = ScoreBoard_IO.Winner_Data();

        int num_of_players = winnerList.size();

        for (int i=0; i<num_of_players; i++) {
            Label nameLabel = new Label(winnerList.get(i).getName());
            nameLabel.setFont(new Font(ITEM_FONT_SIZE));
            scoreBoardGrid.add(nameLabel, 0,i);
            Label scoreLabel = new Label(String.valueOf(winnerList.get(i).getScore()));
            scoreLabel.setFont(new Font(ITEM_FONT_SIZE));
            scoreBoardGrid.add(scoreLabel, 1,i);
        }
    }

    @FXML
    private void handleButtonActionScoreScene(ActionEvent event) throws Exception {
        if (event.getSource() == btnBack_ScoreBoard) {
            Stage window = (Stage) btnBack_ScoreBoard.getScene().getWindow(); //get the window which contains the button btnBack_scoreBoard
            window.setScene(BattleshipMain.menuScene);
        } else if (event.getSource() == btnResetScore){
            scoreBoardGrid.getChildren().clear();
            ScoreBoard_IO.clearResultFile();
        }
    }
}
