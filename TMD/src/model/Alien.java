// src/model/Alien.java
package model;

import java.awt.Rectangle;

public class Alien {
    private int x, y;
    private int width = 30, height = 30;
    private int speed = 2;
    private int direction = 1;

    public Alien(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void update() {
        // Contoh: Alien bergerak kanan-kiri di posisi bawah
        x += (speed * direction);
        if (x > 750 || x < 10) direction *= -1;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}