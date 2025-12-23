package model;

import java.awt.Rectangle;

public class Bullet {
    private int x, y;
    private int speed;
    private int width = 8, height = 8;
    private boolean active = true;
    private boolean isEnemy;

    public Bullet(int x, int y, int speed, boolean isEnemy) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.isEnemy = isEnemy;
    }

    public void update() {
        y += speed;
        // Perbaikan: Peluru mati jika keluar batas bawah (600) atau batas atas (-10)
        if (y > 600 || y < -10) active = false;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public boolean isActive() { return active; }
    public void setInactive() { this.active = false; }
}