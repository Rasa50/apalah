package model;

public class Benefit {
    private String username;
    private int skor, peluruMeleset, peluruAkhir;

    public Benefit(String u, int s, int m, int a) {
        this.username = u; this.skor = s; this.peluruMeleset = m; this.peluruAkhir = a;
    }
    // Getters
    public String getUsername() { return username; }
    public int getSkor() { return skor; }
    public int getPeluruMeleset() { return peluruMeleset; }
    public int getPeluruAkhir() { return peluruAkhir; }
}