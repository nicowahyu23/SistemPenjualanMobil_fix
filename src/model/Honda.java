package model;

public class Honda extends MobilBensin {
    public Honda(String tipe, String jenisBB, double harga) {
        super("Honda", tipe, harga);
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