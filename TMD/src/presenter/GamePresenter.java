package presenter;

import view.GameView;
import model.Player;
import model.Alien;
import model.Rock;
import model.Bullet;
import model.Benefit;
import model.BenefitDAO;
import util.GameLoop;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

public class GamePresenter {
    private GameView view;
    private Player player;
    private List<Alien> aliens;
    private List<Rock> rocks;
    private List<Bullet> playerBullets = new ArrayList<>();
    private List<Bullet> enemyBullets = new ArrayList<>();
    private int score, missedBullets, ammo;
    private boolean gameOver = false, victory = false;
    private String currentUsername;
    private GameLoop loop;
    private Random rand = new Random();

    private long lastScanTime = 0;
    private boolean isScanning = false;
    private final int SCAN_DURATION_MS = 1000;
    private final int SCAN_INTERVAL_MS = 3000;

    public enum GamePhase { HIDING, PLAYER_TURN }
    private GamePhase currentPhase = GamePhase.HIDING;

    public GamePresenter(GameView view, String username) {
        this.view = view;
        this.currentUsername = username;
        BenefitDAO dao = new BenefitDAO();
        Benefit dataLama = dao.getUserData(username);

        if (dataLama != null) {
            this.score = dataLama.getSkor();
            this.ammo = dataLama.getPeluruAkhir();
            this.missedBullets = dataLama.getPeluruMeleset();
        } else {
            dao.upsertUser(username);
            this.score = 0; this.ammo = 0; this.missedBullets = 0;
        }

        this.player = new Player(380, 280);
        this.rocks = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            this.rocks.add(new Rock(rand.nextInt(700) + 50, rand.nextInt(400) + 50));
        }

        this.aliens = new ArrayList<>(Arrays.asList(
                new Alien(100, 550),
                new Alien(400, 550),
                new Alien(700, 550)
        ));

        this.loop = new GameLoop(this);
        new Thread(this.loop).start();
    }

    public void update() {
        if (view.getKeyHandler().space && !gameOver && !victory) {
            gameOver = true;
            saveFinalData();
            view.repaint();
            return;
        }

        if (gameOver || victory) {
            if (view.getKeyHandler().esc) {
                resetGame();
                view.getKeyHandler().resetKeys();
                view.backToMenu();
            }
            return;
        }

        handleInput();
        for (Alien a : aliens) a.update();

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastScanTime > SCAN_INTERVAL_MS) {
            isScanning = true;
            lastScanTime = currentTime;
        } else if (isScanning && currentTime - lastScanTime > SCAN_DURATION_MS) {
            isScanning = false;
        }

        currentPhase = (ammo > 0) ? GamePhase.PLAYER_TURN : GamePhase.HIDING;

        if (!isPlayerHidden()) {
            for (Alien a : aliens) {
                if (Math.random() < 0.02) {
                    double diffX = player.getX() - a.getX();
                    double diffY = player.getY() - a.getY();
                    double distance = Math.sqrt(diffX * diffX + diffY * diffY);
                    double bSpeed = 7.0;
                    enemyBullets.add(new Bullet(a.getX() + 15, a.getY(), (diffX/distance)*bSpeed, (diffY/distance)*bSpeed, true));
                }
            }
        }

        updateEnemyBullets();
        updatePlayerBullets();

        if (aliens.isEmpty() && !victory) {
            victory = true;
            saveFinalData();
        }
        view.repaint();
    }

    public String getStatusMessage() {
        if (gameOver) return "MISI GAGAL!";
        if (victory) return "MISI BERHASIL!";
        if (isScanning) return "ALIEN SEDANG SCANNING MAP!";
        return (currentPhase == GamePhase.HIDING) ? "CARI PERSEMBUNYIAN!" : "SERANG ALIEN!";
    }

    private void handleInput() {
        if (view.getKeyHandler().up) player.move(0, -5);
        if (view.getKeyHandler().down) player.move(0, 5);
        if (view.getKeyHandler().left) player.move(-5, 0);
        if (view.getKeyHandler().right) player.move(5, 0);

        if (currentPhase == GamePhase.PLAYER_TURN && view.getKeyHandler().isShooting && ammo > 0 && !isPlayerHidden()) {
            playerBullets.add(new Bullet(player.getX() + 17, player.getY(), 0, 10, false));
            ammo--;
            view.getKeyHandler().isShooting = false;
        }
    }

    private void updateEnemyBullets() {
        Iterator<Bullet> it = enemyBullets.iterator();
        while (it.hasNext()) {
            Bullet b = it.next();
            b.update();
            if (!isPlayerHidden() && b.getBounds().intersects(player.getBounds())) {
                gameOver = true;
                saveFinalData();
                return;
            }
            if (!b.isActive()) { missedBullets++; ammo++; it.remove(); }
        }
    }

    private void updatePlayerBullets() {
        Iterator<Bullet> it = playerBullets.iterator();
        while (it.hasNext()) {
            Bullet b = it.next();
            b.update();
            Iterator<Alien> itA = aliens.iterator();
            while (itA.hasNext()) {
                Alien a = itA.next();
                if (b.getBounds().intersects(a.getBounds())) {
                    score += 10;
                    itA.remove();
                    b.setInactive();
                }
            }
            if (!b.isActive()) it.remove();
        }
    }

    private void saveFinalData() { new BenefitDAO().updateScore(currentUsername, score, missedBullets, ammo); }

    public boolean isPlayerHidden() {
        for (Rock r : rocks) if (player.getBounds().intersects(r.getBounds())) return true;
        return false;
    }

    public void resetGame() { if (loop != null) loop.stop(); }
    public List<Rock> getRocks() { return rocks; }
    public Player getPlayer() { return player; }
    public List<Alien> getAliens() { return aliens; }
    public List<Bullet> getPlayerBullets() { return playerBullets; }
    public List<Bullet> getEnemyBullets() { return enemyBullets; }
    public int getScore() { return score; }
    public int getAmmo() { return ammo; }
    public boolean isGameOver() { return gameOver; }
    public boolean isVictory() { return victory; }
}