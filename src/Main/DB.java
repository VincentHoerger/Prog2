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
                	System.err.println("Couldn't close DB-Connection");
                    e.printStackTrace();
                }
            }
        });
    }

    void closeDBConnection() {
    	try {
			connection.close();
            if (connection.isClosed())
                System.out.println("Connection to Database closed");
			
		} catch (SQLException e) {
            System.err.println("Couldn't close DB-Connection");
            e.printStackTrace();
		}
    }
    
    Cocktail[] getAllCocktails() {
    	Cocktail[] resCocktails = null;
    	try {
    		// Number of Result sets
    		int rowCount = 0;
    		Statement stmtc = connection.createStatement();
    		ResultSet rsc = stmtc.executeQuery("SELECT COUNT(*) as c FROM tab_cocktails;");
    		while (rsc.next()) {
    			rowCount = rsc.getInt("c");;
            }
    		rsc.close();
    		resCocktails = new Cocktail[rowCount];
    		
    		Statement stmt = connection.createStatement();
    		ResultSet rs = stmt.executeQuery("SELECT * FROM tab_cocktails;");

            int row = 0;
            while (rs.next()) {
            	resCocktails[row] = new Cocktail();
            	resCocktails[row].cocktail_id = rs.getInt("cocktail_id");
            	resCocktails[row].name = rs.getString("name");
            	resCocktails[row].instructions = rs.getString("instructions");
            	row++;
            }
            
            rs.close();
        } catch (SQLException e) {
            System.err.println("Couldn't handle DB-Query");
            e.printStackTrace();
        }
		return resCocktails;
    }
    
    Cocktail getCocktailInfo(int cocktail_id) {
    	Cocktail resCocktail = new Cocktail();
    	try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM tab_cocktails WHERE cocktail_id = ?;");
            ps.setObject(1, cocktail_id);
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
            	resCocktail.cocktail_id = rs.getInt("cocktail_id");
            	resCocktail.name = rs.getString("name");
            	resCocktail.instructions = rs.getString("instructions");
               // System.out.println("cocktail_id = " + resCocktail.cocktail_id);
               // System.out.println("name = " + resCocktail.name);
               // System.out.println("instructions = " + resCocktail.instructions);
            }
            rs.close();
        } catch (SQLException e) {
            System.err.println("Couldn't handle DB-Query");
            e.printStackTrace();
        }
		return resCocktail;
    }
    
    Ingredient[] getCocktailIngredients(int cocktail_id) {
    	Ingredient[] resIngredients = null;
    	try {
    		// Number of Result sets
    		int rowCount = 0;
    		PreparedStatement psc = connection.prepareStatement("SELECT COUNT(*) as c FROM tab_ingredients WHERE cocktail_id = ?;");
    		psc.setObject(1, cocktail_id);
    		ResultSet rsc = psc.executeQuery();
    		while (rsc.next()) {
    			rowCount = rsc.getInt("c");;
            }
    		rsc.close();
    		resIngredients = new Ingredient[rowCount];
    		
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM tab_ingredients WHERE cocktail_id = ?;");
            ps.setObject(1, cocktail_id);
            ResultSet rs = ps.executeQuery();
            
            int row = 0;
            while (rs.next()) {
            	resIngredients[row] = new Ingredient();
            	resIngredients[row].ingredient_id = rs.getInt("ingredient_id");
            	resIngredients[row].cocktail_id = rs.getInt("cocktail_id");
            	resIngredients[row].quantity = rs.getDouble("quantity");
            	resIngredients[row].unit = rs.getString("unit");
            	resIngredients[row].description = rs.getString("description");
//				System.out.println("ingredient_id = " + resIngredients[row].ingredient_id);
//				System.out.println("cocktail_id = " + resIngredients[row].cocktail_id);
//				System.out.println("quantity = " + resIngredients[row].quantity);
//				System.out.println("unit = " + resIngredients[row].unit);
//				System.out.println("description = " + resIngredients[row].description);
            	row++;
            }
            rs.close();
        } catch (SQLException e) {
            System.err.println("Couldn't handle DB-Query");
            e.printStackTrace();
        }
		return resIngredients;
    }
    
    UsageStatistic[] getUsageStatistics() {
    	UsageStatistic[] resStat = null;
    	String query = 	"SELECT description, count(description) as c " +
						"FROM tab_ingredients " +
						"GROUP by description " +
						"ORDER BY c desc";
    	try {
    		// Number of Result sets
    		int rowCount = 0;
    		Statement stmtc = connection.createStatement();
    		ResultSet rsc = stmtc.executeQuery("SELECT COUNT(*) as c FROM (" + query + ");");
    		while (rsc.next()) {
    			rowCount = rsc.getInt("c");;
            }
    		rsc.close();
    		resStat = new UsageStatistic[rowCount];
    		
    		Statement stmt = connection.createStatement();
    		ResultSet rs = stmt.executeQuery(query);

            int row = 0;
            while (rs.next()) {
            	resStat[row] = new UsageStatistic();
            	resStat[row].description = rs.getString("description");
            	resStat[row].count = rs.getInt("c");
            	row++;
            }
            
            rs.close();
        } catch (SQLException e) {
            System.err.println("Couldn't handle DB-Query");
            e.printStackTrace();
        }
		return resStat;
    }
    /*Statement stmt = connection.createStatement();
    
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
}

