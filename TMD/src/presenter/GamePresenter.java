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

    public enum GamePhase { HIDING, PLAYER_TURN }
    private GamePhase currentPhase = GamePhase.HIDING;
    private long phaseStartTime;
    private final int HIDE_TIME = 3000;
    private Random rand = new Random();

    public GamePresenter(GameView view) {
        this.view = view;
        this.player = new Player(380, 500);
        this.aliens = new ArrayList<>();
        this.aliens.add(new Alien(380, 50));

        new BenefitDAO().upsertUser("Rex ID");
        this.phaseStartTime = System.currentTimeMillis();
        new Thread(new GameLoop(this)).start();
    }

    public void update() {
        if (gameOver && view.getKeyHandler().esc) {
            view.backToMenu();
            return;
        }
        if (gameOver) return;

        if (currentPhase == GamePhase.HIDING) {
            handleInput();
            // Alien menembak secara acak saat fase sembunyi
            if (Math.random() < 0.05) {
                enemyBullets.add(new Bullet(rand.nextInt(750), 0, 5, true));
            }
            updateEnemyBullets();

            if (System.currentTimeMillis() - phaseStartTime > HIDE_TIME) {
                currentPhase = GamePhase.PLAYER_TURN;
            }
        } else {
            handleInput();
            updatePlayerBullets();
            // Jika peluru habis, kembali ke fase sembunyi untuk cari peluru lagi
            if (playerBullets.isEmpty() && ammo == 0) {
                currentPhase = GamePhase.HIDING;
                phaseStartTime = System.currentTimeMillis();
            }
        }
        view.repaint();
    }

    private void handleInput() {
        // Spesifikasi: Bisa berjalan kiri, kanan, bawah (dan atas untuk sembunyi)
        if (view.getKeyHandler().up) player.move(0, -5);
        if (view.getKeyHandler().down) player.move(0, 5);
        if (view.getKeyHandler().left) player.move(-5, 0);
        if (view.getKeyHandler().right) player.move(5, 0);

        // Menembak hanya jika di fase PLAYER_TURN dan punya amunisi
        if (currentPhase == GamePhase.PLAYER_TURN && view.getKeyHandler().pause && ammo > 0) {
            playerBullets.add(new Bullet(player.getX() + 17, player.getY(), 10, false));
            ammo--; // Peluru yang ditembakkan mengurangi jumlah peluru
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
                ammo++;          // Peluru bertambah sesuai peluru alien yang meleset
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
                    score += 10; // Setiap alien yang dikalahkan menambah skor
                    a.setX(rand.nextInt(750)); // Alien berpindah tempat
                    b.setInactive();
                }
            }
            if (!b.isActive()) it.remove();
        }
    }

    private void endGame() {
        if (!gameOver) {
            gameOver = true;
            // Spesifikasi: Simpan skor, meleset, dan sisa peluru akhir
            new BenefitDAO().updateScore("Rex ID", score, missedBullets, ammo);
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

    public Player getPlayer() { return player; }
    public List<Alien> getAliens() { return aliens; }
    public List<Bullet> getPlayerBullets() { return playerBullets; }
    public List<Bullet> getEnemyBullets() { return enemyBullets; }
    public int getScore() { return score; }
    public int getAmmo() { return ammo; }
    public boolean isGameOver() { return gameOver; }
}