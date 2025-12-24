package view;

import util.KeyHandler;
import java.awt.Component;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.CardLayout;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private GameView gameView;
    private KeyHandler keyHandler;
    private String playerName;

    public MainFrame() {
        setTitle("Hide and Seek: The Challenge");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(820, 650);
        setLocationRelativeTo(null);

        this.keyHandler = new KeyHandler();
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        MenuView menuView = new MenuView(this);
        this.gameView = new GameView(this);

        mainPanel.add(menuView, "MENU");
        mainPanel.add(this.gameView, "GAME");

        add(mainPanel);
        cardLayout.show(mainPanel, "MENU");
    }

    public KeyHandler getKeyHandler() {
        return this.keyHandler;
    }

    public void setPlayerName(String name) { this.playerName = name; }
    public String getPlayerName() { return this.playerName; }

    public void showView(String name) {
        cardLayout.show(mainPanel, name);

        if (name.equals("GAME")) {
            gameView.resetGameSesi();
            gameView.requestFocusInWindow();
        }
        else if (name.equals("MENU")) {
            for (Component comp : mainPanel.getComponents()) {
                if (comp instanceof MenuView) {
                    ((MenuView) comp).getPresenter().loadData();
                }
            }
        }
        mainPanel.revalidate();
        mainPanel.repaint();
    }
}