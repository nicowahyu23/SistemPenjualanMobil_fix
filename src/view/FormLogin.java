package view;

import model.User;
import service.AuthService;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class FormLogin extends JFrame {

    private static final Color BG    = new Color(245, 247, 250);
    private static final Color BLUE  = new Color(10, 36, 99);
    private static final Color BORDER= new Color(220, 225, 235);
    private static final Color TEXT  = new Color(20, 20, 40);
    private static final Color MUTED = new Color(100, 110, 130);

    private final JTextField     txtUser = new JTextField();
    private final JPasswordField txtPass = new JPasswordField();

    public FormLogin() {
        setTitle("Login \u2014 Showroom Mobil Nusantara");
        setSize(440, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG);

        add(buildHeader(),  BorderLayout.NORTH);
        add(buildForm(),    BorderLayout.CENTER);
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BLUE);
        header.setPreferredSize(new Dimension(440, 100));
        header.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        JLabel title = new JLabel("SHOWROOM MOBIL NUSANTARA");
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setForeground(Color.WHITE);

        JLabel sub = new JLabel("Premium Car Dealership \u2014 Login");
        sub.setFont(new Font("Arial", Font.PLAIN, 12));
        sub.setForeground(new Color(180, 200, 255));

        JPanel stack = new JPanel();
        stack.setLayout(new BoxLayout(stack, BoxLayout.Y_AXIS));
        stack.setBackground(BLUE);
        stack.add(title);
        stack.add(Box.createVerticalStrut(6));
        stack.add(sub);
        header.add(stack, BorderLayout.WEST);
        return header;
    }

    private JPanel buildForm() {
        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setBackground(BG);
        wrap.setBorder(BorderFactory.createEmptyBorder(28, 32, 28, 32));

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        Border line = BorderFactory.createLineBorder(BORDER, 1);
        Border pad  = BorderFactory.createEmptyBorder(24, 24, 24, 24);
        card.setBorder(BorderFactory.createCompoundBorder(line, pad));

        JLabel hi = new JLabel("Selamat Datang");
        hi.setFont(new Font("Arial", Font.BOLD, 18));
        hi.setForeground(TEXT);
        hi.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sub = new JLabel("Masuk untuk mulai bertransaksi");
        sub.setFont(new Font("Arial", Font.PLAIN, 12));
        sub.setForeground(MUTED);
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(hi);
        card.add(Box.createVerticalStrut(4));
        card.add(sub);
        card.add(Box.createVerticalStrut(20));

        card.add(label("Username"));
        styleField(txtUser);
        card.add(txtUser);
        card.add(Box.createVerticalStrut(14));

        card.add(label("Password"));
        styleField(txtPass);
        card.add(txtPass);
        card.add(Box.createVerticalStrut(20));

        JButton btnLogin = new JButton("MASUK");
        btnLogin.setFont(new Font("Arial", Font.BOLD, 13));
        btnLogin.setBackground(BLUE);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setOpaque(true);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        btnLogin.addActionListener(this::doLogin);

        // Submit on Enter key from password field
        txtPass.addActionListener(this::doLogin);
        txtUser.addActionListener(e -> txtPass.requestFocusInWindow());

        getRootPane().setDefaultButton(btnLogin);

        card.add(btnLogin);
        card.add(Box.createVerticalStrut(16));

        JLabel hint = new JLabel("<html><div style='color:#7a8090;font-size:11px;'>Akun demo:<br>"
            + "&bull; <b>admin</b> / admin (Administrator)<br>"
            + "&bull; <b>customer</b> / customer (Customer)</div></html>");
        hint.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(hint);

        wrap.add(card, BorderLayout.CENTER);
        return wrap;
    }

    private JLabel label(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Arial", Font.BOLD, 12));
        l.setForeground(MUTED);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        l.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));
        return l;
    }

    private void styleField(JTextField f) {
        f.setFont(new Font("Arial", Font.PLAIN, 13));
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        f.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private void doLogin(ActionEvent e) {
        String u = txtUser.getText().trim();
        String p = new String(txtPass.getPassword());
        User user = AuthService.login(u, p);
        if (user == null) {
            JOptionPane.showMessageDialog(this,
                "Username atau password salah.",
                "Login Gagal",
                JOptionPane.ERROR_MESSAGE);
            txtPass.setText("");
            txtPass.requestFocusInWindow();
            return;
        }
        dispose();
        SwingUtilities.invokeLater(() -> new FormPenjualan(user).setVisible(true));
    }
}
