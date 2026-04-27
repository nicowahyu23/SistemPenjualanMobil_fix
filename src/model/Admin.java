package model;

public class Admin extends User {
    private static final long serialVersionUID = 1L;

    public Admin(String username, String password, String namaLengkap) {
        super(username, password, namaLengkap);
    }

    @Override
    public String getRole() { return "Admin"; }

    @Override
    public boolean isAdmin() { return true; }
}
