package model;
import java.awt.Rectangle;

public class Player {
    private int x, y, width = 40, height = 40;

    public Player(int x, int y) { this.x = x; this.y = y; }

    public void move(int dx, int dy) {
        int nextX = x + dx;
        int nextY = y + dy;

        if (nextX >= 0 && nextX <= 820 - width) {
            x = nextX;
        }

        if (nextY >= 0 && nextY <= 640 - height) {
            y = nextY;
        }
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public Rectangle getBounds() { return new Rectangle(x, y, width, height); }
}