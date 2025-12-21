package model;

import java.awt.Rectangle;

public class Bullet {
    private int x, y;
    private int speed = 10;
    private int width = 5, height = 10;
    private boolean active = true;

    public Bullet(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void update() {
        y -= speed; // Peluru naik ke atas
        if (y < 0) active = false; // Nonaktif jika keluar layar
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    // Getters & Setters
    public int getX() { return x; }
    public int getY() { return y; }
    public boolean isActive() { return active; }
    public void setInactive() { this.active = false; }
}