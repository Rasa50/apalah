package util;

import presenter.GamePresenter;

public class GameLoop implements Runnable {
    private GamePresenter presenter;
    private boolean running = true;

    public GameLoop(GamePresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void run() {
        while (running) {
            presenter.update();
            try {
                Thread.sleep(16); // Target ~60 FPS
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}