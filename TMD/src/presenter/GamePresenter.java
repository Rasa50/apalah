package presenter;

import view.GameView;
import model.*;
import util.GameLoop;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

public class GamePresenter {
    private GameView view;
    private Player player;
    private List<Alien> aliens;
    private List<Bullet> playerBullets = new ArrayList<>();
    private List<Bullet> enemyBullets = new ArrayList<>();
    private GameLoop gameLoop;
    private int score = 0;
    private int ammo = 100;
    private boolean gameOver = false;
    private long lastShotTime = 0;

    public GamePresenter(GameView view) {
        this.view = view;
        this.player = new Player(380, 500); // Player di bawah
        this.aliens = new ArrayList<>();
        this.gameLoop = new GameLoop(this);
        new Thread(this.gameLoop).start();
    }

    public void update() {
        if (gameOver) return;

        handleInput();

        // 1. Spawn Alien dari atas
        if (Math.random() < 0.02) aliens.add(new Alien((int)(Math.random()*750), -30));

        // 2. Jalankan logika Update (Hanya panggil method pembantu agar rapi)
        updateAliens();
        updatePlayerBullets();
        updateEnemyBullets();

        view.repaint();
    }

    private void handleInput() {
        if (view.getKeyHandler().up) player.move(0, -5);
        if (view.getKeyHandler().down) player.move(0, 5);
        if (view.getKeyHandler().left) player.move(-5, 0);
        if (view.getKeyHandler().right) player.move(5, 0);

        long currentTime = System.currentTimeMillis();
        // Menembak dengan jeda 200ms
        if (view.getKeyHandler().pause && ammo > 0 && currentTime - lastShotTime > 200) {
            playerBullets.add(new Bullet(player.getX() + 17, player.getY(), 10, false));
            ammo--;
            lastShotTime = currentTime;
        }
    }

    private void updateAliens() {
        Iterator<Alien> it = aliens.iterator();
        while (it.hasNext()) {
            Alien a = it.next();
            // Panggil update dengan parameter agar alien bisa nembak
            a.update(enemyBullets);

            // Cek tabrakan fisik player vs alien
            if (player.getBounds().intersects(a.getBounds())) {
                endGame();
            }

            if (!a.isAlive()) it.remove();
        }
    }

    private void updatePlayerBullets() {
        Iterator<Bullet> it = playerBullets.iterator();
        while (it.hasNext()) {
            Bullet b = it.next();
            b.update();

            for (Alien a : aliens) {
                if (b.getBounds().intersects(a.getBounds())) {
                    score += 10;
                    a.die();
                    b.setInactive();
                }
            }
            if (!b.isActive()) it.remove();
        }
    }

    private void updateEnemyBullets() {
        Iterator<Bullet> it = enemyBullets.iterator();
        while (it.hasNext()) {
            Bullet b = it.next();
            b.update();

            // Cek jika player kena peluru musuh
            if (b.getBounds().intersects(player.getBounds())) {
                endGame();
            }

            if (!b.isActive()) it.remove();
        }
    }

    private void endGame() {
        if (!gameOver) {
            gameOver = true;
            new BenefitDAO().updateScore("Rex ID", score, 0, ammo);
        }
    }

    // Getters untuk render di GameView
    public Player getPlayer() { return player; }
    public List<Alien> getAliens() { return aliens; }
    public List<Bullet> getPlayerBullets() { return playerBullets; }
    public List<Bullet> getEnemyBullets() { return enemyBullets; }
    public int getScore() { return score; }
    public int getAmmo() { return ammo; }
    public boolean isGameOver() { return gameOver; }
}