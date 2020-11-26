import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


@WebServlet("/UserLogin")
public class UserLogin extends HttpServlet {

    private Connection conn;
    private Statement stmt;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
        String USER = "user";
        String PASS = "password";

        // URLs to connect to database depending on your development approach
        // (NOTE: please change to option 1 when submitting)

        // 1. use this when running everything in Docker using docker-compose
//        String DB_URL = "jdbc:mysql://db:3306/lottery";

        // 2. use this when running tomcat server locally on your machine and mysql database server in Docker
        String DB_URL = "jdbc:mysql://localhost:33333/lottery";

        // 3. use this when running tomcat and mysql database servers on your machine
        //String DB_URL = "jdbc:mysql://localhost:3306/lottery";

        String username = request.getParameter("username");
        String password = request.getParameter("password");


        try {
            // create database connection and statement
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);

            if(username != null && password != null ){
                // Create sql query
                String query = String.format("SELECT * FROM userAccounts WHERE username = '%s' AND Pwd = '%s' LIMIT 1",username,password);
                String userRole = "";
                // query database and get results
                ResultSet rs = stmt.executeQuery(query);

                int rowCount = 0;
                while(rs.next()){
                    userRole = rs.getString("UserRole").toLowerCase();
                    rowCount++;
                }
                if(rowCount == 0){
                    int loginAttempts = (int) request.getSession().getAttribute("loginAttempts");
                    loginAttempts += 1;
                    request.getSession().setAttribute("loginAttempts",loginAttempts);

                    RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
                    request.setAttribute("message", String.format("login unsuccessful you have %d attempts left",3-loginAttempts));
                    dispatcher.forward(request, response);

                }else{
                    removeSessionAttributes(request);
                    addSessionAtrributes(rs,request.getSession());
                    List<EncryptedData> encryptedDataList = new ArrayList<>();
                    var session = request.getSession();
                    session.setAttribute("encryptedDatas",encryptedDataList);
                    System.out.println(userRole);
                    if(userRole.equals("admin")){
                        RequestDispatcher dispatcher = request.getRequestDispatcher("/Admin/admin_home.jsp");
                        dispatcher.forward(request, response);
                    }else if(userRole.equals("user")){
                        RequestDispatcher dispatcher = request.getRequestDispatcher("/account.jsp");
                        request.setAttribute("message", "login success");
                        dispatcher.forward(request, response);
                    }
                }

            }else{
                // query database and get results
                ResultSet rs = stmt.executeQuery("SELECT * FROM userAccounts");
                // create HTML table text
                String content = "<table border='1' cellspacing='2' cellpadding='2' width='100%' align='left'>" +
                        "<tr><th>First name</th><th>Last name</th><th>Email</th><th>Phone number</th><th>Username</th></tr>";

                while (rs.next()) {
                    content += "<tr><td>"+ rs.getString("Firstname") + "</td>" +
                            "<td>" + rs.getString("Lastname") + "</td>" +
                            "<td>" + rs.getString("Email") + "</td>" +
                            "<td>" + rs.getString("Phone") + "</td>" +
                            "<td>" + rs.getString("Username") + "</td>";
                }
                // finish HTML table text
                content += "</table>";

                // display output.jsp page with given content above if successful
                RequestDispatcher dispatcher = request.getRequestDispatcher("/output.jsp");
                request.setAttribute("data", content);
                dispatcher.forward(request, response);
            }

            // add HTML table data using data from database


            // close connection
            conn.close();

        } catch (Exception se) {
            se.printStackTrace();
            // display error.jsp page with given message if successful
            RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
            request.setAttribute("message", se.getMessage());
            dispatcher.forward(request, response);

        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    private void addSessionAtrributes(ResultSet rs,HttpSession session) {
        try {
            rs.first();
            var rsMetadata =  rs.getMetaData();
            for (int i = 1; i < 8 ; i++) {
                var columnName = rsMetadata.getColumnName(i);
                if (columnName.equals("Pwd") == true){
                    session.setAttribute("password",rs.getString(columnName));
                }else{
                    session.setAttribute(columnName.toLowerCase(),rs.getString(columnName));
                }


            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void removeSessionAttributes(HttpServletRequest request) {
        var session = request.getSession();
        while(session.getAttributeNames().asIterator().hasNext()){
            var attribute = session.getAttributeNames().nextElement();
            session.removeAttribute(attribute);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
