package model;

public class Mitsubishi extends MobilBensin {
    public Mitsubishi(String tipe, String jenisBB, double harga) {
        super("Mitsubishi", tipe, harga);
        this.jenisBB = jenisBB;
    }

    @Override
    public String getDetailNota() {
        return "Merk       : " + merk + "\n" +
               "Tipe       : " + tipe + "\n" +
               "Bahan Bakar: " + jenisBB + "\n" +
               "Harga      : Rp " + (long)harga;
    }
}