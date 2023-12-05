package com.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Koneksi {
    Connection conn = null;

    public static Connection conDB() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/library", "root", "");
            return con;
        } catch (ClassNotFoundException | SQLException ex) {
            System.err.println("ConnectionUtil : " + ex.getMessage());
            return null;
        }
    }

    public static boolean query(String SQLString) {
        try (Connection con = conDB();
             PreparedStatement preparedStatement = con.prepareStatement(SQLString)) {
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException ex) {
            System.err.println("Error: " + ex.getMessage());
            return false;
        }
    }

    public static ResultSet getData(String SQLString) {
        try {
            Connection con = conDB();
            PreparedStatement preparedStatement = con.prepareStatement(SQLString);
            return preparedStatement.executeQuery();
        } catch (SQLException ex) {
            System.err.println("Error: " + ex.getMessage());
            return null;
        }
    }
}
