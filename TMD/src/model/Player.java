package model;
import java.awt.Rectangle;

public class Player {
    private int x, y, width = 40, height = 40;
    public Player(int x, int y) { this.x = x; this.y = y; }
    public void move(int dx, int dy) { x += dx; y += dy; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public Rectangle getBounds() { return new Rectangle(x, y, width, height); }
}