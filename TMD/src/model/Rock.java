package model;

import java.awt.Rectangle;

public class Rock {
    private int x, y, width = 150, height = 150;

    public Rock(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}