package view;

import model.Transaksi;
import service.RepositoryTransaksi;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

/**
 * Riwayat transaksi — hanya dapat dibuka oleh Admin.
 * Sumber data: {@link RepositoryTransaksi#muatSemua()}.
 */
public class FormRiwayat extends JDialog {

    private static final Color BG     = new Color(245, 247, 250);
    private static final Color BLUE   = new Color(10, 36, 99);
    private static final Color BORDER = new Color(220, 225, 235);
    private static final Color TEXT   = new Color(33, 37, 41);
    private static final Color MUTED  = new Color(120, 125, 135);

    private final DefaultTableModel model;
    private List<Transaksi> rows;

    public FormRiwayat(JFrame owner) {
        super(owner, "Riwayat Transaksi", true);
        setSize(900, 580);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG);

        add(buildHeader(), BorderLayout.NORTH);

        String[] cols = {"No. Nota", "Tanggal", "Pembeli", "Mobil", "Varian", "Metode", "Total Bayar"};
        model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);
        table.setRowHeight(28);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(235, 240, 255));
        table.setSelectionForeground(TEXT);

        JTableHeader h = table.getTableHeader();
        h.setBackground(BLUE);
        h.setForeground(Color.WHITE);
        h.setFont(new Font("Arial", Font.BOLD, 12));
        h.setPreferredSize(new Dimension(h.getPreferredSize().width, 32));

        DefaultTableCellRenderer right = new DefaultTableCellRenderer();
        right.setHorizontalAlignment(SwingConstants.RIGHT);
        table.getColumnModel().getColumn(6).setCellRenderer(right);

        loadRows();

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    if (row >= 0 && row < rows.size()) {
                        new FormInvoice(rows.get(row)).setVisible(true);
                    }
                }
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder(0, 16, 16, 16));
        scroll.getViewport().setBackground(Color.WHITE);
        add(scroll, BorderLayout.CENTER);

        add(buildFooter(), BorderLayout.SOUTH);
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG);
        header.setBorder(BorderFactory.createEmptyBorder(16, 18, 8, 18));

        JLabel title = new JLabel("Riwayat Transaksi");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(TEXT);

        JLabel hint = new JLabel("Klik dua kali baris untuk membuka nota.");
        hint.setFont(new Font("Arial", Font.PLAIN, 12));
        hint.setForeground(MUTED);

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setBackground(BG);
        left.add(title);
        left.add(Box.createVerticalStrut(2));
        left.add(hint);

        header.add(left, BorderLayout.WEST);
        return header;
    }

    private JPanel buildFooter() {
        JPanel f = new JPanel(new BorderLayout());
        f.setBackground(Color.WHITE);
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER),
            BorderFactory.createEmptyBorder(10, 18, 10, 18)
        ));

        double totalPendapatan = 0;
        for (Transaksi t : rows) totalPendapatan += t.getPembayaran().getTotalBayar();

        JLabel summary = new JLabel("Total: " + rows.size() + " transaksi"
            + "    \u2022    Pendapatan: " + formatRupiah(totalPendapatan));
        summary.setFont(new Font("Arial", Font.BOLD, 13));
        summary.setForeground(TEXT);

        JButton tutup = new JButton("Tutup");
        tutup.setFont(new Font("Arial", Font.BOLD, 12));
        tutup.setBackground(BLUE);
        tutup.setForeground(Color.WHITE);
        tutup.setFocusPainted(false);
        tutup.setBorderPainted(false);
        tutup.setOpaque(true);
        tutup.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        tutup.setCursor(new Cursor(Cursor.HAND_CURSOR));
        tutup.addActionListener(e -> dispose());

        f.add(summary, BorderLayout.WEST);
        f.add(tutup,   BorderLayout.EAST);
        return f;
    }

    private void loadRows() {
        rows = RepositoryTransaksi.muatSemua();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm");
        for (Transaksi t : rows) {
            model.addRow(new Object[]{
                t.getNoNota(),
                t.getTanggal().format(fmt),
                t.getNamaPembeli(),
                t.getMerk() + " " + t.getTipe(),
                t.getVarianNama(),
                t.getPembayaran().getMetode(),
                formatRupiah(t.getPembayaran().getTotalBayar())
            });
        }
    }

    private static String formatRupiah(double v) {
        NumberFormat nf = NumberFormat.getInstance(new Locale("id", "ID"));
        return "Rp. " + nf.format((long) v);
    }
}
