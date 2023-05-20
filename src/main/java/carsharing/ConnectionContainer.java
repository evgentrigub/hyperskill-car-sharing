package carsharing;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionContainer {
    private static Connection connection = null;

    private static String dbUri = null;

    static final String JDBC_DRIVER = "org.h2.Driver";

    public static void setDbUri(String dbUri) {
        ConnectionContainer.dbUri = dbUri;
    }

    public static Connection getConnection(){
        if (connection == null) {
            try {
                Class.forName(JDBC_DRIVER);
                connection = DriverManager.getConnection(dbUri);
                connection.setAutoCommit(true);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return  connection;
    }

    public static void closeConnection() {
        if(ConnectionContainer.connection != null){
            try {
                ConnectionContainer.connection.close();
            }
            catch(SQLException se){
                se.printStackTrace();
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
            ConnectionContainer.connection = null;
        }
    }
}
