package presenter;

import view.GameView;
import model.*;
import util.GameLoop;
import java.util.*;

public class GamePresenter {
    private GameView view;
    private Player player;
    private List<Alien> aliens;
    private List<Rock> rocks;
    private List<Bullet> playerBullets = new ArrayList<>();
    private List<Bullet> enemyBullets = new ArrayList<>();
    private int score, missedBullets, ammo;
    private boolean gameOver = false;
    private boolean victory = false;
    private String currentUsername;
    private GameLoop loop;
    private Random rand = new Random(); // Digunakan untuk posisi acak batu

    public enum GamePhase { HIDING, PLAYER_TURN }
    private GamePhase currentPhase = GamePhase.HIDING;

    public GamePresenter(GameView view, String username) {
        this.view = view;
        this.currentUsername = username;
        BenefitDAO dao = new BenefitDAO();
        Benefit dataLama = dao.getUserData(username);

        // Memuat data lama atau inisialisasi baru
        if (dataLama != null) {
            this.score = dataLama.getSkor();
            this.ammo = dataLama.getPeluruAkhir();
            this.missedBullets = dataLama.getPeluruMeleset();
        } else {
            dao.upsertUser(username);
            this.score = 0; this.ammo = 0; this.missedBullets = 0;
        }

        // SPESIFIKASI: Pemeran utama muncul dari tengah (asumsi frame 800x600)
        this.player = new Player(380, 280);

        // SPESIFIKASI: Posisi batu (perlindungan) di-generasi acak setiap permainan dimulai
        this.rocks = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            int rx = rand.nextInt(700) + 50;
            int ry = rand.nextInt(400) + 50;
            this.rocks.add(new Rock(rx, ry));
        }

        // SPESIFIKASI: Alien muncul dari bawah
        this.aliens = new ArrayList<>(Arrays.asList(
                new Alien(100, 550),
                new Alien(400, 550),
                new Alien(700, 550)
        ));

        this.loop = new GameLoop(this);
        new Thread(this.loop).start();
    }

    public void update() {
        // SPESIFIKASI: Tombol space digunakan untuk menghentikan permainan dan kembali ke menu awal
        if (view.getKeyHandler().space) {
            view.backToMenu();
            return;
        }

        // SPESIFIKASI: Game Over jika pemeran utama tertembak
        if (gameOver || victory) {
            if (view.getKeyHandler().esc) view.backToMenu();
            return;
        }

        handleInput();

        // Gerakkan semua alien (Patroli otomatis)
        for (Alien a : aliens) {
            a.update();
        }

        // Update fase berdasarkan ketersediaan amunisi
        currentPhase = (ammo > 0) ? GamePhase.PLAYER_TURN : GamePhase.HIDING;

        // Alien menembak otomatis (karena muncul dari bawah, peluru bergerak ke atas/speed negatif)
        if (!isPlayerHidden()) {
            for (Alien a : aliens) {
                if (player.getX() >= a.getX() - 20 && player.getX() <= a.getX() + 40) {
                    if (Math.random() < 0.05) {
                        enemyBullets.add(new Bullet(a.getX() + 15, a.getY() - 10, -12, true));
                    }
                }
            }
        }

        updateEnemyBullets();
        updatePlayerBullets();

        // Cek kemenangan (Semua alien habis)
        if (aliens.isEmpty() && !victory) {
            victory = true;
            saveFinalData();
        }

        view.repaint();
    }

    public String getStatusMessage() {
        if (gameOver) return "MISI GAGAL!";
        if (victory) return "MISI BERHASIL!";
        return (currentPhase == GamePhase.HIDING) ? "CARI PERSEMBUNYIAN!" : "SERANG ALIEN!";
    }

    public boolean isPlayerHidden() {
        for (Rock r : rocks) {
            if (player.getBounds().intersects(r.getBounds())) return true;
        }
        return false;
    }

    private void handleInput() {
        // SPESIFIKASI: Tombol panah up, down, right, left untuk menggerakkan pemeran utama
        if (view.getKeyHandler().up) player.move(0, -5);
        if (view.getKeyHandler().down) player.move(0, 5);
        if (view.getKeyHandler().left) player.move(-5, 0);
        if (view.getKeyHandler().right) player.move(5, 0);

        // Logika menembak menggunakan amunisi yang tersedia
        if (currentPhase == GamePhase.PLAYER_TURN && view.getKeyHandler().isShooting && ammo > 0) {
            playerBullets.add(new Bullet(player.getX() + 17, player.getY(), -10, false));
            ammo--;
            view.getKeyHandler().isShooting = false;
        }
    }

    private void updateEnemyBullets() {
        Iterator<Bullet> it = enemyBullets.iterator();
        while (it.hasNext()) {
            Bullet b = it.next();
            b.update();

            // SPESIFIKASI: Game Over jika pemeran utama tertembak
            if (b.getBounds().intersects(player.getBounds())) {
                gameOver = true;
                saveFinalData();
                return;
            }

            // SPESIFIKASI: Skor, jumlah peluru meleset, dan jumlah peluru bertambah
            if (!b.isActive()) {
                missedBullets++;
                ammo++;
                it.remove();
            }
        }
    }

    private void updatePlayerBullets() {
        Iterator<Bullet> itBullet = playerBullets.iterator();
        while (itBullet.hasNext()) {
            Bullet b = itBullet.next();
            b.update();

            Iterator<Alien> itAlien = aliens.iterator();
            while (itAlien.hasNext()) {
                Alien a = itAlien.next();
                if (b.getBounds().intersects(a.getBounds())) {
                    score += 10;
                    itAlien.remove();
                    b.setInactive();
                }
            }
            if (!b.isActive()) itBullet.remove();
        }
    }

    private void saveFinalData() {
        new BenefitDAO().updateScore(currentUsername, score, missedBullets, ammo);
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