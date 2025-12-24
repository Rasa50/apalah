package view;

import presenter.MenuPresenter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.BorderFactory;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class MenuView extends JPanel {
    private JTextField txtUsername;
    private JButton btnPlay;
    private JTable tableScores;
    private DefaultTableModel tableModel;
    private MenuPresenter presenter;
    private MainFrame frame;

    public MenuView(MainFrame frame) {
        this.frame = frame;
        this.presenter = new MenuPresenter(this);

        setLayout(new BorderLayout());
        setBackground(new Color(10, 10, 25));

        JPanel mainContainer = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(new Color(0, 255, 255, 30));
                for (int i = 0; i < getWidth(); i += 40) g2.drawLine(i, 0, i, getHeight());
                for (int i = 0; i < getHeight(); i += 40) g2.drawLine(0, i, getWidth(), i);

                g2.setColor(new Color(0, 200, 255, 100));
                g2.setStroke(new BasicStroke(3));
                g2.drawRoundRect(20, 20, getWidth() - 40, getHeight() - 40, 30, 30);
            }
        };
        mainContainer.setOpaque(false);
        mainContainer.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel titleLabel = new JLabel("SPACE DEFENDER");
        titleLabel.setFont(new Font("Monospaced", Font.BOLD, 36));
        titleLabel.setForeground(new Color(0, 255, 255));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.insets = new Insets(0, 0, 30, 0);
        mainContainer.add(titleLabel, gbc);

        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.setOpaque(false);

        JLabel labelName = new JLabel("PILOT NAME: ");
        labelName.setForeground(Color.WHITE);
        labelName.setFont(new Font("Monospaced", Font.BOLD, 18));

        txtUsername = new JTextField(15);
        txtUsername.setBackground(new Color(20, 20, 50));
        txtUsername.setForeground(Color.CYAN);
        txtUsername.setCaretColor(Color.CYAN);
        txtUsername.setFont(new Font("Monospaced", Font.BOLD, 18));
        txtUsername.setBorder(BorderFactory.createLineBorder(new Color(0, 255, 255), 2));

        inputPanel.add(labelName);
        inputPanel.add(txtUsername);
        gbc.gridy = 1; gbc.insets = new Insets(0, 0, 20, 0);
        mainContainer.add(inputPanel, gbc);

        tableScores = new JTable();
        tableScores.setRowHeight(30);
        tableScores.setBackground(new Color(15, 15, 40));
        tableScores.setForeground(Color.CYAN);
        tableScores.setGridColor(new Color(0, 255, 255, 50));

        JScrollPane scrollPane = new JScrollPane(tableScores);
        scrollPane.setPreferredSize(new Dimension(600, 200));
        scrollPane.getViewport().setBackground(new Color(15, 15, 40));
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(0, 255, 255, 100)),
                " LEADERBOARD ", 0, 0, null, Color.CYAN));

        gbc.gridy = 2;
        mainContainer.add(scrollPane, gbc);

        btnPlay = new JButton("INIT MISSION");
        btnPlay.setFont(new Font("Monospaced", Font.BOLD, 22));
        btnPlay.setBackground(new Color(0, 150, 150));
        btnPlay.setForeground(Color.WHITE);
        btnPlay.setFocusPainted(false);
        btnPlay.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.CYAN, 3),
                BorderFactory.createEmptyBorder(10, 30, 10, 30)
        ));

        btnPlay.addActionListener(e -> presenter.onPlayClicked());

        gbc.gridy = 3; gbc.insets = new Insets(20, 0, 0, 0);
        mainContainer.add(btnPlay, gbc);

        add(mainContainer, BorderLayout.CENTER);
        presenter.loadData();
    }

    public MenuPresenter getPresenter() {
        return presenter;
    }

    public void setTableModel(DefaultTableModel model) {
        tableScores.setAutoCreateColumnsFromModel(true);
        tableScores.setModel(model);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tableScores.getColumnCount(); i++) {
            tableScores.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    public String getUsername() { return txtUsername.getText().trim(); }
    public MainFrame getMainFrame() { return frame; }
    public void startGame() { frame.showView("GAME"); }
}