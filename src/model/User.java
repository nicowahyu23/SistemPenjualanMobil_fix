package model;

import java.io.Serializable;

/**
 * User abstrak. Subclass = Admin / Customer. Field password sengaja package-private
 * (diakses hanya via {@link #cocokPassword}) untuk mendemokan encapsulation.
 */
public abstract class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String username;
    private final String password;
    private final String namaLengkap;

    public User(String username, String password, String namaLengkap) {
        this.username = username;
        this.password = password;
        this.namaLengkap = namaLengkap;
    }

    public String getUsername()    { return username; }
    public String getNamaLengkap() { return namaLengkap; }

    boolean cocokPassword(String password) {
        return this.password.equals(password);
    }

    public abstract String getRole();
    public abstract boolean isAdmin();
}
