package hr.foi.woodyoumobile;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DbConnection {

    private static String connectionString = "jdbc:jtds:sqlserver://31.147.204.119:1433;database=18040_DB;user=18040_User;password=FW3E%bdH;encrypt=true;";
    public Connection connection = null;

    public DbConnection()
    {
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openConnection()
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

    public void closeConnection() {
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

    public ResultSet executeQuery(String queryText)
    {
        Statement statement = null;
        try
        {
            if(connection != null && connection.isClosed() == false)
            {
                statement = connection.createStatement();
                return statement.executeQuery(queryText);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }

        return null;
    }
}
