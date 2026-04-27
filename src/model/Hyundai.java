package model;

public class Hyundai extends MobilListrik {
    public Hyundai(String tipe, double harga) {
        super("Hyundai", tipe, harga);
    }

    @Override
    public String getDetailNota() {
        return "Merk       : " + merk + "\n" +
               "Tipe       : " + tipe + "\n" +
               "Bahan Bakar: Listrik (EV)\n" +
               "Harga      : Rp " + (long)harga;
    }
}