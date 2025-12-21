package presenter;

import view.GameView;
import model.*;
import util.GameLoop;
import java.util.ArrayList;
import java.util.List;

public class GamePresenter {
    private GameView view;
    private Player player;
    private List<Alien> aliens;
    private GameLoop gameLoop;
    private List<Bullet> bullets = new ArrayList<>();
    private int score = 0;
    private int ammo = 10;
    private boolean gameOver = false;

    public GamePresenter(GameView view) {
        this.view = view;
        this.player = new Player(380, 250);
        this.aliens = new ArrayList<>();
        this.gameLoop = new GameLoop(this);
        new Thread(gameLoop).start();
    }

    public void update() {
        if (gameOver) return;

        // Input Handling
        if (view.getKeyHandler().up) player.move(0, -5);
        if (view.getKeyHandler().down) player.move(0, 5);
        if (view.getKeyHandler().left) player.move(-5, 0);
        if (view.getKeyHandler().right) player.move(5, 0);
        if (view.getKeyHandler().pause) view.backToMenu();

        // Logic Spawn Alien sederhana
        if (Math.random() < 0.02) aliens.add(new Alien((int)(Math.random()*750), 600));

        for (int i = 0; i < aliens.size(); i++) {
            Alien a = aliens.get(i);
            a.update();
            if (!a.isAlive()) aliens.remove(i);

            // Cek tabrakan Player vs Alien
            if (player.getBounds().intersects(a.getBounds())) {
                gameOver = true;
                saveGameResult();
            }
        }

        for (int i = 0; i < bullets.size(); i++) {
            Bullet b = bullets.get(i);
            b.update();

            for (int j = 0; j < aliens.size(); j++) {
                Alien a = aliens.get(j);
                if (b.getBounds().intersects(a.getBounds())) {
                    score += 10; // Tambah skor
                    b.setInactive();
                    a.die();
                }
            }
            if (!b.isActive()) bullets.remove(i);
        }
        view.repaint();
    }

    private void saveGameResult() {
        // Panggil DAO untuk update skor ke DB
        new BenefitDAO().updateScore("User", score, 0, ammo);
    }

    // Getters untuk View
    public Player getPlayer() { return player; }
    public List<Alien> getAliens() { return aliens; }
    public int getScore() { return score; }
    public int getAmmo() { return ammo; }
    public boolean isGameOver() { return gameOver; }
    // Di dalam class GamePresenter
    public List<Bullet> getBullets() {
        return bullets;
    }
}