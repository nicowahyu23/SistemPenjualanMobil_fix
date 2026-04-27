package service;

import model.Admin;
import model.Customer;
import model.User;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * In-memory user store. Untuk demo: ada 1 admin & 1 customer default.
 * login() mengembalikan User kalau cocok, atau null kalau gagal.
 */
public class AuthService {

    private static final List<User> users = new ArrayList<>();
    static {
        users.add(new Admin   ("admin",    "admin",    "Administrator"));
        users.add(new Customer("customer", "customer", "Customer Demo"));
    }

    private AuthService() {}

    public static User login(String username, String password) {
        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(username) && cocokPassword(u, password)) {
                return u;
            }
        }
        return null;
    }

    /**
     * Reflection trick supaya AuthService bisa mengakses package-private
     * method {@code cocokPassword(String)} di {@link User} tanpa harus
     * menaruh class ini di package model.
     */
    private static boolean cocokPassword(User u, String password) {
        try {
            Method m = User.class.getDeclaredMethod("cocokPassword", String.class);
            m.setAccessible(true);
            return (Boolean) m.invoke(u, password);
        } catch (ReflectiveOperationException e) {
            return false;
        }
    }
}
