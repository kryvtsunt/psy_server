package dao;


import com.mysql.jdbc.Driver;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Connect to Database
 * @author hany.said
 */
public class ConnectionFactory {

    //public static final String URL = "jdbc:mysql://localhost:3306/psych";
	
	//this is the original
	public static final String URL = "jdbc:mysql://aarc9cin3z08cw.clcjwswdswyh.us-east-2.rds.amazonaws.com:3306/aarc9cin3z08cw";
	
	//this is the backup
	//public static final String URL = "jdbc:mysql://aa1arb2i67pp0c7.clcjwswdswyh.us-east-2.rds.amazonaws.com:3306/aa1arb2i67pp0c7";
	
	
    public static final String USER = "root";
    //public static final String PASS = "root";
    public static final String PASS = "rootrootroot";

   /* public static final String URL = "jdbc:mysql://localhost:3306/psych";
    public static final String USER = "root";
    public static final String PASS = "root";*/

    /**
     * Get a connection to database
     * @return Connection object
     */
    public static Connection getConnection()
    {
      try {
          DriverManager.registerDriver(new Driver());
          return DriverManager.getConnection(URL, USER, PASS);
      } catch (SQLException ex) {
          throw new RuntimeException("Error connecting to the database", ex);
      }
    }
    /**
     * Test Connection
     */
    public static void main(String[] args) {
        Connection connection = ConnectionFactory.getConnection();
    }
}