package util;

import presenter.GamePresenter;

public class GameLoop implements Runnable {
    private GamePresenter presenter;
    private volatile boolean running = true; // Gunakan volatile agar sinkron antar thread

    public GameLoop(GamePresenter presenter) {
        this.presenter = presenter;
    }

    // Method baru untuk mematikan thread
    public void stop() {
        this.running = false;
    }

    @Override
    public void run() {
        while (running) {
            presenter.update();
            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}