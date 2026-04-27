package view;

import model.*;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Pilih metode pembayaran (Tunai / Kredit). Untuk Kredit, isi DP%, tenor, bunga.
 * Cicilan/bln diupdate real-time.
 */
public class FormPembayaran extends JFrame {

    private static final Color BG          = new Color(245, 247, 250);
    private static final Color CARD        = Color.WHITE;
    private static final Color BORDER      = new Color(225, 228, 232);
    private static final Color TEXT        = new Color(33, 37, 41);
    private static final Color TEXT_MUTED  = new Color(120, 125, 135);
    private static final Color ACCENT      = new Color(180, 95, 6);
    private static final Color BTN_PRIMARY = new Color(10, 36, 99);
    private static final Color BTN_GHOST   = new Color(120, 125, 135);

    private final FormPenjualan parent;
    private final User           userLogin;
    private final String         namaPembeli;
    private final Mobil          mobil;
    private final VarianMobil    varian;
    private final String         warna;
    private final String         velg;
    private final double         hargaPokok;

    private final JRadioButton rbTunai  = new JRadioButton("Tunai (Cash)");
    private final JRadioButton rbKredit = new JRadioButton("Kredit / Cicilan");

    private final JComboBox<Integer> cbDP    = new JComboBox<>(new Integer[]{10, 20, 30, 50});
    private final JComboBox<Integer> cbTenor = new JComboBox<>(new Integer[]{12, 24, 36, 48, 60});
    private final JComboBox<Double>  cbBunga = new JComboBox<>(new Double[]{4.5, 6.0, 7.5, 9.0});

    private JPanel kreditCard;
    private JLabel lblDP, lblPokok, lblBunga, lblCicilan, lblTotal;

    public FormPembayaran(FormPenjualan parent,
                          User userLogin,
                          String namaPembeli,
                          Mobil mobil,
                          VarianMobil varian,
                          String warna,
                          String velg) {
        this.parent      = parent;
        this.userLogin   = userLogin;
        this.namaPembeli = namaPembeli;
        this.mobil       = mobil;
        this.varian      = varian;
        this.warna       = warna;
        this.velg        = velg;
        this.hargaPokok  = mobil.getHarga() + (varian == null ? 0 : varian.getTambahanHarga());

        setTitle("Metode Pembayaran");
        setSize(720, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG);

        JPanel paper = new JPanel();
        paper.setLayout(new BoxLayout(paper, BoxLayout.Y_AXIS));
        paper.setBackground(BG);
        paper.setBorder(BorderFactory.createEmptyBorder(28, 36, 28, 36));

        paper.add(buildHeader());
        paper.add(Box.createVerticalStrut(18));
        paper.add(buildPilihMetode());
        paper.add(Box.createVerticalStrut(18));
        kreditCard = buildKredit();
        paper.add(kreditCard);
        paper.add(Box.createVerticalStrut(22));
        paper.add(buildTotal());
        paper.add(Box.createVerticalStrut(8));

        JScrollPane scroll = new JScrollPane(paper);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(BG);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);
        add(buildActions(), BorderLayout.SOUTH);

        // initial state: tunai
        rbTunai.setSelected(true);
        toggleKredit(false);
        updateBreakdown();
    }

    // ---------- builders ----------

    private JPanel buildHeader() {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(BG);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JLabel title = new JLabel("Metode Pembayaran");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(TEXT);

        JLabel sub = new JLabel(mobil.getMerk() + " " + mobil.getTipe()
            + (varian == null ? "" : " \u2022 " + varian.getNama()));
        sub.setFont(new Font("Arial", Font.PLAIN, 13));
        sub.setForeground(TEXT_MUTED);

        row.add(title, BorderLayout.WEST);
        row.add(sub,   BorderLayout.EAST);
        return row;
    }

    private JPanel buildPilihMetode() {
        JPanel c = card();
        c.add(sectionTitle("Pilih Metode"));
        c.add(divider());

        ButtonGroup g = new ButtonGroup();
        g.add(rbTunai);
        g.add(rbKredit);

        styleRadio(rbTunai);
        styleRadio(rbKredit);

        JPanel rowTunai = methodRow(rbTunai,
            "Bayar lunas. Total = harga mobil.",
            formatRupiah(hargaPokok));
        JPanel rowKredit = methodRow(rbKredit,
            "Cicil dengan DP & bunga flat per tahun.",
            "Cicilan/bln dihitung");
        c.add(rowTunai);
        c.add(rowKredit);

        rbTunai.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                toggleKredit(false);
                updateBreakdown();
            }
        });
        rbKredit.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                toggleKredit(true);
                updateBreakdown();
            }
        });
        return c;
    }

    private JPanel buildKredit() {
        JPanel c = card();
        c.add(sectionTitle("Pengaturan Kredit"));
        c.add(divider());

        c.add(comboRow("DP",    cbDP,    "% dari harga pokok"));
        c.add(comboRow("Tenor", cbTenor, "bulan"));
        c.add(comboRow("Bunga", cbBunga, "% per tahun (flat)"));

        c.add(Box.createVerticalStrut(10));
        c.add(sectionTitle("Rincian"));
        c.add(divider());
        lblPokok   = breakdownRow(c, "Harga Pokok",   formatRupiah(hargaPokok), false);
        lblDP      = breakdownRow(c, "DP",            "-", false);
        lblBunga   = breakdownRow(c, "Total Bunga",   "-", false);
        lblCicilan = breakdownRow(c, "Cicilan / Bln", "-", true);

        cbDP.setSelectedItem(20);
        cbTenor.setSelectedItem(36);
        cbBunga.setSelectedItem(6.0);

        cbDP.addActionListener(e -> updateBreakdown());
        cbTenor.addActionListener(e -> updateBreakdown());
        cbBunga.addActionListener(e -> updateBreakdown());

        return c;
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

        lblTotal = new JLabel(formatRupiah(hargaPokok));
        lblTotal.setFont(new Font("Arial", Font.BOLD, 26));
        lblTotal.setForeground(TEXT);
        lblTotal.setHorizontalAlignment(SwingConstants.RIGHT);

        row.add(lbl,      BorderLayout.WEST);
        row.add(lblTotal, BorderLayout.EAST);
        return row;
    }

    private JPanel buildActions() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 14));
        bar.setBackground(CARD);
        bar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER));

        JButton kembali = flatButton("Kembali", BTN_GHOST, Color.WHITE);
        kembali.addActionListener(e -> {
            dispose();
            new FormDetailMobil(parent, userLogin, namaPembeli, mobil).setVisible(true);
        });

        JButton konfirmasi = flatButton("Konfirmasi & Buat Nota  \u2192", BTN_PRIMARY, Color.WHITE);
        konfirmasi.addActionListener(e -> {
            Pembayaran p = buildPembayaran();
            dispose();
            new FormInvoice(parent, userLogin, namaPembeli, mobil, varian, warna, velg, p)
                .setVisible(true);
        });

        bar.add(kembali);
        bar.add(konfirmasi);
        return bar;
    }

    // ---------- helpers ----------

    private void toggleKredit(boolean enabled) {
        kreditCard.setVisible(enabled);
        revalidate();
        repaint();
    }

    private Pembayaran buildPembayaran() {
        if (rbTunai.isSelected()) {
            return new PembayaranTunai(hargaPokok);
        }
        int    dp    = (int)    cbDP.getSelectedItem();
        int    tenor = (int)    cbTenor.getSelectedItem();
        double bunga = (double) cbBunga.getSelectedItem();
        return new PembayaranKredit(hargaPokok, dp, tenor, bunga);
    }

    private void updateBreakdown() {
        if (lblTotal == null) return; // not yet built
        if (rbTunai.isSelected()) {
            lblTotal.setText(formatRupiah(hargaPokok));
            return;
        }
        PembayaranKredit pk = (PembayaranKredit) buildPembayaran();
        if (lblPokok   != null) lblPokok.setText(formatRupiah(pk.getPokokDicicil()));
        if (lblDP      != null) lblDP.setText(formatRupiah(pk.getDP()));
        if (lblBunga   != null) lblBunga.setText(formatRupiah(pk.getTotalBunga()));
        if (lblCicilan != null) lblCicilan.setText(formatRupiah(pk.getCicilanPerBulan()));
        lblTotal.setText(formatRupiah(pk.getTotalBayar()));
    }

    // ---------- small UI helpers ----------

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

    private void styleRadio(JRadioButton r) {
        r.setFont(new Font("Arial", Font.BOLD, 14));
        r.setBackground(CARD);
        r.setForeground(ACCENT);
        r.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private JPanel methodRow(JRadioButton rb, String desc, String price) {
        JPanel row = new JPanel(new BorderLayout(12, 0));
        row.setBackground(CARD);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setBackground(CARD);
        left.add(rb);
        JLabel d = new JLabel("    " + desc);
        d.setFont(new Font("Arial", Font.PLAIN, 12));
        d.setForeground(TEXT_MUTED);
        left.add(d);

        JLabel p = new JLabel(price);
        p.setFont(new Font("Arial", Font.PLAIN, 13));
        p.setForeground(TEXT);
        p.setHorizontalAlignment(SwingConstants.RIGHT);

        row.add(left, BorderLayout.CENTER);
        row.add(p,    BorderLayout.EAST);
        return row;
    }

    private JPanel comboRow(String label, JComboBox<?> combo, String suffix) {
        JPanel row = new JPanel(new BorderLayout(12, 0));
        row.setBackground(CARD);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));

        JLabel l = new JLabel(label);
        l.setFont(new Font("Arial", Font.PLAIN, 13));
        l.setForeground(TEXT);
        l.setPreferredSize(new Dimension(120, 22));

        combo.setFont(new Font("Arial", Font.PLAIN, 13));
        combo.setBackground(Color.WHITE);

        JLabel s = new JLabel("  " + suffix);
        s.setFont(new Font("Arial", Font.PLAIN, 12));
        s.setForeground(TEXT_MUTED);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        right.setBackground(CARD);
        right.add(combo);
        right.add(s);

        row.add(l,     BorderLayout.WEST);
        row.add(right, BorderLayout.CENTER);
        return row;
    }

    private JLabel breakdownRow(JPanel parent, String label, String value, boolean accent) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(CARD);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setBorder(BorderFactory.createEmptyBorder(6, 0, 6, 0));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));

        JLabel l = new JLabel(label);
        l.setFont(new Font("Arial", Font.PLAIN, 13));
        l.setForeground(accent ? ACCENT : TEXT_MUTED);

        JLabel v = new JLabel(value);
        v.setFont(new Font("Arial", accent ? Font.BOLD : Font.PLAIN, 13));
        v.setForeground(accent ? ACCENT : TEXT);
        v.setHorizontalAlignment(SwingConstants.RIGHT);

        row.add(l, BorderLayout.WEST);
        row.add(v, BorderLayout.EAST);
        parent.add(row);
        return v;
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

    private static String formatRupiah(double v) {
        NumberFormat nf = NumberFormat.getInstance(new Locale("id", "ID"));
        return "Rp. " + nf.format((long) v);
    }
}
