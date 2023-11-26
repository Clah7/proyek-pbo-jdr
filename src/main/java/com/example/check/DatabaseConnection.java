package com.example.check;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
public class DatabaseConnection {
    public Connection databaseLink;

    public Connection getConnection(){
        String db = "library";
        String user = "root";
        String password = "";
        String url = "jdbc:mysql://localhost/" + db;

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            databaseLink = DriverManager.getConnection(url, user, password);
        } catch(Exception e){
            e.printStackTrace();
        }

        return databaseLink;
    }
}
