package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Mobil implements Serializable {
    private static final long serialVersionUID = 1L;

    protected String merk;
    protected String tipe;
    protected String jenisBB;
    protected double harga;
    protected int    stok;
    protected String gambar;

    protected final List<VarianMobil> varianList = new ArrayList<>();
    protected final List<String>      warnaList  = new ArrayList<>();
    protected final List<String>      velgList   = new ArrayList<>();

    public Mobil(String merk, String tipe, String jenisBB, double harga, int stok, String gambar) {
        this.merk    = merk;
        this.tipe    = tipe;
        this.jenisBB = jenisBB;
        this.harga   = harga;
        this.stok    = stok;
        this.gambar  = gambar;
    }

    /** Polymorphism: tiap subclass override. */
    public abstract String getDetailNota();

    public String getMerk()    { return merk; }
    public String getTipe()    { return tipe; }
    public String getJenisBB() { return jenisBB; }
    public double getHarga()   { return harga; }
    public int    getStok()    { return stok; }
    public String getGambar()  { return gambar; }

    public void kurangiStok()   { if (stok > 0) stok--; }
    public void tambahStok(int n) { if (n > 0) stok += n; }

    // ---------- variant configuration (builder-style) ----------

    public Mobil tambahVarian(String nama, double tambahanHarga) {
        varianList.add(new VarianMobil(nama, tambahanHarga));
        return this;
    }

    public Mobil tambahWarna(String... warna) {
        Collections.addAll(warnaList, warna);
        return this;
    }

    public Mobil tambahVelg(String... velg) {
        Collections.addAll(velgList, velg);
        return this;
    }

    public List<VarianMobil> getVarianList() { return Collections.unmodifiableList(varianList); }
    public List<String>      getWarnaList()  { return Collections.unmodifiableList(warnaList); }
    public List<String>      getVelgList()   { return Collections.unmodifiableList(velgList); }
}
