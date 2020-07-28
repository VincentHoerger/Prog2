package Main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;		//Importieren der wichtigsten Databases um SQLL in Java laufen zu lassen
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DB {
    
    private static final DB DB = new DB();			//Treiber damit SQL in Java funktionieren kann (Aus Internet kopiert)
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
    } 				//Ende vom Treiber
    
    Cocktail[] getAllCocktails() { 			//Funktion damit alle angelegten Cocktails aus DB ausgewählt und angezeigt werden
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
    		resCocktails = new Cocktail[rowCount]; 		//Cocktail ID wird automatisch um 1 erhöht wenn Rezept neu angelegt wird
    		
    		Statement stmt = connection.createStatement();
    		ResultSet rs = stmt.executeQuery("SELECT * FROM tab_cocktails;");

            int row = 0;
            while (rs.next()) {									//Bei anlegen de neuen Rezept werden die 3 Parameter ID, Name und Instructions ebenfalls angelegt
            	resCocktails[row] = new Cocktail();
            	resCocktails[row].cocktail_id = rs.getInt("cocktail_id");
            	resCocktails[row].name = rs.getString("name");
            	resCocktails[row].instructions = rs.getString("instructions");
            	row++;
            }
            
            rs.close();
        } catch (SQLException e) {			//Bei Fehlermeldung wird Exception ausgegeben
            System.err.println("Couldn't handle DB-Query");
            e.printStackTrace();
        }
		return resCocktails;
    }
    
    Cocktail getCocktailInfo(int cocktail_id) {
    	Cocktail resCocktail = new Cocktail();
    	try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM tab_cocktails WHERE cocktail_id = ?;"); 	//Suchfunktion um Cocktails anhand ihrer unikaten ID zu finden
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
    
    UsageStatistic[] getUsageStatistics() {			//Anzahl der jeweils benutzten Zutaten in der gesamten Datenbank --> Funktion für das Diagramm
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
    
    boolean createCocktail(Cocktail cocktail) {			//Anlegen neuer Cocktails in der Datenbank
		try {
			String statement = 	"INSERT INTO tab_cocktails (name, instructions)" +
								"VALUES (?,?)";
			PreparedStatement ps = connection.prepareStatement(statement);
			ps.setObject(1, cocktail.name);
			ps.setObject(2, cocktail.instructions);

			ps.execute();
    		Statement stmtc = connection.createStatement();
    		ResultSet rsc = stmtc.executeQuery("SELECT last_insert_rowid() as id");
    		while (rsc.next()) {
    			cocktail.cocktail_id = rsc.getInt("id");
            }
		} catch (SQLException e) {
			System.err.println("Couldn't handle DB-Query");
			e.printStackTrace();
			return false;
		}
		return true;
    }
    
    boolean createIngredient(Ingredient ingredient) { 		//Anlegen neuer Ingredients in der Datenbank
		try {
			String statement = 	"INSERT INTO tab_ingredients (cocktail_id, quantity, unit, description)" +
								"VALUES (?,?,?,?)";
			PreparedStatement ps = connection.prepareStatement(statement);
			ps.setObject(1, ingredient.cocktail_id);
			ps.setObject(2, ingredient.quantity);
			ps.setObject(3, ingredient.unit);
			ps.setObject(4, ingredient.description);

			ps.execute();
    		Statement stmtc = connection.createStatement();
    		ResultSet rsc = stmtc.executeQuery("SELECT last_insert_rowid() as id");
    		while (rsc.next()) {
    			ingredient.ingredient_id = rsc.getInt("id");
            }
		} catch (SQLException e) {
			System.err.println("Couldn't handle DB-Query");
			e.printStackTrace();
			return false;
		}
		return true;
    }
    
   
    boolean updateCocktail(Cocktail cocktail) {		//Ändern der Cocktails in der Datenbank
		try {
			String statement = 	"UPDATE tab_cocktails " +
								"SET name = ?, instructions = ?" +
								"WHERE cocktail_id = ?";
			PreparedStatement ps = connection.prepareStatement(statement);
			ps.setObject(1, cocktail.name);
			ps.setObject(2, cocktail.instructions);
			ps.setObject(3, cocktail.cocktail_id);

			ps.execute();
		} catch (SQLException e) {
			System.err.println("Couldn't handle DB-Query");
			e.printStackTrace();
			return false;
		}
		return true;
    }
    
    boolean updateIngredient(Ingredient ingredient) {		//Ändern der Ingredients in der Datenbank
		try {
			String statement = 	"UPDATE tab_ingredients " +
								"SET quantity = ?, unit = ?, description = ?" +
								"WHERE cocktail_id = ?";
			PreparedStatement ps = connection.prepareStatement(statement);
			ps.setObject(1, ingredient.quantity);
			ps.setObject(2, ingredient.unit);
			ps.setObject(3, ingredient.description);
			ps.setObject(4, ingredient.cocktail_id);
			
			ps.execute();
		} catch (SQLException e) {
			System.err.println("Couldn't handle DB-Query");
			e.printStackTrace();
			return false;
		}
		return true;
    }
    
    boolean deleteCocktail(int cocktail_id) { 	//Funktion um Cocktails zu löschen
		try {
			// Hier reicht es den Coktail einfach zu löschen, da in SQL eine foreign key reference ON DELETE CASCADE angelegt ist
			String statement = 	"DELETE FROM tab_cocktails" +
								"WHERE cocktail_id = ?";
			PreparedStatement ps = connection.prepareStatement(statement);
			ps.setObject(1, cocktail_id);
			
			ps.execute();
		} catch (SQLException e) {		//Bei Fehlermeldung (z.B. wenn Cocktail fehlt) wird die Exception ausgeführt
			System.err.println("Couldn't handle DB-Query");
			e.printStackTrace();
			return false;
		}
		return true;
    }
    
    boolean deleteIngredient(int ingredient_id) { //Funktion um Ingredients zu löschen --> funktioniert genau gleich wie den Cocktail löschen 
		try {
			String statement = 	"DELETE FROM tab_ingredients" +
					"WHERE ingredient_id = ?";
			PreparedStatement ps = connection.prepareStatement(statement);
			ps.setObject(1, ingredient_id);
			
			ps.execute();
		} catch (SQLException e) {
			System.err.println("Couldn't handle DB-Query");
			e.printStackTrace();
			return false;
		}
		return true;
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

