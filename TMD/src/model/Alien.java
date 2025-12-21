package model;

import java.awt.Rectangle;

public class Alien {
    private int x, y;
    private int speed = 3;
    private int width = 30, height = 30;
    private boolean alive = true;

    public Alien(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void update() {
        y += speed; // Alien turun ke bawah
        if (y > 600) alive = false; // Hilang jika lewat batas bawah
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    // Getters & Setters
    public int getX() { return x; }
    public int getY() { return y; }
    public boolean isAlive() { return alive; }
    public void die() { this.alive = false; }
}