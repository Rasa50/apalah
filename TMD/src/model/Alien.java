package model;

import java.awt.Rectangle;

public class Alien {
    private int x, y;
    private int width = 30, height = 30;
    private int speed = 2; // Kecepatan gerak alien
    private int direction = 1; // 1 untuk kanan, -1 untuk kiri

    public Alien(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Method baru untuk menggerakkan alien
    public void update() {
        x += (speed * direction);

        // Jika menabrak batas kanan (800) atau kiri (0), balik arah
        if (x > 750 || x < 10) {
            direction *= -1;
        }
    }

    public void setX(int x) { this.x = x; }
    public int getX() { return x; }
    public int getY() { return y; }
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}