package com.almasb.battleship;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javafx.geometry.Point2D;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.media.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import java.io.File;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.Stack;

import javafx.animation.*;


import com.almasb.battleship.Board.Cell;

public class BattleshipMain extends Application {
    @FXML
    private static Stage window;
    //MUSIC---------------------------------------------------------------------------------------------------------------------------------------------------------
    private static MediaPlayer mediaPlayer; //we have to declare mediaPlayer static to run the whole SOUND FILE, if not it will stop when finish the methods inside
    private static final String MENU_SOUND = "sound/menuSound1.mp3";
    private static final String GAME_SOUND = "sound/GameSound.mp3";
    //Scene---------------------------------------------------------------------------------------------------------------------------------------------------------
    private static boolean resume = false;
    public static Scene menuScene, inGameScene, scoreScene, resultScene;
    //Main menu button----------------------------------------------------------------------------------------------------------------------------------------------
    @FXML
    private Button btnNewGame, btnSound, btnExit, btnResume, btnScore;
    //In game button------------------------------------------------------------------------------------------------------------------------------------------------
    private Button btnBack_inGame;
    private String playerName;
    //Game Attribute------------------------------------------------------------------------------------------------------------------------------------------------
    private boolean running = false;
    private Board enemyBoard, playerBoard;
    private int shipsToPlace = 5;
    private int[] shipList = {2, 3, 3, 4, 5};
    private boolean enemyTurn = false;
    private Random random = new Random();
    public static String strResult;
    public static ArrayList<Shot> history = new ArrayList<> ();
//    public static ArrayList<String> stringHistory = new ArrayList<> ();
    private Stack<Point2D> adjacentPoints = new Stack<Point2D>();
    private boolean mark = false;



    //Menu Scene initialization
    private Parent menuScene(){
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("battleship.fxml"));
            root.setId("pane");
        } catch (IOException e) {
            e.printStackTrace();
        }

        playMusic(MENU_SOUND);
        return root;
    }

    private Parent scoreScene(){
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("scoreBoard.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return root;
    }


    //In Game Scene initialization----------------------------------------------------------------------------------------------------------------------------------
    private Parent createContent() { //inGameScene
        shipsToPlace = 5;
        history.clear();

        //stop Menu sound when enter the game
        if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            mediaPlayer.stop();
        }

        //Create a borderpane
        BorderPane root = new BorderPane();
        root.setPrefSize(800, 800);


        //btn_Back do
        btnBack_inGame = new Button("Back");
        btnBack_inGame.setOnAction(e -> {
            window.setScene(menuScene); //back to menuScene
            resume = true;
            btnResume.setVisible(true); //appear resume button
        });
        //Text : enemy board
        Text enemy = new Text();
        enemy.setText("Enemy Board");
        //Text : your board
        Text yours = new Text();
        yours.setText("Your Board");

        enemyBoard = new Board(true, event -> {
            if (!running)
                return;

            Cell cell = (Cell) event.getSource();
            if (cell.wasShot)
                return;

            enemyTurn = !cell.shoot();
            addHistoryShot(cell, !enemyTurn);


            if (enemyBoard.ships == 0) {
                strResult = "YOU WIN";
                playerName = null;
                playerName = ConfirmBox.display("YOU WIN", "Enter you name");
//                printHistory(history);
                switchToHistoryScene();
                running = false;
                resume = false;
                btnResume.setVisible(false);

                System.out.println("YOU WIN");
//                System.out.println("Player hits : " + enemyBoard.getPlayerHit());
//                System.out.println("Moves : " + enemyBoard.getMove());
//                playerBoard.setScore((int)((float)enemyBoard.getPlayerHit() / enemyBoard.getMove() * 100 ));
//                System.out.println("SCORE : " + playerBoard.getScore());
//                System.exit(0);
            }

            if (enemyTurn)
                enemyMove();
        });

        playerBoard = new Board(false, event -> {
            if (running)
                return;

            Cell cell = (Cell) event.getSource();
            if (playerBoard.placeShip(new Ship(shipList[shipsToPlace - 1], event.getButton() == MouseButton.PRIMARY), cell.x, cell.y)) {
                if (--shipsToPlace == 0) {
                    startGame();
                }
            }
        });

        //The box part contains the boards
        VBox vbox = new VBox(30, enemy, enemyBoard, yours, playerBoard);
        vbox.setAlignment(Pos.CENTER);

        root.setCenter(vbox);
        root.setBottom(btnBack_inGame);

        return root;
    }

    private void getAdjacentPoint(int x, int y){
        Point2D[] points = new Point2D[] {
                new Point2D(x - 1, y),
                new Point2D(x + 1, y),
                new Point2D(x, y - 1),
                new Point2D(x, y + 1)
        };
        for (Point2D p : points){
            if(isValidPoint(p.getX(),p.getY())){
                adjacentPoints.push(p);
            }
        }
    }

    private void getAdjacentByDirection(int x, int y, boolean vertical){
        Point2D[] points ;
        if(vertical){
            points = new Point2D[] {
                    new Point2D(x, y - 1),
                    new Point2D(x, y + 1)
            };
        }
        else{
            points = new Point2D[] {
                    new Point2D(x - 1, y),
                    new Point2D(x + 1, y),
            };
        }
        for (Point2D p : points){
            if(isValidPoint(p.getX(),p.getY())){
                adjacentPoints.push(p);
            }
        }
    }


    private void enemyMove() {
        while (enemyTurn) {
            int x, y;
            if(adjacentPoints.empty()) {
                x =  random.nextInt(10);
                y =  random.nextInt(10);
            }
            else {
                System.out.println("STACK SIZE : " + adjacentPoints.size());
                Point2D p = adjacentPoints.pop();
                x = (int) p.getX();
                y = (int) p.getY();
            }
            Cell cell = playerBoard.getCell(x,y);
            if(cell.wasShot) continue;
            mark = cell.shoot();
            if(mark){
                if(!cell.ship.isAlive()) {
                    while(!adjacentPoints.empty()) adjacentPoints.pop();
                    continue;
                }
                if(cell.ship.getHealth() < cell.ship.type - 1){
                    getAdjacentByDirection(x,y,cell.ship.vertical);
                }
                else getAdjacentPoint(x,y);
            }
            enemyTurn = mark;

            if (playerBoard.ships == 0) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                strResult = "YOU LOSE";
//                System.out.println("YOU LOSE");
//                System.out.println("Player hits : " + enemyBoard.getPlayerHit());
//                System.out.println("Moves : " + enemyBoard.getMove());
                switchToHistoryScene();
                running = false;
                resume = false;
                btnResume.setVisible(false);
//                System.exit(0);
            }
        }
    }


    public void addHistoryShot(Cell cell, boolean accurate) {
        Shot mShot = new Shot(cell.x, cell.y, accurate );
        history.add(mShot);
    }

//    public void printHistory(ArrayList<Shot> history) {
//        int i = 1;
//        for (Shot his : history) {
//            String accurateStr;
//            if (his.isAccurate()) {
//                accurateStr = "hit";
//            } else {
//                accurateStr = "miss";
//            }
//            System.out.println(String.format("%2d. (x, y) : (%d, %d) : %s", i, his.getX(), his.getY(), accurateStr));
//            i++;
//        }
//    }



    private void startGame() {
        // place enemy ships
        int type = 5;

        while (type > 0) {
            int x = random.nextInt(10);
            int y = random.nextInt(10);

            if (enemyBoard.placeShip(new Ship(shipList[type - 1], Math.random() < 0.5), x, y)) {
                type--;
            }
        }

        running = true;
    }

    private Point2D getRandom(int x, int y) {
        ArrayList<Point2D> ckPoints = new ArrayList<Point2D>();
        Point2D[] points = new Point2D[] {
                new Point2D(x - 1, y),
                new Point2D(x + 1, y),
                new Point2D(x, y - 1),
                new Point2D(x, y + 1)
        };
        for (Point2D p : points){
            if(isValidPoint(p.getX(),p.getY())){
                ckPoints.add(p);
            }
        }
        int rnd = new Random().nextInt(ckPoints.size());
        Point2D p = ckPoints.get(rnd);
        return p;
    }
    boolean isValidPoint(double x, double y) {
        return x >= 0 && x < 10 && y >= 0 && y < 10;
    }

    //MUSIC PLAYER
    public void playMusic(String sound_path) {
        //MUST Include javafx.graphics,javafx.media to --add-modules to play media
            //mediaPlayer.dispose();
            Media menuMusic = new Media(new File(sound_path).toURI().toString());
            mediaPlayer = new MediaPlayer(menuMusic);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); //loop
            mediaPlayer.play();
    }

    private void switchToHistoryScene() {
        // calculate player's score
        if (strResult == "YOU WIN") {
            playerBoard.setScore((int)((float)enemyBoard.getPlayerHit() / enemyBoard.getMove() * 100 ));

            // if player enter a name -> check if he reaches a high score -> save to file Result
            if (playerName != null) {
                ArrayList<WinnerData> winnerList = new ArrayList<>();
                winnerList = ScoreBoard_IO.Winner_Data();
                winnerList = ScoreBoard_IO.InputWinner(playerName, playerBoard.getScore(),winnerList);
                ScoreBoard_IO.InputDataToFile(winnerList);
            }
        } else {
            playerBoard.setScore(0);
        }
        ResultBoard.transferHistory(history);
        ResultBoard.transferStat(strResult,enemyBoard.getMove(), enemyBoard.getPlayerHit(), playerBoard.getScore());
        try {
            Parent mRoot = FXMLLoader.load(getClass().getResource("result_board.fxml"));
            resultScene = new Scene(mRoot);
            window.setScene(resultScene);

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Player hits : " + enemyBoard.getPlayerHit());
        System.out.println("Moves : " + enemyBoard.getMove());
        System.out.println("SCORE : " + playerBoard.getScore());


    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage; //put the name of the stage window is easier to understand
        //menuScene
        menuScene = new Scene(menuScene());
        menuScene.getStylesheets().addAll(this.getClass().getResource("style.css").toExternalForm());
        //inGameScene
        inGameScene = new Scene(createContent());


        window.setScene(menuScene);
        window.show();

    }

    //Buttons created in an FXML file have to be used in a method
    @FXML
    private void handleButtonAction(ActionEvent event) throws Exception {
        if (event.getSource() == btnNewGame) {
            mediaPlayer.dispose();
            playMusic(GAME_SOUND);
            running = false;
            inGameScene = new Scene(createContent());
            window = (Stage) btnNewGame.getScene().getWindow(); //get the window which contains the button btnNewGame
            window.setTitle("Battleship");
            window.setScene(inGameScene);
            window.setResizable(false);
            window.show();
        }
        if (event.getSource() == btnResume && resume == true) {
            window = (Stage) btnResume.getScene().getWindow(); //get the window which contains the button btnNewGame
            window.setTitle("Battleship");
            window.setScene(inGameScene);
            window.setResizable(false);
        }
        if (event.getSource() == btnSound) {
            if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                mediaPlayer.stop();
            } else if (mediaPlayer.getStatus() == MediaPlayer.Status.STOPPED) {
                mediaPlayer.play();
            }
        }
        if (event.getSource() == btnScore) {
            //scoreboardScene
            scoreScene = new Scene(scoreScene());
            window = (Stage) btnScore.getScene().getWindow(); //get the window which contains the button btnSound
            window.setTitle("Score Board");
            window.setScene(scoreScene);
        }
        if (event.getSource() == btnExit) {
            window = (Stage) btnExit.getScene().getWindow();
            window.close();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
