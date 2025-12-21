package view;

import presenter.GamePresenter;
import util.KeyHandler;
import model.Player;
import model.Alien;
import model.Bullet;
import javax.swing.*;
import java.awt.*;

public class GameView extends JPanel {
    private GamePresenter presenter;
    private MainFrame frame;
    private KeyHandler keyHandler;

    public GameView(MainFrame frame) {
        this.frame = frame;
        setFocusable(true);
        setBackground(Color.BLACK);
        keyHandler = new KeyHandler();
        addKeyListener(keyHandler);

        presenter = new GamePresenter(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (presenter != null) {
            drawGame(g);
        }
    }

    private void drawGame(Graphics g) {
        // Draw Player (Blue Square)
        Player p = presenter.getPlayer();
        g.setColor(Color.CYAN);
        g.fillRect(p.getX(), p.getY(), p.getWidth(), p.getHeight());

        // Draw Aliens
        g.setColor(Color.RED);
        for (Alien a : presenter.getAliens()) {
            g.fillOval(a.getX(), a.getY(), 30, 30);
        }

        // Draw Bullets
        g.setColor(Color.YELLOW);
        for (Bullet b : presenter.getBullets()) {
            g.fillRect(b.getX(), b.getY(), 5, 10);
        }

        // Draw UI
        g.setColor(Color.WHITE);
        g.drawString("Score: " + presenter.getScore(), 20, 20);
        g.drawString("Ammo: " + presenter.getAmmo(), 20, 40);

        if(presenter.isGameOver()) {
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.drawString("GAME OVER", 300, 300);
        }
    }

    public void backToMenu() { frame.showView("MENU"); }
    public KeyHandler getKeyHandler() { return keyHandler; }
}