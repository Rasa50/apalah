package view;

import presenter.GamePresenter;
import util.KeyHandler;
import model.*;
import javax.swing.*;
import java.awt.*;

public class GameView extends JPanel {
    private GamePresenter presenter;
    private MainFrame frame;
    private KeyHandler keyHandler;

    public GameView(MainFrame frame) {
        this.frame = frame;
        setFocusable(true);
        setBackground(new Color(10, 10, 40)); // Biru gelap luar angkasa
        keyHandler = new KeyHandler();
        addKeyListener(keyHandler);

        presenter = new GamePresenter(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (presenter != null) {
            drawGame(g2);
        }
    }

    private void drawGame(Graphics2D g) {
        // 1. Draw Player
        Player p = presenter.getPlayer();
        g.setColor(Color.CYAN);
        g.fillRoundRect(p.getX(), p.getY(), p.getWidth(), p.getHeight(), 10, 10);

        // 2. Draw Aliens
        g.setColor(Color.MAGENTA);
        for (Alien a : presenter.getAliens()) {
            g.fillOval(a.getX(), a.getY(), 30, 30);
            // Tambahkan "mata" agar alien terlihat lebih hidup
            g.setColor(Color.WHITE);
            g.fillOval(a.getX()+5, a.getY()+10, 5, 5);
            g.fillOval(a.getX()+20, a.getY()+10, 5, 5);
            g.setColor(Color.MAGENTA);
        }

        // 3. Draw Player Bullets (Warna Kuning)
        g.setColor(Color.YELLOW);
        for (Bullet b : presenter.getPlayerBullets()) {
            g.fillRect(b.getX(), b.getY(), 5, 12);
        }

        // 4. Draw Enemy Bullets (Warna Merah/Oranye - Efek Animasi)
        g.setColor(Color.ORANGE);
        for (Bullet b : presenter.getEnemyBullets()) {
            // Karena peluru musuh turun, kita gambar dengan arah terbalik
            // Kita asumsikan update peluru musuh ditangani di presenter
            g.fillOval(b.getX(), b.getY(), 8, 8);
        }

        // 5. Draw UI (HUD)
        g.setColor(Color.WHITE);
        g.setFont(new Font("Consolas", Font.BOLD, 16));
        g.drawString("SCORE: " + presenter.getScore(), 20, 30);
        g.drawString("AMMO : " + presenter.getAmmo(), 20, 55);

        if(presenter.isGameOver()) {
            drawGameOverScreen(g);
        }
    }

    private void drawGameOverScreen(Graphics2D g) {
        g.setColor(new Color(0, 0, 0, 150)); // Overlay transparan
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 50));
        String msg = "MISSION FAILED";
        FontMetrics metrics = g.getFontMetrics();
        int x = (getWidth() - metrics.stringWidth(msg)) / 2;
        g.drawString(msg, x, 300);

        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.setColor(Color.WHITE);
        g.drawString("Press ESC to Back to Menu", x + 50, 350);
    }

    public void backToMenu() { frame.showView("MENU"); }
    public KeyHandler getKeyHandler() { return keyHandler; }
}