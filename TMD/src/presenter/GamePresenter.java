package presenter;

import view.GameView;
import model.*;
import util.GameLoop;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.Random;

public class GamePresenter {
    private GameView view;
    private Player player;
    private List<Alien> aliens;
    private List<Bullet> playerBullets = new ArrayList<>();
    private List<Bullet> enemyBullets = new ArrayList<>();

    private int score = 0;
    private int missedBullets = 0; // Peluru alien yang meleset
    private int ammo = 0;          // Awalnya tidak punya peluru
    private boolean gameOver = false;
    private String currentUsername;

    public enum GamePhase { HIDING, PLAYER_TURN }
    private GamePhase currentPhase = GamePhase.HIDING;
    private long phaseStartTime;
    private final int HIDE_TIME = 3000; // 3 detik persembunyian
    private Random rand = new Random();

    public GamePresenter(GameView view, String username) {
        this.view = view;
        this.currentUsername = username;
        this.player = new Player(380, 500);
        this.aliens = new ArrayList<>();
        this.aliens.add(new Alien(380, 50));

        new BenefitDAO().upsertUser(this.currentUsername);
        this.phaseStartTime = System.currentTimeMillis();
        new Thread(new GameLoop(this)).start();
    }

    public void update() {
        if (gameOver && view.getKeyHandler().esc) {
            view.backToMenu();
            return;
        }
        if (gameOver) return;

        handleInput(); // Proses masukan keyboard

        if (currentPhase == GamePhase.HIDING) {
            // Alien menembak secara acak agar player mencari posisi
            if (Math.random() < 0.05) {
                enemyBullets.add(new Bullet(rand.nextInt(750), 0, 5, true));
            }
            updateEnemyBullets();

            if (System.currentTimeMillis() - phaseStartTime > HIDE_TIME) {
                currentPhase = GamePhase.PLAYER_TURN;
            }
        } else {
            updatePlayerBullets();
            // Jika peluru habis, kembali sembunyi untuk kumpulkan peluru lagi
            if (playerBullets.isEmpty() && ammo == 0) {
                currentPhase = GamePhase.HIDING;
                phaseStartTime = System.currentTimeMillis();
            }
        }
        view.repaint();
    }

    private void handleInput() {
        // Player bisa jalan kiri, kanan, bawah, dan atas
        if (view.getKeyHandler().up) player.move(0, -5);
        if (view.getKeyHandler().down) player.move(0, 5);
        if (view.getKeyHandler().left) player.move(-5, 0);
        if (view.getKeyHandler().right) player.move(5, 0);

        // Menembak mengurangi jumlah amunisi
        if (currentPhase == GamePhase.PLAYER_TURN && view.getKeyHandler().pause && ammo > 0) {
            playerBullets.add(new Bullet(player.getX() + 17, player.getY(), 10, false));
            ammo--;
        }
    }

    private void updateEnemyBullets() {
        Iterator<Bullet> it = enemyBullets.iterator();
        while (it.hasNext()) {
            Bullet b = it.next();
            b.update();
            if (b.getBounds().intersects(player.getBounds())) {
                endGame();
            }
            if (!b.isActive()) {
                missedBullets++; // Peluru alien meleset
                ammo++;          // Peluru bertambah sesuai yang meleset
                it.remove();
            }
        }
    }

    private void updatePlayerBullets() {
        Iterator<Bullet> it = playerBullets.iterator();
        while (it.hasNext()) {
            Bullet b = it.next();
            b.update();
            for (Alien a : aliens) {
                if (b.getBounds().intersects(a.getBounds())) {
                    score += 10; // Alien tertembak menambah skor
                    a.setX(rand.nextInt(750));
                    b.setInactive();
                }
            }
            if (!b.isActive()) it.remove();
        }
    }

    private void endGame() {
        if (!gameOver) {
            gameOver = true;
            new BenefitDAO().updateScore(this.currentUsername, this.score, this.missedBullets, this.ammo);
        }
    }

    public void resetGame() {
        this.score = 0;
        this.missedBullets = 0;
        this.ammo = 0;
        this.gameOver = false;
        this.currentPhase = GamePhase.HIDING;
        this.phaseStartTime = System.currentTimeMillis();
    }

    public String getStatusMessage() {
        return (currentPhase == GamePhase.HIDING) ? "CARI PERSEMBUNYIAN!" : "SERANG ALIEN!";
    }

    // Getters...
    public Player getPlayer() { return player; }
    public List<Alien> getAliens() { return aliens; }
    public List<Bullet> getPlayerBullets() { return playerBullets; }
    public List<Bullet> getEnemyBullets() { return enemyBullets; }
    public int getScore() { return score; }
    public int getAmmo() { return ammo; }
    public boolean isGameOver() { return gameOver; }
}