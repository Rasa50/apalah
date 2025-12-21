package main;

import view.MainFrame;

public class Main {
    public static void main(String[] args) {
        // Menjalankan UI di Event Dispatch Thread (EDT) agar thread-safe
        javax.swing.SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}