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

        // Table Score - Inisialisasi model dengan kolom yang sesuai spesifikasi
        tableModel = new DefaultTableModel(new String[]{"User", "Skor", "Meleset", "Sisa Peluru"}, 0);
        tableScores = new JTable(tableModel);

        add(pnlInput, BorderLayout.NORTH);
        add(new JScrollPane(tableScores), BorderLayout.CENTER);

        // Inisialisasi Presenter
        presenter = new MenuPresenter(this);

        btnPlay.addActionListener(e -> presenter.onPlayClicked());
        presenter.loadData();
    }

    // Mengambil referensi MainFrame untuk koordinasi antar layar
    public MainFrame getMainFrame() {
        return frame;
    }

    public String getUsername() {
        return txtUsername.getText();
    }

    // Method aman untuk memperbarui data tabel tanpa merusak header kolom
    public void updateTableData(DefaultTableModel newModel) {
        tableModel.setRowCount(0); // Hapus data lama
        for (int i = 0; i < newModel.getRowCount(); i++) {
            Object[] row = new Object[newModel.getColumnCount()];
            for (int j = 0; j < newModel.getColumnCount(); j++) {
                row[j] = newModel.getValueAt(i, j);
            }
            tableModel.addRow(row); // Tambah data baru
        }
    }

    public void setTableModel(DefaultTableModel model) {
        tableScores.setModel(model);
    }

    public void startGame() {
        frame.showView("GAME");
    }

    public MenuPresenter getPresenter() {
        return presenter;
    }
}