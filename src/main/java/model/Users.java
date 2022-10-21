package model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;

import static helper.JDBC.*;

public abstract class Users {
    public static int userId;
    public static String userName;
    /**
     Processes fields entered by user and compares them using usernames and passwords retrieved by database. Comparison is done
     with hashed values rather than plaintext. **/
    public static boolean authorizeLogin(String username, String userPassword) throws IOException {
        try {
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM users";
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                if (hashText(rs.getString("User_Name")).equals(hashText(username)) &&
                hashText(rs.getString("Password")).equals(hashText(userPassword))) {
                    userId = rs.getInt("User_ID");
                    userName = rs.getString("User_Name");
                    String append = userName + " logged in successfully at " + Instant.now() + "\n";
                    Files.write(Paths.get("login_activity.txt"), append.getBytes(), StandardOpenOption.APPEND);

                    return true;
                }
            }
        } catch(SQLException e){
                e.printStackTrace();
        }
        String append = "Unsuccessful login attempt at time: " + Instant.now() + "\n";
        Files.write(Paths.get("login_activity.txt"), append.getBytes(), StandardOpenOption.APPEND);
            return false;
    }
    /**
     Processes text upon retrieval and converts it to SHA-256. **/
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
