package model;

import java.io.Serializable;

/**
 * Trim/varian mobil. Tiap mobil punya beberapa varian (mis. Standard, Premium)
 * dengan tambahan harga sendiri.
 */
public class VarianMobil implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String nama;
    private final double tambahanHarga;

    public VarianMobil(String nama, double tambahanHarga) {
        this.nama = nama;
        this.tambahanHarga = tambahanHarga;
    }

    public String getNama()           { return nama; }
    public double getTambahanHarga()  { return tambahanHarga; }

    @Override
    public String toString() {
        if (tambahanHarga == 0) return nama;
        return nama + "  (+Rp " + String.format("%,d", (long) tambahanHarga) + ")";
    }
}
