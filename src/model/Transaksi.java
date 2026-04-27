package model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Snapshot 1 transaksi pembelian. Disimpan ke file via RepositoryTransaksi.
 * Field merk/tipe/harga sengaja di-snapshot supaya nota lama tetap konsisten
 * walau data master berubah.
 */
public class Transaksi implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String        noNota;
    private final LocalDateTime tanggal;
    private final String        namaPembeli;
    private final String        usernamePetugas;

    private final String merk;
    private final String tipe;
    private final String jenisBB;
    private final double hargaMobil;

    private final String varianNama;
    private final double varianTambahan;
    private final String warna;
    private final String velg;

    private final Pembayaran pembayaran;

    public Transaksi(String noNota,
                     LocalDateTime tanggal,
                     String namaPembeli,
                     String usernamePetugas,
                     Mobil mobil,
                     VarianMobil varian,
                     String warna,
                     String velg,
                     Pembayaran pembayaran) {
        this.noNota          = noNota;
        this.tanggal         = tanggal;
        this.namaPembeli     = namaPembeli;
        this.usernamePetugas = usernamePetugas;
        this.merk            = mobil.getMerk();
        this.tipe            = mobil.getTipe();
        this.jenisBB         = mobil.getJenisBB();
        this.hargaMobil      = mobil.getHarga();
        this.varianNama      = varian == null ? "Standard" : varian.getNama();
        this.varianTambahan  = varian == null ? 0.0        : varian.getTambahanHarga();
        this.warna           = warna == null ? "-" : warna;
        this.velg            = velg  == null ? "-" : velg;
        this.pembayaran      = pembayaran;
    }

    public double getTotalHarga() { return hargaMobil + varianTambahan; }

    public String        getNoNota()          { return noNota; }
    public LocalDateTime getTanggal()         { return tanggal; }
    public String        getNamaPembeli()     { return namaPembeli; }
    public String        getUsernamePetugas() { return usernamePetugas; }
    public String        getMerk()            { return merk; }
    public String        getTipe()            { return tipe; }
    public String        getJenisBB()         { return jenisBB; }
    public double        getHargaMobil()      { return hargaMobil; }
    public String        getVarianNama()      { return varianNama; }
    public double        getVarianTambahan()  { return varianTambahan; }
    public String        getWarna()           { return warna; }
    public String        getVelg()            { return velg; }
    public Pembayaran    getPembayaran()      { return pembayaran; }
}
