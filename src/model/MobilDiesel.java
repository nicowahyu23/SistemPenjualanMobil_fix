package model;

public class MobilDiesel extends Mobil {
    private static final long serialVersionUID = 1L;

    public MobilDiesel(String merk, String tipe, double harga, int stok, String gambar) {
        super(merk, tipe, "Diesel", harga, stok, gambar);
    }

    @Override
    public String getDetailNota() {
        return "Merk       : " + merk + "\n" +
               "Tipe       : " + tipe + "\n" +
               "Bahan Bakar: Diesel\n" +
               "Harga      : Rp " + (long) harga;
    }
}
