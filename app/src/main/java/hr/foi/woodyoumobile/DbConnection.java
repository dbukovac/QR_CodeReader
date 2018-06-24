package hr.foi.woodyoumobile;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Singleton klasa koja služi za pristup i radu s bazom podataka.
 */
public class DbConnection {

    private static DbConnection instance;
    private static String connectionString = "jdbc:jtds:sqlserver://31.147.204.119:1433;database=18040_DB;user=18040_User;password=FW3E%bdH;encrypt=true;";
    private static Connection connection = null;

    /**
     * Metoda koja kreira novi singleton objekt tipa DbConnection,
     * ukoliko on nije već stvoren
     * @return      instanca klase DbConnection
     */
    public static synchronized DbConnection getInstance() {
        if(instance == null) {
            instance = new DbConnection();
        }
        return instance;
    }

    /**
     * Konstrukor klase DbConnection koji učitava
     * paket sa driverom za rad sa SQL Server
     * bazom podataka
     */
    private DbConnection()
    {
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Metoda kojom se otvara konekcija prema bazi podataka
     */
    public static void openConnection()
    {
        try {
            connection = DriverManager.getConnection(connectionString);
        }
        catch (Exception e) {
            e.printStackTrace();
            if(connection != null)
            {
               try { connection.close(); }catch (Exception e1) {}
            }
        }
    }

    /**
     * Metoda kojom se zatvara konekcija prema bazi podataka
     */
    public static void closeConnection() {
        if(connection != null)
        {
            try {
                connection.close();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Metoda kojom se šalje upit prema bazi podatka, ukoliko konekcija postoji
     * i nije zatvorena, te vraća rezultate upita
     *
     * @param queryText     Tekst SQL upita za bazu podataka
     * @return              Ako je upit uspješno prošao metoda vraća objekt tipa
     *                      ResultSet koji sadrži rezultate upita, a ako je došlo
     *                      do greške vraća null
     */
    public static ResultSet executeQuery(String queryText) {
        Statement statement = null;
        try {
            if(connection != null && connection.isClosed() == false) {
                statement = connection.createStatement();
                return statement.executeQuery(queryText);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }

    /**
     * Metoda kojom se izvršavaju INSERT/UPDATE naredbe u bazi podataka.
     * Metoda prvo provjerava postoji li konekcija i je li ona zatvorena.
     * Ako postoji i nije zatvorena šalje se naredba prema bazi.
     *
     * @param sqlText       Tekst SQL naredbe za bazu podataka
     */
    public static void executeUpdate(String sqlText) {
        Statement statement = null;
        try{
            if(connection != null && connection.isClosed() == false) {
                statement = connection.createStatement();
                statement.executeUpdate(sqlText);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
