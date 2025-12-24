package util;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyHandler extends KeyAdapter {
    // Tambahkan variabel 'space' di sini
    public boolean up, down, left, right, pause, esc, space;
    public boolean isShooting;

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_UP || code == KeyEvent.VK_W) up = true;
        if (code == KeyEvent.VK_DOWN || code == KeyEvent.VK_S) down = true;
        if (code == KeyEvent.VK_LEFT || code == KeyEvent.VK_A) left = true;
        if (code == KeyEvent.VK_RIGHT || code == KeyEvent.VK_D) right = true;

        if (code == KeyEvent.VK_SPACE) {
            space = true; // Set true agar bisa dideteksi oleh GamePresenter
            if (!pause) isShooting = true;
            pause = true;
        }

        if (code == KeyEvent.VK_ESCAPE) esc = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_UP || code == KeyEvent.VK_W) up = false;
        if (code == KeyEvent.VK_DOWN || code == KeyEvent.VK_S) down = false;
        if (code == KeyEvent.VK_LEFT || code == KeyEvent.VK_A) left = false;
        if (code == KeyEvent.VK_RIGHT || code == KeyEvent.VK_D) right = false;

        if (code == KeyEvent.VK_SPACE) {
            space = false; // Reset saat tombol dilepas
            pause = false;
            isShooting = false;
        }

        if (code == KeyEvent.VK_ESCAPE) esc = false;
    }
}