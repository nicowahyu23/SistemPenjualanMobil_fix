package model;

public class MobilListrik extends Mobil {
    private static final long serialVersionUID = 1L;

    public MobilListrik(String merk, String tipe, double harga, int stok, String gambar) {
        super(merk, tipe, "Listrik", harga, stok, gambar);
    }

    @Override
    public String getDetailNota() {
        return "Merk       : " + merk + "\n" +
               "Tipe       : " + tipe + "\n" +
               "Bahan Bakar: Listrik (EV)\n" +
               "Harga      : Rp " + (long) harga;
    }
}
