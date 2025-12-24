package model;

import java.awt.Rectangle;

public class Bullet {
    private int x, y;
    private double speedX, speedY;
    private int width = 8, height = 8;
    private boolean active = true;
    private boolean isEnemy;

    public Bullet(int x, int y, double speedX, double speedY, boolean isEnemy) {
        this.x = x;
        this.y = y;
        this.speedX = speedX;
        this.speedY = speedY;
        this.isEnemy = isEnemy;
    }

    public void update() {
        x += speedX;
        y += speedY;
        if (y > 600 || y < -10 || x < -10 || x > 810) active = false;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public boolean isActive() { return active; }
    public void setInactive() { this.active = false; }
}