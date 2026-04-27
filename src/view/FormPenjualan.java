package view;

import model.Mobil;
import model.User;
import service.MobilRegistry;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class FormPenjualan extends JFrame {

    private final User userLogin;

    private String merkDipilih = "Toyota";
    private String filterBB    = "Semua";
    /** untuk admin: nama customer yang sedang dilayani; untuk customer: nama dirinya sendiri */
    private String namaPembeliTerakhir;
    private JPanel  panelList;
    private JPanel  panelTabContainer;

    private final Color BG        = new Color(245, 247, 250);
    private final Color BLUE      = new Color(10, 36, 99);
    private final Color BLUE_BTN  = new Color(15, 55, 150);
    private final Color ACCENT    = new Color(0, 120, 215);
    private final Color TEXT      = new Color(20, 20, 40);
    private final Color TEXT_GRAY = new Color(100, 110, 130);
    private final Color BORDER    = new Color(220, 225, 235);
    private final Color GREEN     = new Color(0, 150, 80);
    private final Color RED       = new Color(200, 50, 50);

    public FormPenjualan(User userLogin) {
        this.userLogin = userLogin;
        this.namaPembeliTerakhir = userLogin.isAdmin() ? "" : userLogin.getNamaLengkap();
        setTitle("Showroom Mobil Nusantara \u2014 " + userLogin.getRole());
        setSize(1000, 720);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG);

        add(buatHeader(),  BorderLayout.NORTH);
        add(buatSidebar(), BorderLayout.WEST);
        add(buatMain(),    BorderLayout.CENTER);

        tampilkanList();
    }

    // ---------- HEADER ----------

    private JPanel buatHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BLUE);
        header.setPreferredSize(new Dimension(1000, 65));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        left.setBackground(BLUE);

        JLabel lblTitle = new JLabel("SHOWROOM MOBIL NUSANTARA");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel("  Premium Car Dealership");
        lblSub.setFont(new Font("Arial", Font.PLAIN, 12));
        lblSub.setForeground(new Color(180, 200, 255));

        left.add(lblTitle);
        left.add(lblSub);
        header.add(left, BorderLayout.WEST);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 14));
        right.setBackground(BLUE);

        JLabel lblUser = new JLabel(userLogin.getNamaLengkap()
            + "  (" + userLogin.getRole() + ")");
        lblUser.setForeground(Color.WHITE);
        lblUser.setFont(new Font("Arial", Font.BOLD, 12));
        right.add(lblUser);

        if (userLogin.isAdmin()) {
            JButton btnRiwayat = headerButton("Riwayat Transaksi");
            btnRiwayat.addActionListener(e ->
                new FormRiwayat(this).setVisible(true));
            right.add(btnRiwayat);
        }

        JButton btnLogout = headerButton("Logout");
        btnLogout.addActionListener(e -> {
            dispose();
            new FormLogin().setVisible(true);
        });
        right.add(btnLogout);

        header.add(right, BorderLayout.EAST);
        return header;
    }

    private JButton headerButton(String text) {
        JButton b = new JButton(text);
        b.setFont(new Font("Arial", Font.BOLD, 12));
        b.setBackground(new Color(30, 60, 130));
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setOpaque(true);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 140, 220)),
            BorderFactory.createEmptyBorder(5, 12, 5, 12)
        ));
        return b;
    }

    // ---------- SIDEBAR ----------

    private JPanel buatSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(Color.WHITE);
        sidebar.setPreferredSize(new Dimension(170, 600));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, BORDER));

        JLabel lblBrand = new JLabel("  BRAND");
        lblBrand.setFont(new Font("Arial", Font.BOLD, 11));
        lblBrand.setForeground(TEXT_GRAY);
        lblBrand.setBorder(BorderFactory.createEmptyBorder(25, 15, 10, 0));
        lblBrand.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(lblBrand);

        String[] merks = {"Toyota", "Mitsubishi", "Hyundai"};
        for (String merk : merks) {
            JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
            item.setBackground(merk.equals(merkDipilih) ? new Color(235, 240, 255) : Color.WHITE);
            item.setMaximumSize(new Dimension(170, 45));
            item.setAlignmentX(Component.LEFT_ALIGNMENT);
            item.setCursor(new Cursor(Cursor.HAND_CURSOR));

            JLabel lblMerk = new JLabel(merk);
            lblMerk.setFont(new Font("Arial", Font.BOLD, 14));
            lblMerk.setForeground(merk.equals(merkDipilih) ? BLUE : TEXT);
            item.add(lblMerk);

            item.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    if (!merkDipilih.equals(merk))
                        item.setBackground(new Color(245, 247, 255));
                }
                public void mouseExited(MouseEvent e) {
                    item.setBackground(merkDipilih.equals(merk) ? new Color(235, 240, 255) : Color.WHITE);
                }
                public void mouseClicked(MouseEvent e) {
                    merkDipilih = merk;
                    filterBB    = "Semua";
                    refreshSidebar();
                }
            });

            sidebar.add(item);
            JSeparator sep = new JSeparator();
            sep.setMaximumSize(new Dimension(170, 1));
            sep.setForeground(BORDER);
            sidebar.add(sep);
        }

        sidebar.add(Box.createVerticalGlue());
        return sidebar;
    }

    private void refreshSidebar() {
        getContentPane().removeAll();
        add(buatHeader(),  BorderLayout.NORTH);
        add(buatSidebar(), BorderLayout.WEST);
        add(buatMain(),    BorderLayout.CENTER);
        revalidate();
        repaint();
        tampilkanList();
    }

    // ---------- MAIN ----------

    private JPanel buatMain() {
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(BG);

        // Tab filter BB (per-merk dynamic)
        panelTabContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelTabContainer.setBackground(Color.WHITE);
        panelTabContainer.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER));
        panelTabContainer.setPreferredSize(new Dimension(800, 50));

        renderTabs();

        main.add(panelTabContainer, BorderLayout.NORTH);

        panelList = new JPanel();
        panelList.setLayout(new BoxLayout(panelList, BoxLayout.Y_AXIS));
        panelList.setBackground(BG);
        panelList.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JScrollPane scroll = new JScrollPane(panelList);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(BG);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        main.add(scroll, BorderLayout.CENTER);

        return main;
    }

    /** Build filter tab list (Semua + setiap jenis BB yang ada di merk yang aktif). */
    private String[] buildFilters() {
        Set<String> filters = new LinkedHashSet<>();
        filters.add("Semua");
        for (Mobil m : MobilRegistry.byMerk(merkDipilih)) {
            filters.add(m.getJenisBB());
        }
        return filters.toArray(new String[0]);
    }

    private void renderTabs() {
        panelTabContainer.removeAll();
        for (String f : buildFilters()) {
            JButton tab = new JButton(f);
            tab.setFont(new Font("Arial", Font.BOLD, 13));
            tab.setPreferredSize(new Dimension(110, 50));
            tab.setBorderPainted(false);
            tab.setCursor(new Cursor(Cursor.HAND_CURSOR));
            tab.setFocusPainted(false);
            if (f.equals(filterBB)) {
                tab.setBackground(Color.WHITE);
                tab.setForeground(BLUE);
                tab.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, BLUE));
            } else {
                tab.setBackground(Color.WHITE);
                tab.setForeground(TEXT_GRAY);
                tab.setBorder(BorderFactory.createEmptyBorder());
            }
            tab.addActionListener(e -> {
                filterBB = f;
                renderTabs();
                tampilkanList();
            });
            panelTabContainer.add(tab);
        }
        panelTabContainer.revalidate();
        panelTabContainer.repaint();
    }

    void tampilkanList() {
        if (panelList == null) return;
        panelList.removeAll();

        List<Mobil> data = MobilRegistry.byMerk(merkDipilih);
        boolean ada = false;

        for (Mobil m : data) {
            if (!filterBB.equals("Semua") && !m.getJenisBB().equals(filterBB)) continue;
            ada = true;
            panelList.add(buildCard(m));
            panelList.add(Box.createVerticalStrut(5));
        }

        if (!ada) {
            JLabel lblKosong = new JLabel("Tidak ada mobil " + filterBB + " untuk " + merkDipilih);
            lblKosong.setFont(new Font("Arial", Font.ITALIC, 14));
            lblKosong.setForeground(TEXT_GRAY);
            lblKosong.setAlignmentX(Component.CENTER_ALIGNMENT);
            lblKosong.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0));
            panelList.add(lblKosong);
        }

        panelList.revalidate();
        panelList.repaint();
    }

    private JPanel buildCard(Mobil m) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 170));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER),
            BorderFactory.createEmptyBorder(15, 15, 15, 20)
        ));

        // Foto
        JLabel lblFoto = new JLabel();
        lblFoto.setPreferredSize(new Dimension(200, 130));
        lblFoto.setHorizontalAlignment(SwingConstants.CENTER);
        lblFoto.setBackground(new Color(245, 247, 250));
        lblFoto.setOpaque(true);
        try {
            File f = new File("src" + File.separator + "images" + File.separator + m.getGambar());
            if (f.exists()) {
                Image img = ImageIO.read(f).getScaledInstance(200, 130, Image.SCALE_SMOOTH);
                lblFoto.setIcon(new ImageIcon(img));
            } else {
                lblFoto.setText("No Image");
                lblFoto.setForeground(TEXT_GRAY);
            }
        } catch (Exception ex) {
            lblFoto.setText("Error");
        }
        card.add(lblFoto, BorderLayout.WEST);

        // Info
        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setBackground(Color.WHITE);
        info.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 0));

        JLabel lblTipe = new JLabel(m.getMerk() + " " + m.getTipe());
        lblTipe.setFont(new Font("Arial", Font.BOLD, 18));
        lblTipe.setForeground(TEXT);

        Color bbColor = "Listrik".equals(m.getJenisBB()) ? GREEN
                       : "Diesel".equals(m.getJenisBB()) ? new Color(180, 90, 0)
                       : ACCENT;
        JLabel lblBB = new JLabel(m.getJenisBB());
        lblBB.setFont(new Font("Arial", Font.PLAIN, 12));
        lblBB.setForeground(bbColor);
        lblBB.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bbColor),
            BorderFactory.createEmptyBorder(2, 8, 2, 8)
        ));

        JLabel lblDari = new JLabel("Dari");
        lblDari.setFont(new Font("Arial", Font.PLAIN, 12));
        lblDari.setForeground(TEXT_GRAY);

        JLabel lblHarga = new JLabel("Rp. " + String.format("%,.0f", m.getHarga()));
        lblHarga.setFont(new Font("Arial", Font.BOLD, 16));
        lblHarga.setForeground(TEXT);

        info.add(lblTipe);
        info.add(Box.createVerticalStrut(8));
        info.add(lblBB);
        info.add(Box.createVerticalStrut(10));
        info.add(lblDari);
        info.add(Box.createVerticalStrut(2));
        info.add(lblHarga);
        card.add(info, BorderLayout.CENTER);

        // Right col: stok + tombol Beli
        JPanel right = new JPanel();
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.setBackground(Color.WHITE);
        right.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JLabel lblStokTitle = new JLabel("Stok");
        lblStokTitle.setFont(new Font("Arial", Font.PLAIN, 11));
        lblStokTitle.setForeground(TEXT_GRAY);
        lblStokTitle.setAlignmentX(Component.RIGHT_ALIGNMENT);

        boolean habis = m.getStok() <= 0;
        JLabel lblStok = new JLabel(habis ? "HABIS" : (m.getStok() + " unit"));
        lblStok.setFont(new Font("Arial", Font.BOLD, 14));
        lblStok.setForeground(habis ? RED : GREEN);
        lblStok.setAlignmentX(Component.RIGHT_ALIGNMENT);

        JButton btnBeli = new JButton(habis ? "Stok Habis" : "Beli");
        btnBeli.setFont(new Font("Arial", Font.BOLD, 13));
        btnBeli.setBackground(habis ? new Color(200, 205, 215) : BLUE_BTN);
        btnBeli.setForeground(Color.WHITE);
        btnBeli.setBorderPainted(false);
        btnBeli.setFocusPainted(false);
        btnBeli.setOpaque(true);
        btnBeli.setEnabled(!habis);
        btnBeli.setCursor(new Cursor(habis ? Cursor.DEFAULT_CURSOR : Cursor.HAND_CURSOR));
        btnBeli.setMaximumSize(new Dimension(140, 38));
        btnBeli.setAlignmentX(Component.RIGHT_ALIGNMENT);
        btnBeli.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (btnBeli.isEnabled()) btnBeli.setBackground(ACCENT);
            }
            public void mouseExited(MouseEvent e) {
                if (btnBeli.isEnabled()) btnBeli.setBackground(BLUE_BTN);
            }
        });
        btnBeli.addActionListener(e -> bukaDetail(m));

        right.add(lblStokTitle);
        right.add(Box.createVerticalStrut(2));
        right.add(lblStok);
        right.add(Box.createVerticalGlue());
        right.add(btnBeli);
        card.add(right, BorderLayout.EAST);

        return card;
    }

    private void bukaDetail(Mobil m) {
        String namaPembeli;
        if (userLogin.isAdmin()) {
            String prefill = namaPembeliTerakhir == null ? "" : namaPembeliTerakhir;
            String input = (String) JOptionPane.showInputDialog(this,
                "Masukkan nama customer yang membeli " + m.getMerk() + " " + m.getTipe() + ":",
                "Nama Pembeli",
                JOptionPane.QUESTION_MESSAGE,
                null, null, prefill);
            if (input == null) return;
            namaPembeli = input.trim();
            if (namaPembeli.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Nama pembeli tidak boleh kosong.",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }
            namaPembeliTerakhir = namaPembeli;
        } else {
            namaPembeli = userLogin.getNamaLengkap();
        }
        new FormDetailMobil(this, userLogin, namaPembeli, m).setVisible(true);
    }

    /** Dipanggil dari FormInvoice setelah pembelian sukses & stok berkurang. */
    public void refreshSetelahTransaksi() {
        tampilkanList();
    }
}
