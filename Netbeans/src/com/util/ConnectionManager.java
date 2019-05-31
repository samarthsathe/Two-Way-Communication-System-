/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.util;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

import org.apache.commons.net.DatabaseHelper;

public class ConnectionManager {

    public static void main(String[] args) {
        addIMEI();
    }

    public static void addIMEI() {
        String imei = JOptionPane.showInputDialog("Please enter IMEI No to be added");
        if (imei != null) {
            registerAdminPhone(imei);
        }

    }

    public static Connection getDBConnection() {

        Connection conn = null;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:"
                    + getDatabasePath());

            System.out.println("Got Connection " + getDatabasePath());
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Please start the mysql Service from XAMPP Console.");

        }

        return conn;
    }

    private static void closeConnection(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public static String getDatabasePath() {
        File f = new File(".");
        String dbpath = "";
        try {
            dbpath = f.getCanonicalPath() + "/db/NetworkBuddy.db";
            File db = new File(dbpath);
            if (!db.exists()) {
                JOptionPane.showMessageDialog(null,
                        "Databse Path Does Not Exist!!!");
            } else {
                return dbpath;
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    public static boolean registerAdminPhone(String imei) {
        String query = "Insert into UserAccounts (imei) values('" + imei + "')";
        int i = executeUpdate(query);
        if (i > 0) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean checkRegisteredPhone(String imei) {
        String query = "select * from UserAccounts where IMEI LIKE '" + imei + "'";
        String userId = getMaxColumnValue(query);
        System.out.println("userId " + userId);
        if (userId.length() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public static int executeUpdate(String sQuery) {
        Connection con = null;
        Statement stmt = null;

        int i = -1;
        try {
            con = ConnectionManager.getDBConnection();
            stmt = con.createStatement();
            System.out.println(sQuery);
            i = stmt.executeUpdate(sQuery);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return i;
    }

    public static String getMaxColumnValue(String query) {
        String max = "";
        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;
        if (query.length() > 0) {
            try {
                connection = ConnectionManager.getDBConnection();
                stmt = connection.createStatement();
                System.out.println(query);
                rs = stmt.executeQuery(query);
                if (rs.next()) {
                    max = rs.getString(1);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    rs.close();
                    connection.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }
        return max;

    }
}
