package model;

public class PembayaranTunai extends Pembayaran {
    private static final long serialVersionUID = 1L;

    public PembayaranTunai(double hargaPokok) {
        super(hargaPokok);
    }

    @Override
    public double getTotalBayar() {
        return hargaPokok;
    }

    @Override
    public String getMetode() {
        return "Tunai (Cash)";
    }

    @Override
    public String getRincian() {
        return "Metode      : Tunai (Cash)\n"
             + "Total Bayar : Rp " + String.format("%,d", (long) getTotalBayar());
    }
}
