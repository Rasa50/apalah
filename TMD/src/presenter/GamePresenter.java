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

        this.player = new Player(380, 500);
        this.rocks = Arrays.asList(new Rock(400, 300));
        this.aliens = new ArrayList<>(Arrays.asList(
                new Alien(100, 50),
                new Alien(400, 50),
                new Alien(700, 50)
        ));

        this.loop = new GameLoop(this);
        new Thread(this.loop).start();
    }

    public void update() {
        // Jika status game berakhir, hanya deteksi ESC untuk kembali ke menu
        if (gameOver || victory) {
            if (view.getKeyHandler().esc) view.backToMenu();
            return;
        }

        handleInput();

        // Gerakkan semua alien (Patroli kanan-kiri)
        for (Alien a : aliens) {
            a.update();
        }

        // Update fase berdasarkan ketersediaan amunisi
        currentPhase = (ammo > 0) ? GamePhase.PLAYER_TURN : GamePhase.HIDING;

        // Alien menyerang jika player terlihat (tidak sembunyi di balik batu)
        if (!isPlayerHidden()) {
            for (Alien a : aliens) {
                // Alien hanya menembak jika player berada dalam jangkauan pandangan (X sejajar)
                if (player.getX() >= a.getX() - 15 && player.getX() <= a.getX() + 35) {
                    if (Math.random() < 0.05) {
                        enemyBullets.add(new Bullet(a.getX() + 15, a.getY() + 30, 15, true));
                    }
                }
            }
        }

        updateEnemyBullets();
        updatePlayerBullets();

        // Cek apakah semua musuh sudah musnah
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
        // Pergerakan Player
        if (view.getKeyHandler().up) player.move(0, -5);
        if (view.getKeyHandler().down) player.move(0, 5);
        if (view.getKeyHandler().left) player.move(-5, 0);
        if (view.getKeyHandler().right) player.move(5, 0);

        // Menembak (Hanya bisa jika di fase Player Turn dan menekan spasi sekali)
        if (currentPhase == GamePhase.PLAYER_TURN && view.getKeyHandler().isShooting && ammo > 0) {
            playerBullets.add(new Bullet(player.getX() + 17, player.getY(), -10, false));
            ammo--;
            view.getKeyHandler().isShooting = false; // Reset trigger spasi
        }
    }

    private void updateEnemyBullets() {
        Iterator<Bullet> it = enemyBullets.iterator();
        while (it.hasNext()) {
            Bullet b = it.next();
            b.update();

            // Player tertabrak peluru alien
            if (b.getBounds().intersects(player.getBounds())) {
                gameOver = true;
                saveFinalData();
                return;
            }

            // Amunisi bertambah jika peluru alien meleset/keluar layar
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
                    itAlien.remove(); // Alien hancur dan dihapus dari list
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