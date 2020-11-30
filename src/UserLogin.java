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
    // calls database helper class which reduces the redundant code.
    private DatabaseConnection databaseConnection = new DatabaseConnection();

    /**
     * checks if the input values entered matches a current username and password combination.
     * if login is successful application removes previous session attributes and adds new session attributes.
     * redirects the user to correct page based on their authorisation role.
     * @param request the HttpServletRequest being passed to the servlet.
     * @param response the HttpServletResponse being passed to the servlet.
     * @throws ServletException if error occurs during the forwarding of request and response objects.
     * @throws IOException  if error occurs during the forwarding of request and response objects.
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");


        try {
            // database settings can be found in DatabaseConnection class.
            // created DatabaseConnection class to help reduce redundant code.
            // i was having to declare the same settings multiple times in different parts of the program.

            var session = request.getSession();

            // checks if username and  parameters are null
            // If they are null application knows this is not a log in attempt so it displays all user account details
            // otherwise it removes previous session attributes
            // in addition, application attempts to log the user in with the username and hashed value of the password provided
            if(username != null && password != null ) {
                removeSessionAttributes(request);
                if(session.getAttribute("loginAttempts") == null) {
                    session.setAttribute("loginAttempts",0);
                }

                // Create sql query
                String query = String.format("SELECT * FROM userAccounts WHERE username = '%s' LIMIT 1",username);
                String userRole = "";
                // query database and get results
                ResultSet rs = databaseConnection.runQuery(query);

                // checks if there is a username the same as the username input value in the database it increases the counter variable.
                // the user's role and user's hashed password is retrieved from the database.
                int rowCount = 0;
                String dbPassword = "";
                String hashedPassword = "";

                while(rs.next()){
                    userRole = rs.getString("UserRole").toLowerCase();
                    dbPassword =  rs.getString("Pwd");
                    rowCount++;
                }

                // creates a hash value of the password provided by the user in the login form
                if(rowCount > 0){
                    String salt = dbPassword.substring(32);
                    String saltedUserPass = salt + password;
                    hashedPassword =  CreateAccount.generateHash(saltedUserPass) + salt;
                }

                // if there is not  a record that matches the username and password combination provided by the user
                // it increases the user login attempts and displays a error message
                //login attempt is stored in a session attribute to keep track of how many failed login attempts are made in a single session
                if(rowCount == 0 || hashedPassword.equals(dbPassword) == false){
                    int loginAttempts = (int) request.getSession().getAttribute("loginAttempts");
                    loginAttempts += 1;
                    request.getSession(true).setAttribute("loginAttempts",loginAttempts);
                    RequestDispatcher dispatcher = request.getRequestDispatcher("/userLogin.jsp");
                    request.setAttribute("data", String.format("login unsuccessful you have %d attempts left",3-loginAttempts));
                    dispatcher.forward(request, response);

                }else{
                    //if login is successful application redirects user to the correct page based on their authorisation role
                    addSessionAttributes(rs,request.getSession());
                    session.setAttribute("userrole",userRole);
                    if(userRole.equals("admin")){
                        // query database and get results
                        rs = databaseConnection.runQuery("SELECT * FROM userAccounts");
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
                        RequestDispatcher dispatcher = request.getRequestDispatcher("/Admin/admin_home.jsp");
                        request.setAttribute("data", content);
                        dispatcher.forward(request, response);
                    }else if(userRole.equals("user")){

                        RequestDispatcher dispatcher = request.getRequestDispatcher("/account.jsp");
                        request.setAttribute("message", "login success");
                        dispatcher.forward(request, response);
                    }
                }
            }else{
                if(session.getAttribute("userrole") == null){
                    RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
                    request.setAttribute("message", "Incorrect Authorisation. You do not have the authorisation to access this page please login");
                    dispatcher.forward(request, response);
                }else if(session.getAttribute("userrole").toString().toLowerCase().equals("admin") == false){
                    RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
                    request.setAttribute("message", "Incorrect Authorisation. You do not have the authorisation to access this page please login");
                    dispatcher.forward(request, response);
                }
            }

            // close connection
            databaseConnection.getConn().close();

        } catch (Exception se) {
            se.printStackTrace();
            // display error.jsp page with given message if exception occurs
            RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
            request.setAttribute("message", se.getMessage());
            dispatcher.forward(request, response);

        } finally {
            try {
                if (databaseConnection.getStmt() != null)
                    databaseConnection.getStmt().close();
            } catch (SQLException se2) {
            }
            try {
                if (databaseConnection.getConn() != null)
                    databaseConnection.getConn() .close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    /**
     * adds user details to the corresponding session attributes.
     * creates session attribute which will store a new hash map.
     * hash map will store a key value pair of the username being the key and a list of encryption objects being the value.
     * @param rs is the result set produced by the query to the database asking for the user's account details.
     * @param session is the session that accompanied the request.
     */
    private void addSessionAttributes(ResultSet rs, HttpSession session) {
        try {
            //makes the result set point to the first row and names sets session attributes using the column names and column values
            rs.first();
            var rsMetadata =  rs.getMetaData();

            for (int i = 1; i < 8 ; i++) {
                var columnName = rsMetadata.getColumnName(i);

                if (columnName.equals("Pwd") == true) {
                    session.setAttribute("password",rs.getString(columnName));
                }else{
                    session.setAttribute(columnName.toLowerCase(),rs.getString(columnName));
                }
            }

            // if the session already contains a hash map it will check if the the hash map has a key that is the same as the user's username
            // if it does not, it will create a new key value pair
            // if the session does not already contain session attribute which holds a hash map
            // it will create a new attribute and assign a hash map as the value
            if(session.getAttribute("encryptionHelperMap") == null){
                EncryptionHelperMap encryptionHelperMap = new EncryptionHelperMap();
                List<EncryptionHelper> encryptionHelpers  = new ArrayList<>();
                encryptionHelperMap.createNewKeyPair((String) session.getAttribute("username"),encryptionHelpers);
                session.setAttribute("encryptionHelperMap",encryptionHelperMap);
            }else if(session.getAttribute("encryptionHelperMap") != null){
                EncryptionHelperMap encryptionHelperMap = (EncryptionHelperMap) session.getAttribute("encryptionHelperMap");

                if(encryptionHelperMap.getEncryptionHashMap().get(session.getAttribute("username")) == null) {
                    List<EncryptionHelper> encryptionHelpers  = new ArrayList<>();
                    encryptionHelperMap.createNewKeyPair((String) session.getAttribute("username"),encryptionHelpers);
                    session.setAttribute("encryptionHelperMap",encryptionHelperMap);
                }
            }

            // set user failed user login attempts back to 0 as they have successfully completed a login
            session.setAttribute("loginAttempts",0);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * removes session attributes from the previous session and sets new session attribute value using the new log in user's details
     * keeps the user's failed login attempts and the hash map of key value pairs of the username and encryption objects
     * @param request the HttpServletRequest being passed to the servlet.
     */
    private void removeSessionAttributes(HttpServletRequest request) {
        var session = request.getSession();
        EncryptionHelperMap encryptionHelperMap = (EncryptionHelperMap) session.getAttribute("encryptionHelperMap");
        var loginAttempts = session.getAttribute("loginAttempts");

        while(session.getAttributeNames().asIterator().hasNext()){
            var attribute = session.getAttributeNames().nextElement();
            session.removeAttribute(attribute);
        }

        session.setAttribute("loginAttempts",loginAttempts);
        session.setAttribute("encryptionHelperMap",encryptionHelperMap);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
