package model;

import java.awt.Rectangle;
import java.util.List;

public class Alien {
    private int x, y;
    private int speed = 3;
    private int width = 30, height = 30;
    private boolean alive = true;
    private long lastShotTime = 0;

    public Alien(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void update(List<Bullet> enemyBullets) {
        y += speed; // Gerak alien turun

        long currentTime = System.currentTimeMillis();
        // Cek jeda tembak dan peluang random
        if (currentTime - lastShotTime > 2000 && Math.random() < 0.01) {
            // SOLUSI: Tambahkan 4 parameter (x, y, speed, isEnemy)
            // Peluru muncul di x alien, y alien + 30, speed 5, true (peluru musuh)
            enemyBullets.add(new Bullet(x + 12, y + 30, 5, true));
            lastShotTime = currentTime;
        }

        if (y > 600) alive = false;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public boolean isAlive() { return alive; }
    public void die() { this.alive = false; }
}