package view;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private GameView gameView; // Disimpan sebagai variabel class agar bisa diakses untuk reset dan fokus

    public MainFrame() {
        setTitle("Hide and Seek: The Challenge");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Inisialisasi View
        MenuView menuView = new MenuView(this);
        this.gameView = new GameView(this);

        mainPanel.add(menuView, "MENU");
        mainPanel.add(this.gameView, "GAME");

        add(mainPanel);
        cardLayout.show(mainPanel, "MENU");
    }

    /**
     * Berpindah antar tampilan (Menu ke Game atau sebaliknya)
     * @param name Nama panel yang ingin ditampilkan ("MENU" atau "GAME")
     */
    public void showView(String name) {
        cardLayout.show(mainPanel, name);

        // Jika pindah ke layar GAME, lakukan persiapan sesi baru
        if (name.equals("GAME")) {
            // 1. Reset data game (skor, posisi, gameOver status) agar bisa main lagi dari nol
            gameView.resetGameSesi();

            // 2. Paksa fokus ke GameView agar KeyListener (WASD/ESC) langsung aktif
            gameView.requestFocusInWindow();
        }

        mainPanel.revalidate();
        mainPanel.repaint();
    }
}