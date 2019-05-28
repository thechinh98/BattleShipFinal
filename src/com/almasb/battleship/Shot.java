package com.almasb.battleship;


public class Shot {
    private int x;
    private int y;
    private boolean accurate;
    private static final String EXPLO_SOUND = "sound/ExplosionSound.mp3";

    public Shot(int x, int y, boolean accurate) {
        super();
        this.x = x;
        this.y = y;
        this.accurate = accurate;
    }

    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }
    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }
    public boolean isAccurate() {
        return accurate;
    }
    public void setAccurate(boolean accurate) {
        this.accurate = accurate;
    }

    @Override
    public String toString() {
        String accurateStr;
        if (isAccurate()) {
            accurateStr = "hit";
        } else {
            accurateStr = "miss";
        }
        return (String.format("(x, y) : (%d, %d) : %s", this.getX(), this.getY(), accurateStr));
//        return "Shot{" +
//                "x=" + x +
//                ", y=" + y +
//                ", accurate=" + accurate +
//                '}';
    }
}
