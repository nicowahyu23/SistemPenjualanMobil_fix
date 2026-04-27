package model;

public class Customer extends User {
    private static final long serialVersionUID = 1L;

    public Customer(String username, String password, String namaLengkap) {
        super(username, password, namaLengkap);
    }

    @Override
    public String getRole() { return "Customer"; }

    @Override
    public boolean isAdmin() { return false; }
}
