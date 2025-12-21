package view;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public MainFrame() {
        setTitle("Hide and Seek: The Challenge");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Inisialisasi View
        MenuView menuView = new MenuView(this);
        GameView gameView = new GameView(this);

        mainPanel.add(menuView, "MENU");
        mainPanel.add(gameView, "GAME");

        add(mainPanel);
        cardLayout.show(mainPanel, "MENU");
    }

    public void showView(String name) {
        cardLayout.show(mainPanel, name);
        mainPanel.revalidate();
    }
}