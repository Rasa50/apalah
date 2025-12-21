package model;

import java.awt.Rectangle;

public class Bullet {
    private int x, y;
    private int speed;
    private int width = 5, height = 10;
    private boolean active = true;
    private boolean isEnemyBullet; // Flag untuk membedakan peluru

    // Constructor dengan 4 parameter
    public Bullet(int x, int y, int speed, boolean isEnemyBullet) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.isEnemyBullet = isEnemyBullet;
    }

    public void update() {
        if (isEnemyBullet) {
            y += speed; // Peluru musuh turun ke bawah
        } else {
            y -= speed; // Peluru pemain naik ke atas
        }

        // Nonaktif jika keluar layar
        if (y < 0 || y > 600) active = false;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public boolean isActive() { return active; }
    public void setInactive() { this.active = false; }
}