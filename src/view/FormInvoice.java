package view;

import model.Mobil;
import javax.swing.*;
import java.awt.*;
import java.awt.print.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FormInvoice extends JFrame implements Printable {

    private Mobil mobil;
    private String namaPembeli;
    private JTextArea txtNota;

    public FormInvoice(Mobil mobil, String namaPembeli) {
        this.mobil = mobil;
        this.namaPembeli = namaPembeli;

        setTitle("Invoice Penjualan");
        setSize(480, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(new Color(18, 18, 28));

        // Header
        JPanel header = new JPanel();
        header.setBackground(new Color(255, 200, 0));
        header.setBounds(0, 0, 480, 60);
        header.setLayout(null);
        add(header);

        JLabel lblJudul = new JLabel("🧾  NOTA PENJUALAN");
        lblJudul.setFont(new Font("Arial", Font.BOLD, 18));
        lblJudul.setForeground(new Color(18, 18, 28));
        lblJudul.setBounds(20, 15, 400, 30);
        header.add(lblJudul);

        // Nota area
        String tanggal = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
        String noNota = "INV-" + System.currentTimeMillis() % 100000;

        txtNota = new JTextArea();
        txtNota.setFont(new Font("Monospaced", Font.PLAIN, 13));
        txtNota.setBackground(new Color(28, 28, 42));
        txtNota.setForeground(Color.WHITE);
        txtNota.setEditable(false);
        txtNota.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        txtNota.setText(
            "  Showroom Mobil Nusantara\n" +
            "  Jl. Sudirman No.1, Jakarta\n" +
            "  Telp: 021-12345678\n" +
            "================================\n" +
            "  No. Nota : " + noNota + "\n" +
            "  Tanggal  : " + tanggal + "\n" +
            "================================\n" +
            "  Pembeli  : " + namaPembeli + "\n" +
            "================================\n" +
            "  DETAIL KENDARAAN\n" +
            "================================\n" +
            "  " + mobil.getDetailNota().replace("\n", "\n  ") + "\n" +
            "================================\n" +
            "  Terima kasih sudah membeli!\n" +
            "  Selamat menikmati kendaraan\n" +
            "  Anda.\n" +
            "================================"
        );

        JScrollPane scroll = new JScrollPane(txtNota);
        scroll.setBounds(20, 75, 435, 360);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(255, 200, 0), 1));
        add(scroll);

        // Tombol
        JButton btnPrint = new JButton("🖨 PRINT");
        btnPrint.setBounds(20, 450, 200, 45);
        btnPrint.setBackground(new Color(255, 200, 0));
        btnPrint.setForeground(new Color(18, 18, 28));
        btnPrint.setFont(new Font("Arial", Font.BOLD, 13));
        btnPrint.setBorderPainted(false);
        btnPrint.setCursor(new Cursor(Cursor.HAND_CURSOR));
        add(btnPrint);

        JButton btnTutup = new JButton("✖ TUTUP");
        btnTutup.setBounds(255, 450, 200, 45);
        btnTutup.setBackground(new Color(200, 50, 50));
        btnTutup.setForeground(Color.WHITE);
        btnTutup.setFont(new Font("Arial", Font.BOLD, 13));
        btnTutup.setBorderPainted(false);
        btnTutup.setCursor(new Cursor(Cursor.HAND_CURSOR));
        add(btnTutup);

        btnPrint.addActionListener(e -> {
            PrinterJob job = PrinterJob.getPrinterJob();
            job.setPrintable(this);
            if (job.printDialog()) {
                try { job.print(); }
                catch (PrinterException ex) {
                    JOptionPane.showMessageDialog(this, "Gagal print: " + ex.getMessage());
                }
            }
        });

        btnTutup.addActionListener(e -> dispose());
    }

    @Override
    public int print(Graphics g, PageFormat pf, int page) throws PrinterException {
        if (page > 0) return NO_SUCH_PAGE;
        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(pf.getImageableX(), pf.getImageableY());
        g.setFont(new Font("Monospaced", Font.PLAIN, 12));
        String[] lines = txtNota.getText().split("\n");
        int y = 20;
        for (String line : lines) {
            g.drawString(line, 10, y);
            y += 18;
        }
        return PAGE_EXISTS;
    }
}