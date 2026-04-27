package model;

/**
 * Pembayaran kredit dengan bunga FLAT sederhana:
 *   bunga total = pokok_dicicil * (bunga% / 100) * (tenor / 12)
 *   cicilan/bulan = (pokok_dicicil + bunga total) / tenor
 */
public class PembayaranKredit extends Pembayaran {
    private static final long serialVersionUID = 1L;

    private final double persenDP;     // 0..100
    private final int    tenorBulan;
    private final double bungaTahunan; // 0..100

    public PembayaranKredit(double hargaPokok, double persenDP, int tenorBulan, double bungaTahunan) {
        super(hargaPokok);
        this.persenDP = persenDP;
        this.tenorBulan = tenorBulan;
        this.bungaTahunan = bungaTahunan;
    }

    public double getDP()             { return hargaPokok * persenDP / 100.0; }
    public double getPokokDicicil()   { return hargaPokok - getDP(); }
    public double getTotalBunga()     { return getPokokDicicil() * (bungaTahunan / 100.0) * (tenorBulan / 12.0); }
    public double getTotalCicilan()   { return getPokokDicicil() + getTotalBunga(); }
    public double getCicilanPerBulan(){ return tenorBulan == 0 ? 0 : getTotalCicilan() / tenorBulan; }

    public double getPersenDP()       { return persenDP; }
    public int    getTenorBulan()     { return tenorBulan; }
    public double getBungaTahunan()   { return bungaTahunan; }

    @Override
    public double getTotalBayar() {
        return getDP() + getTotalCicilan();
    }

    @Override
    public String getMetode() {
        return "Kredit";
    }

    @Override
    public String getRincian() {
        StringBuilder sb = new StringBuilder();
        sb.append("Metode       : Kredit\n");
        sb.append("DP (").append((long) persenDP).append("%)    : Rp ")
          .append(String.format("%,d", (long) getDP())).append("\n");
        sb.append("Tenor        : ").append(tenorBulan).append(" bulan\n");
        sb.append("Bunga        : ").append(String.format("%.1f", bungaTahunan)).append(" % / tahun (flat)\n");
        sb.append("Cicilan/bln  : Rp ").append(String.format("%,d", (long) getCicilanPerBulan())).append("\n");
        sb.append("Total Bayar  : Rp ").append(String.format("%,d", (long) getTotalBayar()));
        return sb.toString();
    }
}
