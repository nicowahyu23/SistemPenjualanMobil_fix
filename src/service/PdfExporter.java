package service;

import model.Pembayaran;
import model.Transaksi;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Ekspor satu Transaksi jadi PDF nota A4 yang rapi pake Apache PDFBox 2.x.
 * Struktur dokumen mirip layout di FormInvoice (header, info pembelian,
 * trim, tampak luar, detail kendaraan, pembayaran, total bayar).
 */
public class PdfExporter {

    // ------- konstanta layout -------
    private static final PDRectangle PAGE = PDRectangle.A4;       // 595 x 842
    private static final float MARGIN     = 48f;
    private static final float CONTENT_W  = PAGE.getWidth() - 2 * MARGIN;

    private static final PDFont F_BOLD    = PDType1Font.HELVETICA_BOLD;
    private static final PDFont F_REG     = PDType1Font.HELVETICA;

    private static final Color C_BLUE     = new Color(10, 36, 99);
    private static final Color C_TEXT     = new Color(33, 37, 41);
    private static final Color C_MUTED    = new Color(120, 125, 135);
    private static final Color C_ACCENT   = new Color(180, 95, 6);
    private static final Color C_BORDER   = new Color(225, 228, 232);

    private static final NumberFormat RP =
        NumberFormat.getInstance(new Locale("id", "ID"));

    private PdfExporter() {}

    /** Export {@code t} ke {@code outFile}. Throws IOException kalau gagal nulis. */
    public static void export(Transaksi t, File outFile) throws IOException {
        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage(PAGE);
            doc.addPage(page);

            try (PDPageContentStream cs = new PDPageContentStream(doc, page)) {
                Cursor cur = new Cursor(cs, PAGE.getHeight() - MARGIN);

                drawHeader(cur, t);
                cur.gap(18);
                drawSectionInfoPembelian(cur, t);
                cur.gap(14);
                drawSectionTrim(cur, t);
                cur.gap(14);
                drawSectionTampakLuar(cur, t);
                cur.gap(14);
                drawSectionDetailKendaraan(cur, t);
                cur.gap(14);
                drawSectionPembayaran(cur, t);
                cur.gap(20);
                drawTotalBayar(cur, t);
                cur.gap(24);
                drawFooter(cur);
            }

            doc.save(outFile);
        }
    }

    // =================================================================
    //                       SECTION DRAWERS
    // =================================================================

    private static void drawHeader(Cursor c, Transaksi t) throws IOException {
        c.setFill(C_BLUE);
        c.rect(MARGIN, c.y - 56, CONTENT_W, 56, true);

        c.setFill(Color.WHITE);
        c.text(MARGIN + 18, c.y - 22, F_BOLD, 16, "SHOWROOM MOBIL NUSANTARA");
        c.text(MARGIN + 18, c.y - 40, F_REG, 10,
            "Jl. Sudirman No. 1, Jakarta  -  Premium Car Dealership");

        c.textRight(PAGE.getWidth() - MARGIN - 18, c.y - 22, F_BOLD, 14, "NOTA PEMBELIAN");
        c.textRight(PAGE.getWidth() - MARGIN - 18, c.y - 40, F_REG, 10, t.getNoNota());

        c.y -= 56;
    }

    private static void drawSectionInfoPembelian(Cursor c, Transaksi t) throws IOException {
        sectionTitle(c, "Informasi Pembelian");
        kvRow(c, "No. Nota",  t.getNoNota());
        kvRow(c, "Tanggal",   t.getTanggal().format(
            DateTimeFormatter.ofPattern("d MMM yyyy, HH:mm", new Locale("id"))));
        kvRow(c, "Pembeli",   t.getNamaPembeli());
        kvRow(c, "Petugas",   t.getUsernamePetugas());
    }

    private static void drawSectionTrim(Cursor c, Transaksi t) throws IOException {
        sectionTitle(c, "Trim");
        c.setFill(C_ACCENT);
        c.text(MARGIN, c.y - 14, F_REG, 11,
            t.getMerk() + " " + t.getTipe() + "  ( " + t.getVarianNama() + " )");
        c.setFill(C_TEXT);
        c.textRight(MARGIN + CONTENT_W, c.y - 14, F_BOLD, 12,
            "Rp. " + RP.format((long) t.getTotalHarga()));
        c.y -= 22;
    }

    private static void drawSectionTampakLuar(Cursor c, Transaksi t) throws IOException {
        sectionTitle(c, "Tampak Luar");
        kvRow(c, "Warna", t.getWarna());
        kvRow(c, "Velg",  t.getVelg());
    }

    private static void drawSectionDetailKendaraan(Cursor c, Transaksi t) throws IOException {
        sectionTitle(c, "Detail Kendaraan");
        kvRow(c, "Merk",         t.getMerk());
        kvRow(c, "Tipe",         t.getTipe());
        kvRow(c, "Bahan Bakar",  t.getJenisBB());
    }

    private static void drawSectionPembayaran(Cursor c, Transaksi t) throws IOException {
        sectionTitle(c, "Pembayaran");
        Pembayaran p = t.getPembayaran();
        for (String line : p.getRincian().split("\\n")) {
            int sep = line.indexOf(':');
            if (sep < 0) {
                kvRow(c, line.trim(), "");
            } else {
                kvRow(c, line.substring(0, sep).trim(),
                          line.substring(sep + 1).trim());
            }
        }
    }

    private static void drawTotalBayar(Cursor c, Transaksi t) throws IOException {
        c.setFill(C_BORDER);
        c.line(MARGIN, c.y - 4, MARGIN + CONTENT_W, c.y - 4);
        c.y -= 14;

        c.setFill(C_MUTED);
        c.text(MARGIN, c.y - 18, F_REG, 14, "TOTAL BAYAR");
        c.setFill(C_TEXT);
        c.textRight(MARGIN + CONTENT_W, c.y - 18, F_BOLD, 18,
            "Rp. " + RP.format((long) t.getPembayaran().getTotalBayar()));
        c.y -= 30;
    }

    private static void drawFooter(Cursor c) throws IOException {
        c.setFill(C_MUTED);
        c.textCenter(MARGIN + CONTENT_W / 2f, c.y - 16, F_REG, 9,
            "Terima kasih atas pembelian Anda. Dokumen ini sah tanpa tanda tangan basah.");
    }

    // =================================================================
    //                          UI HELPERS
    // =================================================================

    private static void sectionTitle(Cursor c, String title) throws IOException {
        c.setFill(C_TEXT);
        c.text(MARGIN, c.y - 14, F_BOLD, 12, title);
        c.setFill(C_BORDER);
        c.line(MARGIN, c.y - 20, MARGIN + CONTENT_W, c.y - 20);
        c.y -= 26;
    }

    /** key kiri (muted) + value kanan (text). */
    private static void kvRow(Cursor c, String key, String val) throws IOException {
        c.setFill(C_MUTED);
        c.text(MARGIN + 4, c.y - 12, F_REG, 10, key);
        c.setFill(C_TEXT);
        c.textRight(MARGIN + CONTENT_W - 4, c.y - 12, F_REG, 10, val);
        c.y -= 16;
    }

    // =================================================================
    //   Cursor: helper kecil biar drawing-nya gak full of getY()/setY().
    // =================================================================

    private static class Cursor {
        final PDPageContentStream cs;
        float y;

        Cursor(PDPageContentStream cs, float y) {
            this.cs = cs;
            this.y  = y;
        }

        void gap(float dy) { y -= dy; }

        void setFill(Color color) throws IOException {
            cs.setNonStrokingColor(color);
            cs.setStrokingColor(color);
        }

        void rect(float x, float y, float w, float h, boolean fill) throws IOException {
            cs.addRect(x, y, w, h);
            if (fill) cs.fill(); else cs.stroke();
        }

        void line(float x1, float y1, float x2, float y2) throws IOException {
            cs.moveTo(x1, y1);
            cs.lineTo(x2, y2);
            cs.stroke();
        }

        void text(float x, float y, PDFont font, float size, String s) throws IOException {
            cs.beginText();
            cs.setFont(font, size);
            cs.newLineAtOffset(x, y);
            cs.showText(safe(s));
            cs.endText();
        }

        void textRight(float xRight, float y, PDFont font, float size, String s) throws IOException {
            float w = font.getStringWidth(safe(s)) / 1000f * size;
            text(xRight - w, y, font, size, s);
        }

        void textCenter(float xCenter, float y, PDFont font, float size, String s) throws IOException {
            float w = font.getStringWidth(safe(s)) / 1000f * size;
            text(xCenter - w / 2f, y, font, size, s);
        }

        /** Helvetica built-in cuma support WinAnsi -> ganti char unicode aneh. */
        private String safe(String s) {
            if (s == null) return "";
            return s
                .replace('\u2022', '-')   // bullet -> dash
                .replace('\u2013', '-')   // en-dash
                .replace('\u2014', '-');  // em-dash
        }
    }
}
