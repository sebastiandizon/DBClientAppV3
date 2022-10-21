package helper;
import java.sql.*;
import java.sql.Connection;

import java.util.TimeZone;

public abstract class JDBC {
    private static final String protocol = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String location = "//localhost/";
    private static final String databaseName = "client_schedule";
    static TimeZone userTimeZone = TimeZone.getDefault();
    private static final String timeZone = "?connectionTimeZone = " + userTimeZone.getID();
    private static final String jdbcUrl = protocol + vendor + location + databaseName + timeZone; // LOCAL
    private static final String driver = "com.mysql.cj.jdbc.Driver"; // Driver reference
    private static final String userName = "sqlUser"; // Username
    private static String password = "Passw0rd!"; // Password
    public static Connection connection;  // Connection Interface

    public static void openConnection()
    {
        try {
            System.out.println(userTimeZone);
            Class.forName(driver); // Locate Driver
            connection = DriverManager.getConnection(jdbcUrl, userName, password); // Reference Connection object
            System.out.println("Connection successful!");
        }
        catch(Exception e)
        {
            System.out.println("Error:" + e.getMessage());
        }
    }
    public static void closeConnection() {
        try {
            connection.close();
            System.out.println("Connection closed!");
        }
        catch(Exception e)
        {
            System.out.println("Error:" + e.getMessage());
        }
    }
    public static TimeZone getUserTimeZone() {
        return userTimeZone;
    }

}
