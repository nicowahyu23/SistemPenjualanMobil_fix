package view;

import model.*;
import service.PdfExporter;
import service.RepositoryTransaksi;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.print.*;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class FormInvoice extends JFrame implements Printable {

    private static final Color BG          = new Color(245, 247, 250);
    private static final Color CARD        = Color.WHITE;
    private static final Color BORDER      = new Color(225, 228, 232);
    private static final Color TEXT        = new Color(33, 37, 41);
    private static final Color TEXT_MUTED  = new Color(120, 125, 135);
    private static final Color ACCENT      = new Color(180, 95, 6);
    private static final Color BTN_PRIMARY = new Color(10, 36, 99);
    private static final Color BTN_DANGER  = new Color(200, 50, 50);

    private final FormPenjualan parent;
    private final Transaksi     transaksi;
    private JPanel paperPanel;

    public FormInvoice(FormPenjualan parent,
                       User userLogin,
                       String namaPembeli,
                       Mobil mobil,
                       VarianMobil varian,
                       String warna,
                       String velg,
                       Pembayaran pembayaran) {
        this.parent = parent;
        this.transaksi = new Transaksi(
            "INV-" + (System.currentTimeMillis() % 100000),
            LocalDateTime.now(),
            (namaPembeli == null || namaPembeli.trim().isEmpty()) ? "-" : namaPembeli.trim(),
            userLogin.getUsername(),
            mobil, varian, warna, velg, pembayaran
        );

        // Persist transaksi & decrement stok exactly once when invoice is opened.
        mobil.kurangiStok();
        RepositoryTransaksi.simpan(transaksi);
        if (parent != null) parent.refreshSetelahTransaksi();

        initUi();
    }

    /** Constructor sekunder: untuk view nota lama dari FormRiwayat (tanpa side-effect). */
    public FormInvoice(Transaksi t) {
        this.parent    = null;
        this.transaksi = t;
        initUi();
    }

    private void initUi() {
        setTitle("Nota " + transaksi.getNoNota());
        setSize(720, 820);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(BG);
        setLayout(new BorderLayout());

        paperPanel = buildPaper();
        JScrollPane scroll = new JScrollPane(paperPanel);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(BG);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);

        add(buildActions(), BorderLayout.SOUTH);
    }

    // ---------- builders ----------

    private JPanel buildPaper() {
        JPanel paper = new JPanel();
        paper.setLayout(new BoxLayout(paper, BoxLayout.Y_AXIS));
        paper.setBackground(BG);
        paper.setBorder(BorderFactory.createEmptyBorder(28, 36, 28, 36));

        paper.add(buildHeader());
        paper.add(Box.createVerticalStrut(18));
        paper.add(buildInfoCard());
        paper.add(Box.createVerticalStrut(18));
        paper.add(buildKendaraanCard());
        paper.add(Box.createVerticalStrut(18));
        paper.add(buildPembayaranCard());
        paper.add(Box.createVerticalStrut(22));
        paper.add(buildTotal());
        paper.add(Box.createVerticalStrut(8));

        return paper;
    }

    private JPanel buildHeader() {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(BG);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JLabel title = new JLabel("Ringkasan");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(TEXT);

        JLabel share = new JLabel("\u2197  Bagikan");
        share.setFont(new Font("Arial", Font.PLAIN, 13));
        share.setForeground(TEXT_MUTED);

        row.add(title, BorderLayout.WEST);
        row.add(share, BorderLayout.EAST);
        return row;
    }

    private JPanel buildInfoCard() {
        JPanel card = card();
        String tanggal = transaksi.getTanggal()
            .format(DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm"));

        card.add(sectionTitle("Informasi Pembelian"));
        card.add(divider());
        card.add(infoRow("Showroom", "Mobil Nusantara \u2013 Jl. Sudirman No.1, Jakarta"));
        card.add(infoRow("No. Nota", transaksi.getNoNota()));
        card.add(infoRow("Tanggal",  tanggal));
        card.add(infoRow("Pembeli",  transaksi.getNamaPembeli()));
        card.add(infoRow("Petugas",  transaksi.getUsernamePetugas()));
        return card;
    }

    private JPanel buildKendaraanCard() {
        JPanel card = card();
        card.add(sectionTitle("Trim"));
        card.add(divider());
        card.add(lineItem(
            transaksi.getMerk() + " " + transaksi.getTipe()
                + "  ( " + transaksi.getVarianNama() + " )",
            formatRupiah(transaksi.getHargaMobil() + transaksi.getVarianTambahan())
        ));

        card.add(Box.createVerticalStrut(12));
        card.add(sectionTitle("Tampak Luar"));
        card.add(divider());
        card.add(lineItem(transaksi.getWarna(), "+Rp 0", false));
        card.add(lineItem(transaksi.getVelg(),  "+Rp 0", false));

        card.add(Box.createVerticalStrut(12));
        card.add(sectionTitle("Detail Kendaraan"));
        card.add(divider());
        card.add(lineItem("Merk",        transaksi.getMerk(),    false));
        card.add(lineItem("Tipe",        transaksi.getTipe(),    false));
        card.add(lineItem("Bahan Bakar", transaksi.getJenisBB(), false));
        return card;
    }

    private JPanel buildPembayaranCard() {
        JPanel card = card();
        card.add(sectionTitle("Pembayaran"));
        card.add(divider());

        Pembayaran p = transaksi.getPembayaran();
        for (String line : p.getRincian().split("\n")) {
            // tiap baris dipecah ke "label : value" untuk tampilan rapi
            String[] parts = line.split(":", 2);
            if (parts.length == 2) {
                card.add(infoRow(parts[0].trim(), parts[1].trim()));
            } else {
                card.add(infoRow(line, ""));
            }
        }
        return card;
    }

    private JPanel buildTotal() {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(BG);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setBorder(BorderFactory.createEmptyBorder(8, 4, 8, 4));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 64));

        JLabel lbl = new JLabel("Total Bayar");
        lbl.setFont(new Font("Arial", Font.PLAIN, 22));
        lbl.setForeground(TEXT_MUTED);

        JLabel val = new JLabel(formatRupiah(transaksi.getPembayaran().getTotalBayar()));
        val.setFont(new Font("Arial", Font.BOLD, 26));
        val.setForeground(TEXT);
        val.setHorizontalAlignment(SwingConstants.RIGHT);

        row.add(lbl, BorderLayout.WEST);
        row.add(val, BorderLayout.EAST);
        return row;
    }

    private JPanel buildActions() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 14));
        bar.setBackground(CARD);
        bar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER));

        JButton btnTutup = flatButton("Tutup", BTN_DANGER, Color.WHITE);
        btnTutup.addActionListener(e -> dispose());

        JButton btnPrint = flatButton("Cetak Nota (PDF)", BTN_PRIMARY, Color.WHITE);
        btnPrint.addActionListener(e -> ekspordanCetakPdf());

        bar.add(btnTutup);
        bar.add(btnPrint);
        return bar;
    }

    /** Tombol "Cetak Nota": ekspor ke PDF, simpan, lalu coba auto-buka. */
    private void ekspordanCetakPdf() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Simpan Nota PDF");
        chooser.setSelectedFile(new File("Nota_" + transaksi.getNoNota() + ".pdf"));
        chooser.setFileFilter(new FileNameExtensionFilter("PDF (*.pdf)", "pdf"));
        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;

        File file = chooser.getSelectedFile();
        if (!file.getName().toLowerCase(Locale.ROOT).endsWith(".pdf")) {
            file = new File(file.getParentFile(), file.getName() + ".pdf");
        }

        try {
            PdfExporter.export(transaksi, file);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                "Gagal menulis PDF: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Coba auto-open. Kalau gak didukung OS, kasih tau path-nya doang.
        boolean opened = false;
        if (Desktop.isDesktopSupported()) {
            Desktop d = Desktop.getDesktop();
            if (d.isSupported(Desktop.Action.OPEN)) {
                try { d.open(file); opened = true; }
                catch (IOException ignored) {}
            }
        }
        JOptionPane.showMessageDialog(this,
            "Nota PDF berhasil disimpan di:\n" + file.getAbsolutePath()
                + (opened ? "\n\nFile sudah dibuka di viewer default." : ""),
            "Berhasil", JOptionPane.INFORMATION_MESSAGE);
    }

    // ---------- small helpers ----------

    private JPanel card() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(CARD);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        Border line = BorderFactory.createLineBorder(BORDER, 1);
        Border pad  = BorderFactory.createEmptyBorder(18, 22, 18, 22);
        p.setBorder(BorderFactory.createCompoundBorder(line, pad));
        return p;
    }

    private JLabel sectionTitle(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Arial", Font.BOLD, 15));
        l.setForeground(TEXT);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        l.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        return l;
    }

    private JComponent divider() {
        JPanel d = new JPanel();
        d.setBackground(BORDER);
        d.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        d.setPreferredSize(new Dimension(10, 1));
        d.setAlignmentX(Component.LEFT_ALIGNMENT);
        return d;
    }

    private JPanel infoRow(String label, String value) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(CARD);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));

        JLabel l = new JLabel(label);
        l.setFont(new Font("Arial", Font.PLAIN, 13));
        l.setForeground(TEXT_MUTED);
        l.setPreferredSize(new Dimension(180, 20));

        JLabel v = new JLabel(value);
        v.setFont(new Font("Arial", Font.PLAIN, 13));
        v.setForeground(TEXT);
        v.setHorizontalAlignment(SwingConstants.RIGHT);

        row.add(l, BorderLayout.WEST);
        row.add(v, BorderLayout.CENTER);
        return row;
    }

    private JPanel lineItem(String label, String value, boolean accent) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(CARD);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));

        JLabel l = new JLabel("\u2022   " + label);
        l.setFont(new Font("Arial", Font.PLAIN, 14));
        l.setForeground(accent ? ACCENT : TEXT);

        JLabel v = new JLabel(value);
        v.setFont(new Font("Arial", accent ? Font.BOLD : Font.PLAIN, 14));
        v.setForeground(accent ? ACCENT : TEXT);
        v.setHorizontalAlignment(SwingConstants.RIGHT);

        row.add(l, BorderLayout.WEST);
        row.add(v, BorderLayout.EAST);
        return row;
    }

    private JPanel lineItem(String label, String value) {
        return lineItem(label, value, true);
    }

    private JButton flatButton(String text, Color bg, Color fg) {
        JButton b = new JButton(text);
        b.setFont(new Font("Arial", Font.BOLD, 13));
        b.setBackground(bg);
        b.setForeground(fg);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setOpaque(true);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createEmptyBorder(10, 22, 10, 22));
        return b;
    }

    private static String formatRupiah(double value) {
        NumberFormat nf = NumberFormat.getInstance(new Locale("id", "ID"));
        return "Rp. " + nf.format((long) value);
    }

    // ---------- printing ----------

    @Override
    public int print(Graphics g, PageFormat pf, int page) throws PrinterException {
        if (page > 0) return NO_SUCH_PAGE;
        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(pf.getImageableX(), pf.getImageableY());

        double scale = Math.min(
            pf.getImageableWidth()  / paperPanel.getWidth(),
            pf.getImageableHeight() / paperPanel.getHeight()
        );
        if (scale < 1.0) g2d.scale(scale, scale);

        paperPanel.printAll(g2d);
        return PAGE_EXISTS;
    }
}
