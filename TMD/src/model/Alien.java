package model;

import java.awt.Rectangle;

public class Alien {
    private int x, y;
    private int width = 30, height = 30;
    private boolean alive = true;

    public Alien(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setX(int x) { this.x = x; }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public boolean isAlive() { return alive; }
}