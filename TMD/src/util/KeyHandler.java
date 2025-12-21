package util;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyHandler extends KeyAdapter {
    public boolean up, down, left, right, pause;

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_UP) up = true;
        if (code == KeyEvent.VK_DOWN) down = true;
        if (code == KeyEvent.VK_LEFT) left = true;
        if (code == KeyEvent.VK_RIGHT) right = true;
        if (code == KeyEvent.VK_SPACE) pause = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_UP) up = false;
        if (code == KeyEvent.VK_DOWN) down = false;
        if (code == KeyEvent.VK_LEFT) left = false;
        if (code == KeyEvent.VK_RIGHT) right = false;
        if (code == KeyEvent.VK_SPACE) pause = false;
    }
}