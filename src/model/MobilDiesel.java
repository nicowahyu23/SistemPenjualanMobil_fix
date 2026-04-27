package model;

public class MobilDiesel extends Mobil {
    public MobilDiesel(String merk, String tipe, double harga) {
        super(merk, tipe, "Diesel", harga);
    }

    @Override
    public String getDetailNota() {
        return "Merk       : " + merk + "\n" +
               "Tipe       : " + tipe + "\n" +
               "Bahan Bakar: Diesel\n" +
               "Harga      : Rp " + (long)harga;
    }
}