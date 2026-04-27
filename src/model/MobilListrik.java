package model;

public class MobilListrik extends Mobil {
    public MobilListrik(String merk, String tipe, double harga) {
        super(merk, tipe, "Listrik", harga);
    }

    @Override
    public String getDetailNota() {
        return "Merk       : " + merk + "\n" +
               "Tipe       : " + tipe + "\n" +
               "Bahan Bakar: Listrik (EV)\n" +
               "Harga      : Rp " + (long)harga;
    }
}