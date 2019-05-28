package com.almasb.battleship;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class ScoreBoard_IO {
    public static void GenerateDataFile() {
        try {
            File file = new File("Result.txt");
            if (file.createNewFile()) {
                System.out.println("New File is created!");
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static ArrayList Winner_Data() {
        ArrayList<WinnerData> list = new ArrayList<>();
        GenerateDataFile();
        File file = new File("Result.txt");
        try(Scanner sc = new Scanner(file)) {
            while(sc.hasNext()){
                WinnerData wnDT = new WinnerData();
                wnDT.setName(sc.nextLine());
                wnDT.setScore(Integer.parseInt(sc.nextLine()));
                list.add(wnDT);
            }
        } catch (Exception e) {
            System.out.println("got an exception!");
        }
        return list;
    }

    public static ArrayList GenerateArray(){
        ArrayList<WinnerData> list = Winner_Data();
        return list;
    }

    public static ArrayList InputWinner(String name, int score, ArrayList<WinnerData> list) {

        WinnerData wtnData = new WinnerData(name,score);

        list.add(wtnData);

        Collections.sort(list, new ScoreComparator());
        return list;

    }

    public static void InputDataToFile(ArrayList<WinnerData> tempList){
        try (PrintWriter pw = new PrintWriter("Result.txt")) {
            for (int i = 0; i < 5; i++){
                pw.println(tempList.get(i).getName());
                pw.println(tempList.get(i).getScore());

            }
        } catch (Exception e) {
            System.out.println("got an exception!");
        }
    }

    public static void clearResultFile() {
        try {
            PrintWriter pw = new PrintWriter("Result.txt");
            pw.close();
        } catch (Exception e) {
            System.out.println("got an exception!");
        }
    }


}

class ScoreComparator implements Comparator<WinnerData> {

    @Override
    public int compare(WinnerData o1, WinnerData o2) {
        return o2.getScore() - o1.getScore();
    }

}
