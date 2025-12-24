package view;

import presenter.GamePresenter;
import util.KeyHandler;
import model.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;

public class GameView extends JPanel {
    private GamePresenter presenter;
    private MainFrame frame;
    private BufferedImage imgBg, imgPlayer, imgAlien, imgRock, imgBullet;
    private Rectangle btnBackBounds;

    public GameView(MainFrame frame) {
        this.frame = frame;
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        setBackground(new Color(10, 10, 40));
        addKeyListener(frame.getKeyHandler());

        btnBackBounds = new Rectangle(300, 380, 200, 50);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (presenter != null && (presenter.isGameOver() || presenter.isVictory())) {
                    if (btnBackBounds.contains(e.getPoint())) {
                        backToMenu();
                    }
                }
            }
        });

        loadAssets();
    }

    private void loadAssets() {
        try {
            imgBg = ImageIO.read(new File("assets/Background.png"));
            imgPlayer = ImageIO.read(new File("assets/Player.png"));
            imgAlien = ImageIO.read(new File("assets/Alien.png"));
            imgRock = ImageIO.read(new File("assets/Rock.png"));
            imgBullet = ImageIO.read(new File("assets/Bullet.png"));
        } catch (Exception e) {
            System.out.println("Peringatan: Assets gambar tidak ditemukan.");
        }
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
        if (imgBg != null) {
            g.drawImage(imgBg, 0, 0, getWidth(), getHeight(), null);
        }

        for (Rock r : presenter.getRocks()) {
            if (imgRock != null) g.drawImage(imgRock, r.getX(), r.getY(), 150, 150, null);
        }

        Player p = presenter.getPlayer();
        if (presenter.isPlayerHidden()) {
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        }

        if (imgPlayer != null) g.drawImage(imgPlayer, p.getX(), p.getY(), p.getWidth(), p.getHeight(), null);

        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

        for (Alien a : presenter.getAliens()) {
            if (imgAlien != null) g.drawImage(imgAlien, a.getX(), a.getY(), 50, 50, null);
        }

        g.setColor(Color.ORANGE);
        for (Bullet b : presenter.getEnemyBullets()) g.fillOval(b.getX(), b.getY(), 8, 8);

        for (Bullet b : presenter.getPlayerBullets()) {
            if (imgBullet != null) g.drawImage(imgBullet, b.getX(), b.getY(), 30, 30, null);
        }

        drawHUD(g);

        if (presenter.isGameOver()) {
            drawEndScreen(g, presenter.getStatusMessage(), new Color(255, 50, 50));
        } else if (presenter.isVictory()) {
            drawEndScreen(g, presenter.getStatusMessage(), new Color(50, 255, 50));
        }
    }

    private void drawHUD(Graphics g) {
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(10, 10, 180, 60);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Consolas", Font.BOLD, 14));
        g.drawString("SKOR   : " + presenter.getScore(), 20, 30);
        g.drawString("PELURU : " + presenter.getAmmo(), 20, 50);

        String status = presenter.getStatusMessage();
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString(status, (getWidth() - g.getFontMetrics().stringWidth(status)) / 2, 40);
    }

    private void drawEndScreen(Graphics g, String message, Color titleColor) {
        g.setColor(new Color(0, 0, 0, 210));
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(titleColor);
        g.setFont(new Font("Arial", Font.BOLD, 55));

        int msgX = (getWidth() - g.getFontMetrics().stringWidth(message)) / 2;

        g.drawString(message, msgX, 280);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 22));

        String scoreTxt = "Skor Akhir: " + presenter.getScore();

        g.drawString(scoreTxt, (getWidth() - g.getFontMetrics().stringWidth(scoreTxt)) / 2, 330);
        g.setColor(new Color(255, 255, 255, 40));
        g.fillRect(btnBackBounds.x, btnBackBounds.y, btnBackBounds.width, btnBackBounds.height);
        g.setColor(Color.WHITE);
        g.drawRect(btnBackBounds.x, btnBackBounds.y, btnBackBounds.width, btnBackBounds.height);
        g.setFont(new Font("Arial", Font.BOLD, 18));

        String btnText = "BACK TO MENU";
        int tx = btnBackBounds.x + (btnBackBounds.width - g.getFontMetrics().stringWidth(btnText)) / 2;
        int ty = btnBackBounds.y + 32;

        g.drawString(btnText, tx, ty);
        g.setFont(new Font("Arial", Font.ITALIC, 13));

        String hint = "Atau tekan ESC pada keyboard";

        g.drawString(hint, (getWidth() - g.getFontMetrics().stringWidth(hint)) / 2, 460);
    }

    public void backToMenu() {
        if (presenter != null) presenter.resetGame(); // Hentikan loop game
        getKeyHandler().resetKeys(); // Reset status tombol agar menu tidak macet
        frame.showView("MENU");
    }

    public void resetGameSesi() {
        this.presenter = new GamePresenter(this, frame.getPlayerName());
        this.requestFocusInWindow();
    }

    public KeyHandler getKeyHandler() { return frame.getKeyHandler(); }
}