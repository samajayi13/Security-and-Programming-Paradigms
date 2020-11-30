import java.sql.*;

public class DatabaseConnection {
    // MySql database connection info
    private String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private String USER = "user";
    private String PASS = "password";

    // URLs to connect to database depending on your development approach
    // (NOTE: please change to option 1 when submitting)

    // 1. use this when running everything in Docker using docker-compose
//        String DB_URL = "jdbc:mysql://db:3306/lottery";

    // 2. use this when running tomcat server locally on your machine and mysql database server in Docker
    private String DB_URL = "jdbc:mysql://localhost:33333/lottery";

    // 3. use this when running tomcat and mysql database servers on your machine
    //String DB_URL = "jdbc:mysql://localhost:3306/lottery";

    private Connection conn;

    private Statement stmt ;

    public DatabaseConnection(){
        assignValues();
    }

    // assigns values to private properties and sets up a new connection with the database
    public void assignValues(){
        try {
            Class.forName(JDBC_DRIVER);
            // create database connection and statement.
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
             this.stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //executes query passed into method
    public ResultSet runQuery(String query){
        try {
            assignValues();
            return stmt.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Statement getStmt() {
        return stmt;
    }

    public Connection getConn() {
        return conn;
    }
}
