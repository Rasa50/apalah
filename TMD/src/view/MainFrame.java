    package view;

    import javax.swing.*;
    import java.awt.*;

    public class MainFrame extends JFrame {
        private CardLayout cardLayout;
        private JPanel mainPanel;
        private GameView gameView;
        private String playerName; // Menyimpan nama user secara dinamis

        public MainFrame() {
            setTitle("Hide and Seek: The Challenge");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(800, 600);
            setLocationRelativeTo(null);

            cardLayout = new CardLayout();
            mainPanel = new JPanel(cardLayout);

            MenuView menuView = new MenuView(this);
            this.gameView = new GameView(this);

            mainPanel.add(menuView, "MENU");
            mainPanel.add(this.gameView, "GAME");

            add(mainPanel);
            cardLayout.show(mainPanel, "MENU");
        }

        public void setPlayerName(String name) { this.playerName = name; }
        public String getPlayerName() { return this.playerName; }

        public void showView(String name) {
            cardLayout.show(mainPanel, name);

            if (name.equals("GAME")) {
                gameView.resetGameSesi();
                gameView.requestFocusInWindow();
            }
            // TAMBAHKAN LOGIKA INI:
            else if (name.equals("MENU")) {
                // Mencari komponen MenuView di dalam mainPanel dan memanggil loadData()
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