package com.almasb.battleship;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ResultBoard implements Initializable {
    private static final int ITEM_FONT_SIZE = 14;
    public static String battleResult;
    public static int totalShots, hit, miss, totalScore;
    private ArrayList<WinnerData> winnerList = new ArrayList<>();

    public static void transferStat(String result, int tShots, int nHit, int tScore) {
        battleResult = result;
        totalShots = tShots;
        hit = nHit;
        miss = tShots - nHit;
        totalScore = tScore;
    }

    public static ArrayList<String> stringHistory = new ArrayList<>();
    public static void transferHistory( ArrayList<Shot> historyList) {
        stringHistory.clear();
        int numLoops = historyList.size();
        for (int i=1; i<=numLoops; i++) {
            stringHistory.add(String.format("%3d. %s",i,historyList.get(i-1).toString()));
        }
    }


    @FXML
    private ListView myListView;
    protected ListProperty<String> listProperty = new SimpleListProperty<>();

    @FXML
    private Label game_result = new Label();
//    StringProperty strProResult = new SimpleStringProperty();

    @FXML
    private GridPane gridPane;

    @FXML
    private GridPane gpHighScore;

    @FXML
    Button btnMainMenu;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        listProperty.set(FXCollections.observableArrayList(stringHistory));
        myListView.itemsProperty().bind(listProperty);
        myListView.setCellFactory(cell -> {
            return new ListCell<String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null) {
                        setText(item);
                        setTextFill(item.contains("hit") ? Color.GREEN: Color.RED);
                        // decide to add a new styleClass
                        // getStyleClass().add("costume style");
                        // decide the new font size
                        setFont(Font.font(20));
                    }
                }
            };
        });

//        strProResult.setValue(BattleshipMain.strResult);
//        game_result.textProperty().bind(strProResult);
        game_result.setText(battleResult);
        game_result.setTextFill((battleResult.contains("WIN") ? Color.GREEN: Color.RED));

        // print the statistics
        gridPane.add(new Label("Total shots\t:"), 0,0);
        gridPane.add(new Label("Hit\t\t\t:"), 0,1);
        gridPane.add(new Label("Miss\t\t\t:"), 0,2);
        gridPane.add(new Label("Total score\t:"), 0,3);

        gridPane.add(new Label(String.valueOf(totalShots)), 1,0);
        gridPane.add(new Label(String.valueOf(hit)), 1,1);
        gridPane.add(new Label(String.valueOf(miss)), 1,2);
        gridPane.add(new Label(String.valueOf(totalScore)), 1,3);

        // print the high score table
        winnerList.clear();
        winnerList = ScoreBoard_IO.Winner_Data();

        int num_of_players = winnerList.size();

        for (int i=0; i<num_of_players; i++) {
            Label nameLabel = new Label(winnerList.get(i).getName());
            nameLabel.setFont(new Font(ITEM_FONT_SIZE));
            gpHighScore.add(nameLabel, 0,i);
            Label scoreLabel = new Label(String.valueOf(winnerList.get(i).getScore()));
            scoreLabel.setFont(new Font(ITEM_FONT_SIZE));
            gpHighScore.add(scoreLabel, 1,i);
        }

    }

    @FXML
    private void handleButtonMainMenu(ActionEvent event) throws Exception {
        if (event.getSource() == btnMainMenu) {
            Stage window = (Stage) btnMainMenu.getScene().getWindow(); //get the window which contains the button btnMainMenu
            window.setScene(BattleshipMain.menuScene);
        }
    }
}
