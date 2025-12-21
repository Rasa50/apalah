package view;

import presenter.MenuPresenter;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MenuView extends JPanel {
    private JTextField txtUsername;
    private JButton btnPlay;
    private JTable tableScores;
    private DefaultTableModel tableModel;
    private MenuPresenter presenter;
    private MainFrame frame;

    public MenuView(MainFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout(10, 10));

        // Form Input
        JPanel pnlInput = new JPanel();
        pnlInput.add(new JLabel("Username: "));
        txtUsername = new JTextField(15);
        btnPlay = new JButton("Play Game");
        pnlInput.add(txtUsername);
        pnlInput.add(btnPlay);

        // Table Score
        tableModel = new DefaultTableModel(new String[]{"User", "Skor", "Meleset", "Sisa Peluru"}, 0);
        tableScores = new JTable(tableModel);

        add(pnlInput, BorderLayout.NORTH);
        add(new JScrollPane(tableScores), BorderLayout.CENTER);

        // Inisialisasi Presenter
        presenter = new MenuPresenter(this);

        btnPlay.addActionListener(e -> presenter.onPlayClicked());
        presenter.loadData();
    }

    public String getUsername() { return txtUsername.getText(); }
    public void setTableModel(DefaultTableModel model) { tableScores.setModel(model); }
    public void startGame() { frame.showView("GAME"); }
}