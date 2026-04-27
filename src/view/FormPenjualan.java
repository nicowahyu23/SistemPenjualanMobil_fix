package view;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;
import model.*;

public class FormPenjualan extends JFrame {

    private String merkDipilih = "Toyota";
    private String filterBB = "Semua";
    private String tipeDipilih = "";
    private double hargaDipilih = 0;
    private JTextField txtNama;
    private JPanel panelList;
    private JLabel lblMerkActive;
    private JPanel panelBottom;
    private JLabel lblSelectedInfo;

    Color BG = new Color(245, 247, 250);
    Color BG_DARK = new Color(8, 20, 50);
    Color BLUE = new Color(10, 36, 99);
    Color BLUE_BTN = new Color(15, 55, 150);
    Color ACCENT = new Color(0, 120, 215);
    Color TEXT = new Color(20, 20, 40);
    Color TEXT_GRAY = new Color(100, 110, 130);
    Color BORDER = new Color(220, 225, 235);

    Object[][] dataToyota = {
        {"Fortuner", 543000000.0, "fortuner.jpg", "Bensin"},
        {"Innova", 417800000.0, "innova.jpg", "Diesel"},
        {"Avanza", 243700000.0, "avanza.jpg", "Bensin"},
        {"Rush", 272400000.0, "rush.jpg", "Bensin"},
        {"Yaris", 330900000.0, "yaris.jpg", "Bensin"}
    };

    Object[][] dataHonda = {
        {"Brio", 165900000.0, "brio.jpg", "Bensin"},
        {"HR-V", 389900000.0, "hrv.jpg", "Bensin"},
        {"Civic", 539900000.0, "civic.jpg", "Bensin"},
        {"Jazz", 340000000.0, "jazz.jpg", "Bensin"}
    };

    Object[][] dataHyundai = {
        {"Ioniq 5", 809000000.0, "ioniq5.jpg", "Listrik"},
        {"Ioniq 6", 1237200000.0, "ioniq6.jpg", "Listrik"},
        {"Kona Electric", 565300000.0, "kona.jpg", "Listrik"},
        {"Tucson", 599000000.0, "tucson.jpg", "Bensin"}
    };

    public FormPenjualan() {
        setTitle("Showroom Mobil Nusantara");
        setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG);

        add(buatHeader(), BorderLayout.NORTH);
        add(buatSidebar(), BorderLayout.WEST);
        add(buatMain(), BorderLayout.CENTER);

        tampilkanList();
        setVisible(true);
    }

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

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 15));
        right.setBackground(BLUE);

        JLabel lblNamaLabel = new JLabel("Nama Pembeli:");
        lblNamaLabel.setForeground(new Color(180, 200, 255));
        lblNamaLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        txtNama = new JTextField(15);
        txtNama.setFont(new Font("Arial", Font.PLAIN, 13));
        txtNama.setBackground(new Color(30, 60, 130));
        txtNama.setForeground(Color.WHITE);
        txtNama.setCaretColor(Color.WHITE);
        txtNama.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 140, 220)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        right.add(lblNamaLabel);
        right.add(txtNama);
        header.add(right, BorderLayout.EAST);
        return header;
    }

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

        String[] merks = {"Toyota", "Honda", "Hyundai"};
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
                    if (!merkDipilih.equals(merk)) {
                        item.setBackground(new Color(245, 247, 255));
                    }
                }
                public void mouseExited(MouseEvent e) {
                    item.setBackground(merkDipilih.equals(merk) ? new Color(235, 240, 255) : Color.WHITE);
                }
                public void mouseClicked(MouseEvent e) {
                    merkDipilih = merk;
                    filterBB = "Semua";
                    tipeDipilih = "";
                    hargaDipilih = 0;
                    panelBottom.setVisible(false);
                    refreshSidebar();
                    tampilkanList();
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
        add(buatHeader(), BorderLayout.NORTH);
        add(buatSidebar(), BorderLayout.WEST);
        add(buatMain(), BorderLayout.CENTER);
        revalidate();
        repaint();
        tampilkanList();
    }

    private JPanel buatMain() {
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(BG);

        // Tab filter BB
        JPanel tabPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        tabPanel.setBackground(Color.WHITE);
        tabPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER));
        tabPanel.setPreferredSize(new Dimension(800, 50));

        String[] filters = {"Semua", "Bensin", "Diesel", "Listrik"};
        for (String f : filters) {
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
                tampilkanList();
                refreshTabs(tabPanel, f, filters);
            });

            tabPanel.add(tab);
        }

        main.add(tabPanel, BorderLayout.NORTH);

        // List mobil
        panelList = new JPanel();
        panelList.setLayout(new BoxLayout(panelList, BoxLayout.Y_AXIS));
        panelList.setBackground(BG);
        panelList.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JScrollPane scroll = new JScrollPane(panelList);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(BG);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        main.add(scroll, BorderLayout.CENTER);

        // Bottom bar
        panelBottom = new JPanel(new BorderLayout());
        panelBottom.setBackground(BLUE);
        panelBottom.setPreferredSize(new Dimension(800, 65));
        panelBottom.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        panelBottom.setVisible(false);

        lblSelectedInfo = new JLabel("Belum ada yang dipilih");
        lblSelectedInfo.setFont(new Font("Arial", Font.BOLD, 14));
        lblSelectedInfo.setForeground(Color.WHITE);
        panelBottom.add(lblSelectedInfo, BorderLayout.WEST);

        JButton btnBeli = new JButton("BELI  →");
        btnBeli.setFont(new Font("Arial", Font.BOLD, 14));
        btnBeli.setBackground(Color.WHITE);
        btnBeli.setForeground(BLUE);
        btnBeli.setBorderPainted(false);
        btnBeli.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBeli.setPreferredSize(new Dimension(130, 42));
        btnBeli.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btnBeli.setBackground(new Color(220, 230, 255));
            }
            public void mouseExited(MouseEvent e) {
                btnBeli.setBackground(Color.WHITE);
            }
        });

        btnBeli.addActionListener(e -> {
            if (txtNama.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Masukkan nama pembeli dulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Mobil mobil;
            if (merkDipilih.equals("Toyota")) {
                mobil = new Toyota(tipeDipilih, cariJenisBB(dataToyota, tipeDipilih), hargaDipilih);
            } else if (merkDipilih.equals("Honda")) {
                mobil = new Honda(tipeDipilih, cariJenisBB(dataHonda, tipeDipilih), hargaDipilih);
            } else {
                mobil = new Hyundai(tipeDipilih, hargaDipilih);
            }
            new FormInvoice(mobil, txtNama.getText()).setVisible(true);
        });

        panelBottom.add(btnBeli, BorderLayout.EAST);
        main.add(panelBottom, BorderLayout.SOUTH);

        return main;
    }

    private void refreshTabs(JPanel tabPanel, String active, String[] filters) {
        tabPanel.removeAll();
        for (String f : filters) {
            JButton tab = new JButton(f);
            tab.setFont(new Font("Arial", Font.BOLD, 13));
            tab.setPreferredSize(new Dimension(110, 50));
            tab.setBorderPainted(false);
            tab.setCursor(new Cursor(Cursor.HAND_CURSOR));
            tab.setFocusPainted(false);
            if (f.equals(active)) {
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
                tampilkanList();
                refreshTabs(tabPanel, f, filters);
            });
            tabPanel.add(tab);
        }
        tabPanel.revalidate();
        tabPanel.repaint();
    }

    private String cariJenisBB(Object[][] data, String tipe) {
        for (Object[] row : data) {
            if (row[0].equals(tipe)) return (String) row[3];
        }
        return "Bensin";
    }

    private void tampilkanList() {
        if (panelList == null) return;
        panelList.removeAll();

        Object[][] data;
        if (merkDipilih.equals("Toyota")) data = dataToyota;
        else if (merkDipilih.equals("Honda")) data = dataHonda;
        else data = dataHyundai;

        boolean ada = false;
        for (Object[] mobil : data) {
            String tipe = (String) mobil[0];
            double harga = (double) mobil[1];
            String gambar = (String) mobil[2];
            String bb = (String) mobil[3];

            if (!filterBB.equals("Semua") && !bb.equals(filterBB)) continue;
            ada = true;

            // Card list
            JPanel card = new JPanel(new BorderLayout());
            card.setBackground(Color.WHITE);
            card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));
            card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER),
                BorderFactory.createEmptyBorder(15, 15, 15, 20)
            ));
            card.setCursor(new Cursor(Cursor.HAND_CURSOR));

            // Foto
            JLabel lblFoto = new JLabel();
            lblFoto.setPreferredSize(new Dimension(200, 130));
            lblFoto.setHorizontalAlignment(SwingConstants.CENTER);
            lblFoto.setBackground(new Color(245, 247, 250));
            lblFoto.setOpaque(true);

            try {
                String path = System.getProperty("user.dir") + "\\src\\images\\" + gambar;
                File imgFile = new File(path);
                if (imgFile.exists()) {
                    Image img = ImageIO.read(imgFile).getScaledInstance(200, 130, Image.SCALE_SMOOTH);
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

            JLabel lblTipe = new JLabel(merkDipilih + " " + tipe);
            lblTipe.setFont(new Font("Arial", Font.BOLD, 18));
            lblTipe.setForeground(TEXT);

            Color bbColor = bb.equals("Listrik") ? new Color(0, 150, 80) :
                            bb.equals("Diesel") ? new Color(180, 90, 0) :
                            ACCENT;
            JLabel lblBB = new JLabel(bb);
            lblBB.setFont(new Font("Arial", Font.PLAIN, 12));
            lblBB.setForeground(bbColor);
            lblBB.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bbColor),
                BorderFactory.createEmptyBorder(2, 8, 2, 8)
            ));

            JLabel lblDari = new JLabel("Dari");
            lblDari.setFont(new Font("Arial", Font.PLAIN, 12));
            lblDari.setForeground(TEXT_GRAY);

            JLabel lblHarga = new JLabel("Rp. " + String.format("%,.0f", harga));
            lblHarga.setFont(new Font("Arial", Font.BOLD, 16));
            lblHarga.setForeground(TEXT);

            JButton btnBeli = new JButton("Beli");
            btnBeli.setFont(new Font("Arial", Font.BOLD, 13));
            btnBeli.setBackground(BLUE_BTN);
            btnBeli.setForeground(Color.WHITE);
            btnBeli.setBorderPainted(false);
            btnBeli.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnBeli.setPreferredSize(new Dimension(120, 35));
            btnBeli.setMaximumSize(new Dimension(120, 35));
            btnBeli.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { btnBeli.setBackground(ACCENT); }
                public void mouseExited(MouseEvent e) { btnBeli.setBackground(BLUE_BTN); }
            });

            btnBeli.addActionListener(e -> {
                if (txtNama.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Masukkan nama pembeli dulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                Mobil m;
                if (merkDipilih.equals("Toyota")) m = new Toyota(tipe, bb, harga);
                else if (merkDipilih.equals("Honda")) m = new Honda(tipe, bb, harga);
                else m = new Hyundai(tipe, harga);
                new FormInvoice(m, txtNama.getText()).setVisible(true);
            });

            info.add(lblTipe);
            info.add(Box.createVerticalStrut(8));
            info.add(lblBB);
            info.add(Box.createVerticalStrut(10));
            info.add(lblDari);
            info.add(Box.createVerticalStrut(2));
            info.add(lblHarga);
            info.add(Box.createVerticalStrut(12));
            info.add(btnBeli);

            card.add(info, BorderLayout.CENTER);

            card.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    card.setBackground(new Color(248, 250, 255));
                    info.setBackground(new Color(248, 250, 255));
                }
                public void mouseExited(MouseEvent e) {
                    card.setBackground(Color.WHITE);
                    info.setBackground(Color.WHITE);
                }
            });

            panelList.add(card);
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
}