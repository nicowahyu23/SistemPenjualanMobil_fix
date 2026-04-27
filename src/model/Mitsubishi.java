package model;

public class Mitsubishi extends MobilBensin {
    private static final long serialVersionUID = 1L;

    public Mitsubishi(String tipe, String jenisBB, double harga, int stok, String gambar) {
        super("Mitsubishi", tipe, harga, stok, gambar);
        this.jenisBB = jenisBB;
    }

    @Override
    public String getDetailNota() {
        return "Merk       : " + merk + "\n" +
               "Tipe       : " + tipe + "\n" +
               "Bahan Bakar: " + jenisBB + "\n" +
               "Harga      : Rp " + (long) harga;
    }
}
