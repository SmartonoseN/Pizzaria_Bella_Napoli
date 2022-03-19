package com.simeon.pizzaria_bella_napoli;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class dataSource {
    public static void main(String[] args) {
        connect();
    }

    public static void connect(){
        Connection conn = null;

        try {
            String url= "jdbc:sqlite:database.db";

            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
