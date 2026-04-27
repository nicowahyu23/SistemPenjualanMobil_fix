package model;

public class MobilBensin extends Mobil {
    private static final long serialVersionUID = 1L;

    public MobilBensin(String merk, String tipe, double harga, int stok, String gambar) {
        super(merk, tipe, "Bensin", harga, stok, gambar);
    }

    @Override
    public String getDetailNota() {
        return "Merk       : " + merk + "\n" +
               "Tipe       : " + tipe + "\n" +
               "Bahan Bakar: Bensin\n" +
               "Harga      : Rp " + (long) harga;
    }
}
