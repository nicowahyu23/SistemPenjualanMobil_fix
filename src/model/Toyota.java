package model;

public class Toyota extends MobilBensin {
    public Toyota(String tipe, String jenisBB, double harga) {
        super("Toyota", tipe, harga);
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