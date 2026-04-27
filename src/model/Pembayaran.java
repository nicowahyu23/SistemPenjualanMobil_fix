package model;

import java.io.Serializable;

/**
 * Abstraksi metode pembayaran. Tiap subclass wajib menentukan total bayar,
 * nama metode, dan rincian multi-baris untuk ditampilkan di nota.
 *
 * Polymorphism: nota tinggal panggil pembayaran.getRincian() tanpa perlu tau
 * jenisnya tunai atau kredit.
 */
public abstract class Pembayaran implements Serializable {
    private static final long serialVersionUID = 1L;

    protected final double hargaPokok;

    public Pembayaran(double hargaPokok) {
        this.hargaPokok = hargaPokok;
    }

    public double getHargaPokok() { return hargaPokok; }

    /** Total uang yang akan dibayar pembeli. */
    public abstract double getTotalBayar();

    /** Label metode pembayaran. */
    public abstract String getMetode();

    /** Rincian multi-baris untuk nota. */
    public abstract String getRincian();
}
