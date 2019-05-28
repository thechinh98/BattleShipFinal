package com.almasb.battleship;

import java.util.ArrayList;
import java.util.List;

import javafx.event.EventHandler;
import java.io.File;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.*;
import javafx.scene.image.*;
import javafx.scene.shape.Rectangle;

public class Board extends Parent {
    private VBox rows = new VBox();
    private boolean enemy = false;
    public int ships = 5;
    private int playerHit = 0;
    private int move = 0;
    private int Score ;

    public int getScore() {
        return Score;
    }

    public void setScore(int score) {
        Score = score;
    }

    public  int getMove() {
        return move;
    }

    public  int getPlayerHit() {
        return playerHit;
    }

    public Board(boolean enemy, EventHandler<? super MouseEvent> handler) {
        this.enemy = enemy;
        for (int y = 0; y < 10; y++) {
            HBox row = new HBox();
            for (int x = 0; x < 10; x++) {
                Cell c = new Cell(x, y, this);
                c.setOnMouseClicked(handler);
                row.getChildren().add(c);
            }

            rows.getChildren().add(row);
        }

        getChildren().add(rows);
    }

    public boolean placeShip(Ship ship, int x, int y) {
        if (canPlaceShip(ship, x, y)) {
            int length = ship.type;

            if (ship.vertical) {
                for (int i = y; i < y + length; i++) {
                    Cell cell = getCell(x, i);
                    cell.ship = ship;
                    if (!enemy) {
//                        cell.setFill(Color.WHITE);
//                        cell.setStroke(Color.GREEN);
                        if (i == y) {
                            Image img = new Image(new File("img/ship_head.png").toURI().toString());
                            cell.setFill(new ImagePattern(img));
                        } else if (i == y + length - 1) {
                            Image img = new Image(new File("img/ship_tail.png").toURI().toString());
                            cell.setFill(new ImagePattern(img));
                        } else {
                            Image img = new Image(new File("img/ship_body.png").toURI().toString());
                            cell.setFill(new ImagePattern(img));
                        }
                    }
//                  }
                }
            }
            else {
                for (int i = x; i < x + length; i++) {
                    Cell cell = getCell(i, y);
                    cell.ship = ship;
                       if (!enemy) {
                        if (i == x) {
                            Image img = new Image(new File("img/ship_head_rotate.png").toURI().toString());
                            cell.setFill(new ImagePattern(img));
                        } else if (i == x + length - 1) {
                            Image img = new Image(new File("img/ship_tail_rotate.png").toURI().toString());
                            cell.setFill(new ImagePattern(img));
                        } else {
                            Image img = new Image(new File("img/ship_body_rotate.png").toURI().toString());
                            cell.setFill(new ImagePattern(img));
                        }
                       }
                }
            }

            return true;
        }

        return false;
    }

    public Cell getCell(int x, int y) {
        return (Cell)((HBox)rows.getChildren().get(y)).getChildren().get(x);
    }

    private Cell[] getNeighbors(int x, int y) {
        Point2D[] points = new Point2D[] {
                new Point2D(x - 1, y),
                new Point2D(x + 1, y),
                new Point2D(x, y - 1),
                new Point2D(x, y + 1)
        };

        List<Cell> neighbors = new ArrayList<Cell>();

        for (Point2D p : points) {
            if (isValidPoint(p)) {
                neighbors.add(getCell((int)p.getX(), (int)p.getY()));
            }
        }

        return neighbors.toArray(new Cell[0]);
    }

    private boolean canPlaceShip(Ship ship, int x, int y) {
        int length = ship.type;

        if (ship.vertical) {
            for (int i = y; i < y + length; i++) {
                if (!isValidPoint(x, i))
                    return false;

                Cell cell = getCell(x, i);
                if (cell.ship != null)
                    return false;

                for (Cell neighbor : getNeighbors(x, i)) {
                    if (!isValidPoint(x, i))
                        return false;

                    if (neighbor.ship != null)
                        return false;
                }
            }
        }
        else {
            for (int i = x; i < x + length; i++) {
                if (!isValidPoint(i, y))
                    return false;

                Cell cell = getCell(i, y);
                if (cell.ship != null)
                    return false;

                for (Cell neighbor : getNeighbors(i, y)) {
                    if (!isValidPoint(i, y))
                        return false;

                    if (neighbor.ship != null)
                        return false;
                }
            }
        }

        return true;
    }

    private boolean isValidPoint(Point2D point) {
        return isValidPoint(point.getX(), point.getY());
    }

    private boolean isValidPoint(double x, double y) {
        return x >= 0 && x < 10 && y >= 0 && y < 10;
    }

    public class Cell extends Rectangle {
        public int x, y;
        public Ship ship = null;
        public boolean wasShot = false;
        public Image image;

        private Board board;

        public Cell(int x, int y, Board board) {
            super(30, 30);
            this.x = x;
            this.y = y;
            this.board = board;
            image = new Image(new File("img/cell.png").toURI().toString());
            setFill(new ImagePattern(image));
//            setStroke(Color.rgb(114, 202, 211));
        }

        public boolean shoot() {
            wasShot = true;

            image = new Image(new File("img/missed.png").toURI().toString());
            setFill(new ImagePattern(image));

            if (ship != null) {
                ship.hit();
                if(board.enemy) {
                    board.playerHit++;
                    board.move++;
                }
                image = new Image(new File("img/explode.png").toURI().toString());
                setFill(new ImagePattern(image));
                if (!ship.isAlive()) {
                    board.ships--;
                }

                return true;
            }
            if(board.enemy) {
                board.move++;
            }
            return false;
        }
    }
}