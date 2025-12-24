package util;

import presenter.GamePresenter;

public class GameLoop implements Runnable {
    private GamePresenter presenter;
    private volatile boolean running = true;

    public GameLoop(GamePresenter presenter) {
        this.presenter = presenter;
    }

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