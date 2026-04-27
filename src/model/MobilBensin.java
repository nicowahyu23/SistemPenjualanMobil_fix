package model;

public class MobilBensin extends Mobil {
    public MobilBensin(String merk, String tipe, double harga) {
        super(merk, tipe, "Bensin", harga);
    }

    @Override
    public String getDetailNota() {
        return "Merk       : " + merk + "\n" +
               "Tipe       : " + tipe + "\n" +
               "Bahan Bakar: Bensin\n" +
               "Harga      : Rp " + (long)harga;
    }
}