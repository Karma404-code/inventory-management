package org.example.authenticate;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AccountRepository {
    Connection conn;

    static void main() {
        AccountRepository ar = new  AccountRepository();
    }

    AccountRepository() {
        String url = "jdbc:h2:mem:inventory;DB_CLOSE_DELAY=-1;INIT=RUNSCRIPT FROM 'classpath:h2init.sql';USER=admin;PASSWORD=admin";
        String user = "admin";
        String password = "admin";
        try {
            conn =  DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean authenticate(String username, String password) {
        PreparedStatement ps;
        boolean result = false;
        try {
            ps = conn.prepareStatement("select * from account where username = ? and password = ?");
            ps.setString(1, username);
            ps.setString(2, password);
            result = ps.executeQuery().next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
