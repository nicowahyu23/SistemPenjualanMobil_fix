package model;

public abstract class Mobil {
    protected String merk;
    protected String tipe;
    protected String jenisBB;
    protected double harga;

    public Mobil(String merk, String tipe, String jenisBB, double harga) {
        this.merk = merk;
        this.tipe = tipe;
        this.jenisBB = jenisBB;
        this.harga = harga;
    }

    // Polymorphism - setiap child class wajib override ini
    public abstract String getDetailNota();

    public String getMerk() { return merk; }
    public String getTipe() { return tipe; }
    public String getJenisBB() { return jenisBB; }
    public double getHarga() { return harga; }
}