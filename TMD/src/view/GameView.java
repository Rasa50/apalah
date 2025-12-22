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
        setBackground(new Color(10, 10, 40));
        keyHandler = new KeyHandler();
        addKeyListener(keyHandler);
        presenter = new GamePresenter(this);
    }

    private void drawGame(Graphics2D g) {
        // Gambar Player, Alien, Peluru (logika gambar sama)
        // ... (gambar objek)

        // HUD Spesifikasi Baru
        g.setColor(Color.WHITE);
        g.setFont(new Font("Consolas", Font.BOLD, 16));
        g.drawString("SKOR      : " + presenter.getScore(), 20, 30);
        g.drawString("AMUNISI   : " + presenter.getAmmo(), 20, 55);

        String status = presenter.getStatusMessage();
        g.drawString("STATUS    : " + status, 20, 80);

        if(presenter.isGameOver()) {
            drawGameOverScreen(g);
        }
    }
    // ... (rest of the file)
}