package com.almasb.battleship;

public class WinnerData {
    String Name;
    int Score;

    public WinnerData() {
    }

    public WinnerData(String name, int score) {
        Name = name;
        Score = score;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getScore() {
        return Score;
    }

    public void setScore(int score) {
        Score = score;
    }
}
