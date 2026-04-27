package service;

import model.Transaksi;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Persist transaksi ke file biner via Java Serialization. File disimpan di
 * {@code data/transaksi.dat} relatif terhadap working directory.
 */
public class RepositoryTransaksi {

    private static final File FILE = new File("data" + File.separator + "transaksi.dat");

    private RepositoryTransaksi() {}

    public static synchronized void simpan(Transaksi t) {
        List<Transaksi> all = muatSemua();
        all.add(t);
        tulis(all);
    }

    @SuppressWarnings("unchecked")
    public static synchronized List<Transaksi> muatSemua() {
        if (!FILE.exists()) return new ArrayList<>();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE))) {
            Object obj = in.readObject();
            if (obj instanceof List) return (List<Transaksi>) obj;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("[RepositoryTransaksi] gagal baca: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    private static void tulis(List<Transaksi> data) {
        try {
            File dir = FILE.getParentFile();
            if (dir != null && !dir.exists()) dir.mkdirs();
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE))) {
                out.writeObject(data);
            }
        } catch (IOException e) {
            System.err.println("[RepositoryTransaksi] gagal tulis: " + e.getMessage());
        }
    }
}
