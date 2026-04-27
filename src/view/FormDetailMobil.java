package view;

import model.Mobil;
import model.User;
import model.VarianMobil;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.File;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Halaman detail mobil: pilih trim/varian, warna, dan velg.
 * Total harga ter-update real-time. Tombol "Lanjut Bayar" buka FormPembayaran.
 */
public class FormDetailMobil extends JFrame {

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

    private final ButtonGroup grpVarian = new ButtonGroup();
    private final ButtonGroup grpWarna  = new ButtonGroup();
    private final ButtonGroup grpVelg   = new ButtonGroup();

    private VarianMobil varianDipilih;
    private String      warnaDipilih;
    private String      velgDipilih;

    private JLabel lblTotal;

    public FormDetailMobil(FormPenjualan parent, User userLogin, String namaPembeli, Mobil mobil) {
        this.parent      = parent;
        this.userLogin   = userLogin;
        this.namaPembeli = namaPembeli;
        this.mobil       = mobil;

        setTitle("Konfigurasi \u2014 " + mobil.getMerk() + " " + mobil.getTipe());
        setSize(820, 760);
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
        paper.add(buildPreview());
        paper.add(Box.createVerticalStrut(18));
        paper.add(buildTrimCard());
        paper.add(Box.createVerticalStrut(18));
        paper.add(buildTampakLuarCard());
        paper.add(Box.createVerticalStrut(22));
        paper.add(buildTotal());
        paper.add(Box.createVerticalStrut(8));

        JScrollPane scroll = new JScrollPane(paper);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(BG);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);
        add(buildActions(), BorderLayout.SOUTH);

        updateTotal();
    }

    // ---------- builders ----------

    private JPanel buildHeader() {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(BG);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JLabel title = new JLabel("Konfigurasi Mobil");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(TEXT);

        JLabel sub = new JLabel(mobil.getMerk() + " " + mobil.getTipe()
            + " \u2022 " + mobil.getJenisBB());
        sub.setFont(new Font("Arial", Font.PLAIN, 13));
        sub.setForeground(TEXT_MUTED);

        row.add(title, BorderLayout.WEST);
        row.add(sub,   BorderLayout.EAST);
        return row;
    }

    private JPanel buildPreview() {
        JPanel p = card();
        p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));

        JLabel lblFoto = new JLabel();
        lblFoto.setPreferredSize(new Dimension(280, 180));
        lblFoto.setMaximumSize(new Dimension(280, 180));
        lblFoto.setHorizontalAlignment(SwingConstants.CENTER);
        lblFoto.setBackground(BG);
        lblFoto.setOpaque(true);
        try {
            File f = new File("src" + File.separator + "images" + File.separator + mobil.getGambar());
            if (f.exists()) {
                Image img = ImageIO.read(f).getScaledInstance(280, 180, Image.SCALE_SMOOTH);
                lblFoto.setIcon(new ImageIcon(img));
            } else {
                lblFoto.setText("No Image");
                lblFoto.setForeground(TEXT_MUTED);
            }
        } catch (Exception ex) {
            lblFoto.setText("Error");
        }
        p.add(lblFoto);

        p.add(Box.createHorizontalStrut(24));

        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setBackground(CARD);

        JLabel name = new JLabel(mobil.getMerk() + " " + mobil.getTipe());
        name.setFont(new Font("Arial", Font.BOLD, 22));
        name.setForeground(TEXT);
        name.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sub = new JLabel("Harga mulai");
        sub.setFont(new Font("Arial", Font.PLAIN, 12));
        sub.setForeground(TEXT_MUTED);
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel price = new JLabel(formatRupiah(mobil.getHarga()));
        price.setFont(new Font("Arial", Font.BOLD, 20));
        price.setForeground(TEXT);
        price.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel stok = new JLabel("Stok tersedia: " + mobil.getStok() + " unit");
        stok.setFont(new Font("Arial", Font.PLAIN, 12));
        stok.setForeground(new Color(0, 150, 80));
        stok.setAlignmentX(Component.LEFT_ALIGNMENT);

        info.add(name);
        info.add(Box.createVerticalStrut(8));
        info.add(sub);
        info.add(price);
        info.add(Box.createVerticalStrut(12));
        info.add(stok);

        p.add(info);
        p.add(Box.createHorizontalGlue());

        return p;
    }

    private JPanel buildTrimCard() {
        JPanel c = card();
        c.add(sectionTitle("Trim"));
        c.add(divider());
        boolean first = true;
        for (VarianMobil v : mobil.getVarianList()) {
            JPanel row = optionRow(v.getNama(),
                                   tambahanLabel(v.getTambahanHarga()),
                                   true);
            JRadioButton rb = (JRadioButton) row.getClientProperty("radio");
            grpVarian.add(rb);
            if (first) {
                rb.setSelected(true);
                varianDipilih = v;
                first = false;
            }
            rb.addActionListener(e -> {
                varianDipilih = v;
                updateTotal();
            });
            c.add(row);
        }
        return c;
    }

    private JPanel buildTampakLuarCard() {
        JPanel c = card();
        c.add(sectionTitle("Tampak Luar"));
        c.add(divider());

        c.add(subSection("Warna"));
        boolean first = true;
        for (String w : mobil.getWarnaList()) {
            JPanel row = optionRow(w, "+Rp 0", false);
            JRadioButton rb = (JRadioButton) row.getClientProperty("radio");
            grpWarna.add(rb);
            if (first) { rb.setSelected(true); warnaDipilih = w; first = false; }
            rb.addActionListener(e -> warnaDipilih = w);
            c.add(row);
        }

        c.add(Box.createVerticalStrut(10));
        c.add(subSection("Velg"));
        first = true;
        for (String v : mobil.getVelgList()) {
            JPanel row = optionRow(v, "+Rp 0", false);
            JRadioButton rb = (JRadioButton) row.getClientProperty("radio");
            grpVelg.add(rb);
            if (first) { rb.setSelected(true); velgDipilih = v; first = false; }
            rb.addActionListener(e -> velgDipilih = v);
            c.add(row);
        }

        return c;
    }

    private JPanel buildTotal() {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(BG);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setBorder(BorderFactory.createEmptyBorder(8, 4, 8, 4));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 64));

        JLabel lbl = new JLabel("Total Harga");
        lbl.setFont(new Font("Arial", Font.PLAIN, 22));
        lbl.setForeground(TEXT_MUTED);

        lblTotal = new JLabel(formatRupiah(mobil.getHarga()));
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

        JButton batal = flatButton("Batal", BTN_GHOST, Color.WHITE);
        batal.addActionListener(e -> dispose());

        JButton lanjut = flatButton("Lanjut Bayar  \u2192", BTN_PRIMARY, Color.WHITE);
        lanjut.addActionListener(e -> {
            dispose();
            new FormPembayaran(parent, userLogin, namaPembeli, mobil,
                               varianDipilih, warnaDipilih, velgDipilih).setVisible(true);
        });

        bar.add(batal);
        bar.add(lanjut);
        return bar;
    }

    // ---------- helpers ----------

    private void updateTotal() {
        double total = mobil.getHarga() + (varianDipilih == null ? 0 : varianDipilih.getTambahanHarga());
        lblTotal.setText(formatRupiah(total));
    }

    private static String tambahanLabel(double v) {
        if (v == 0) return "+Rp 0";
        return "+Rp " + String.format("%,d", (long) v);
    }

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

    private JLabel subSection(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Arial", Font.BOLD, 12));
        l.setForeground(TEXT_MUTED);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        l.setBorder(BorderFactory.createEmptyBorder(8, 0, 4, 0));
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

    private JPanel optionRow(String label, String price, boolean accent) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(CARD);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));

        JRadioButton rb = new JRadioButton(label);
        rb.setBackground(CARD);
        rb.setFont(new Font("Arial", Font.PLAIN, 14));
        rb.setForeground(accent ? ACCENT : TEXT);
        rb.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel right = new JLabel(price);
        right.setFont(new Font("Arial", accent ? Font.BOLD : Font.PLAIN, 13));
        right.setForeground(accent ? ACCENT : TEXT);
        right.setHorizontalAlignment(SwingConstants.RIGHT);

        row.add(rb,    BorderLayout.WEST);
        row.add(right, BorderLayout.EAST);
        row.putClientProperty("radio", rb);
        return row;
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
