package service;

import model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Registry data master mobil. Menggantikan array harcoded di FormPenjualan
 * supaya stok in-memory bisa dipakai bersama lintas form.
 *
 * Stok awal seragam = {@link #STOK_AWAL} unit per tipe.
 */
public class MobilRegistry {

    public static final int STOK_AWAL = 5;

    private static final List<Mobil> mobils = new ArrayList<>();

    static {
        // ---------- Toyota ----------
        tambah(varianStandard(new Toyota("Fortuner",   "Diesel",  543_000_000, STOK_AWAL, "fortuner.jpg")));
        tambah(varianStandard(new Toyota("Innova",     "Diesel",  417_800_000, STOK_AWAL, "innova.jpg")));
        tambah(varianStandard(new Toyota("Avanza",     "Bensin",  243_700_000, STOK_AWAL, "avanza.jpg")));
        tambah(varianStandard(new Toyota("Rush",       "Bensin",  272_400_000, STOK_AWAL, "rush.jpg")));
        tambah(varianStandard(new Toyota("Yaris",      "Bensin",  330_900_000, STOK_AWAL, "yaris.jpg")));
        tambah(varianEV      (new Toyota("bZ4X",       "Listrik", 890_000_000, STOK_AWAL, "bz4x.jpg")));

        // ---------- Mitsubishi ----------
        tambah(varianStandard(new Mitsubishi("Pajero Sport", "Diesel", 650_000_000, STOK_AWAL, "pajero.jpg")));
        tambah(varianStandard(new Mitsubishi("Xpander",      "Bensin", 275_900_000, STOK_AWAL, "xpander.jpg")));
        tambah(varianStandard(new Mitsubishi("Xforce",       "Bensin", 369_900_000, STOK_AWAL, "xforce.jpg")));
        tambah(varianStandard(new Mitsubishi("Destinator",   "Bensin", 420_000_000, STOK_AWAL, "destinator.jpg")));

        // ---------- Hyundai ----------
        tambah(varianEV      (new Hyundai("Ioniq 5",       809_000_000, STOK_AWAL, "ioniq5.jpg")));
        tambah(varianEV      (new Hyundai("Ioniq 6",     1_237_200_000, STOK_AWAL, "ioniq6.jpg")));
        tambah(varianEV      (new Hyundai("Kona Electric", 565_300_000, STOK_AWAL, "kona.jpg")));
        tambah(varianStandard(new Hyundai("Tucson",   "Bensin", 599_000_000, STOK_AWAL, "tucson.jpg")));
        tambah(varianStandard(new Hyundai("Palisade", "Diesel", 850_000_000, STOK_AWAL, "palisade.jpg")));
        tambah(varianStandard(new Hyundai("Santa Fe", "Bensin", 620_000_000, STOK_AWAL, "santafe.jpg")));
        tambah(varianStandard(new Hyundai("Stargazer","Bensin", 340_000_000, STOK_AWAL, "stargazer.jpg")));
    }

    private MobilRegistry() {}

    public static List<Mobil> all() { return mobils; }

    public static List<Mobil> byMerk(String merk) {
        List<Mobil> out = new ArrayList<>();
        for (Mobil m : mobils) {
            if (m.getMerk().equalsIgnoreCase(merk)) out.add(m);
        }
        return out;
    }

    // ---------- helpers ----------

    private static void tambah(Mobil m) { mobils.add(m); }

    /** 2 trim (Standard + Premium), 3 warna umum, 2 pilihan velg. */
    private static Mobil varianStandard(Mobil m) {
        m.tambahVarian("Standard", 0);
        m.tambahVarian("Premium",  35_000_000);
        m.tambahWarna("Putih Mutiara", "Hitam Metalik", "Silver Metalik");
        m.tambahVelg ("17-inch Steel", "18-inch Alloy");
        return m;
    }

    /** Varian khusus EV (penamaan trim sedikit beda). */
    private static Mobil varianEV(Mobil m) {
        m.tambahVarian("Standard Range", 0);
        m.tambahVarian("Long Range",     50_000_000);
        m.tambahWarna("Midnight Black", "Pearl White", "Electric Blue");
        m.tambahVelg ("19-inch Aero",   "20-inch Sport");
        return m;
    }
}
