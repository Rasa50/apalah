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
        JPanel pnlInput = new JPanel();
        pnlInput.add(new JLabel("Username: "));
        txtUsername = new JTextField(15);
        btnPlay = new JButton("Play Game");
        pnlInput.add(txtUsername);
        pnlInput.add(btnPlay);

        tableModel = new DefaultTableModel(new String[]{"User", "Skor", "Meleset", "Sisa Peluru"}, 0);
        tableScores = new JTable(tableModel);
        add(pnlInput, BorderLayout.NORTH);
        add(new JScrollPane(tableScores), BorderLayout.CENTER);

        presenter = new MenuPresenter(this);
        btnPlay.addActionListener(e -> presenter.onPlayClicked());
        presenter.loadData();
    }

    public void setTableModel(DefaultTableModel model) {
        // Perbaikan: Mengizinkan pembuatan kolom otomatis agar tidak ArrayIndexOutOfBounds
        tableScores.setAutoCreateColumnsFromModel(true);
        tableScores.setModel(model);
    }

    public String getUsername() { return txtUsername.getText(); }
    public MainFrame getMainFrame() { return frame; }
    public void startGame() { frame.showView("GAME"); }
    public MenuPresenter getPresenter() { return presenter; }
}