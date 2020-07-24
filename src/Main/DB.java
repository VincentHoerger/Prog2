package Main;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DB {
    
    private static final DB DB = new DB();
    private static Connection connection;
    private static final String DB_PATH = System.getProperty("user.dir") + "/" + "db.sqlite";

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("Fehler beim Laden des JDBC-Treibers");
            e.printStackTrace();
        }
    }
    
    private DB(){
    }
    
    public static DB getInstance(){
        return DB;
    }
    
    void initDBConnection() {
        try {
            if (connection != null)
                return;
            System.out.println("Creating Connection to Database...");
            connection = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
            if (!connection.isClosed())
                System.out.println("...Connection established");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                try {
                    if (!connection.isClosed() && connection != null) {
                        connection.close();
                        if (connection.isClosed())
                            System.out.println("Connection to Database closed");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    void handleDB(int cocktail_id) {
        try {
            Statement stmt = connection.createStatement();
            /*
            stmt.execute("INSERT INTO books (author, title, publication, pages, price) VALUES ('Paulchen Paule', 'Paul der Penner', '2001-05-06', '1234', '5.67')");
            
            PreparedStatement ps = connection
                    .prepareStatement("INSERT INTO books VALUES (?, ?, ?, ?, ?);");

            ps.setString(1, "Willi Winzig");
            ps.setString(2, "Willi's Wille");
            ps.setDate(3, Date.valueOf("2011-05-16"));
            ps.setInt(4, 432);
            ps.setDouble(5, 32.95);
            ps.addBatch();

            ps.setString(1, "Anton Antonius");
            ps.setString(2, "Anton's Alarm");
            ps.setDate(3, Date.valueOf("2009-10-01"));
            ps.setInt(4, 123);
            ps.setDouble(5, 98.76);
            ps.addBatch();

            connection.setAutoCommit(false);
            ps.executeBatch();
            connection.setAutoCommit(true);
			*/
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM tab_cocktails WHERE cocktail_id = ?;");
            ps.setObject(1, cocktail_id);
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                System.out.println("cocktail_id = " + rs.getString("cocktail_id"));
                System.out.println("name = " + rs.getString("name"));
                System.out.println("instructions = " + rs.getDouble("instructions"));
            }
            rs.close();
            connection.close();
        } catch (SQLException e) {
            System.err.println("Couldn't handle DB-Query");
            e.printStackTrace();
        }
    }
}