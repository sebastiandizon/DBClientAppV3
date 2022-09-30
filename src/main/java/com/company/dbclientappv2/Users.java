package com.company.dbclientappv2;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static helper.JDBC.*;

public abstract class Users {
    public static int userID;
    public static boolean authorizeLogin(String username, String userPassword) {
        try {
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM users";
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                if (hashText(rs.getString("User_Name")).equals(hashText(username)) &&
                hashText(rs.getString("Password")).equals(hashText(userPassword))) {
                    userID = rs.getInt("User_ID");
                    return true;
                }
            }
        } catch(SQLException e){
                e.printStackTrace();
        }
            return false;
    }
    public static String hashText(String inputText){
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(inputText.getBytes());
            byte[] digest = md.digest();
            StringBuffer hexString = new StringBuffer();
            for (int i = 0;i<digest.length;i++) {
                hexString.append(Integer.toHexString(0xFF & digest[i]));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
