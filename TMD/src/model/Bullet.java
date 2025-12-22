package model;

import java.awt.Rectangle;

public class Bullet {
    private int x, y;
    private int speed;
    private int width = 8, height = 8; // Peluru alien dibuat bulat kecil
    private boolean active = true;
    private boolean isEnemy;

    public Bullet(int x, int y, int speed, boolean isEnemy) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.isEnemy = isEnemy;
    }

    public void update() {
        y += speed; // Selalu turun karena hanya alien yang menembak
        if (y > 600) active = false;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public boolean isActive() { return active; }
    public void setInactive() { this.active = false; }
}