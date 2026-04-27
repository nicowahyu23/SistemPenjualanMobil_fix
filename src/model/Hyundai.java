package model;

public class Hyundai extends MobilListrik {
    private static final long serialVersionUID = 1L;

    /** Konstruktor untuk Hyundai EV (default Listrik). */
    public Hyundai(String tipe, double harga, int stok, String gambar) {
        super("Hyundai", tipe, harga, stok, gambar);
    }

    /** Konstruktor untuk Hyundai non-EV (mis. Tucson Bensin, Palisade Diesel). */
    public Hyundai(String tipe, String jenisBB, double harga, int stok, String gambar) {
        super("Hyundai", tipe, harga, stok, gambar);
        this.jenisBB = jenisBB;
    }

    @Override
    public String getDetailNota() {
        return "Merk       : " + merk + "\n" +
               "Tipe       : " + tipe + "\n" +
               "Bahan Bakar: " + jenisBB + "\n" +
               "Harga      : Rp " + (long) harga;
    }
}
