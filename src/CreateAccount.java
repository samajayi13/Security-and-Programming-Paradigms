import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.*;
import java.util.Base64.Encoder;
import java.util.Base64.Decoder;

@WebServlet("/CreateAccount")
public class CreateAccount extends HttpServlet {

    private Connection conn;
    private PreparedStatement stmt;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // MySql database connection info
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

        // get parameter data that was submitted in HTML form (use form attributes 'name')
        String firstname = request.getParameter("firstname");
        String lastname = request.getParameter("lastname");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try{
            // create database connection and statement
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            password = processHash(password);

            // Create sql query
            String query = "INSERT INTO userAccounts (Firstname, Lastname, Email, Phone, Username, Pwd)"
                    + " VALUES (?, ?, ?, ?, ?, ?)";

            // set values into SQL query statement
            stmt = conn.prepareStatement(query);
            stmt.setString(1,firstname);
            stmt.setString(2,lastname);
            stmt.setString(3,email);
            stmt.setString(4,phone);
            stmt.setString(5,username);
            stmt.setString(6,password);

            // execute query and close connection
            stmt.execute();
            conn.close();

            //this bellow gets the paramets from the request and assigns each to a session working
            HttpSession session  = request.getSession();
            List<String> parameterNames = new ArrayList<String>(request.getParameterMap().keySet());

            for (var name : parameterNames
            ) {
                session.setAttribute(name,request.getParameter(name));
            }


            // display account.jsp page with given message if successful
            RequestDispatcher dispatcher = request.getRequestDispatcher("/account.jsp");
            request.setAttribute("message", firstname+", you have successfully created an account");
            dispatcher.forward(request, response);

        } catch(Exception se){
            se.printStackTrace();
            // display error.jsp page with given message if unsuccessful
            RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
            request.setAttribute("message", firstname+", this username/password combination already exists. Please try again");
            dispatcher.forward(request, response);
        }
        finally{
            try{
                if(stmt!=null)
                    stmt.close();
            }
            catch(SQLException se2){}
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    private static String generateSalt(int lenght){
        String abcCapitals = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String abcLowerCase = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "01234567890123456789";
        String characters = "!@#$%^&*!@#$%%^^&*";

        String total = abcCapitals + abcLowerCase + numbers + characters;

        String response = "";

        char letters[] = new char[lenght];
        for (int i=0; i<lenght-1; i++){
            Random r = new Random();
            char letter = total.charAt(r.nextInt(total.length()));
            letters[i] = letter;
        }

        response = Arrays.toString(letters).replaceAll("\\s+","");
        response = response.replaceAll(",","");

        return response;
    }

    private static String getHash(String passwordToHash, String salt){
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt.getBytes(StandardCharsets.UTF_8));
            byte[] bytes = md.digest(passwordToHash.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++){
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e){
            System.out.println(e);
        }
        return generatedPassword;
    }

    public static String getSecureHash(String password, String salt){
        String hash = getHash(password, salt);
        hash = getHash(password, hash);
        return hash;
    }


    public static String processHash(String userPassword) {
        String salt = generateSalt(150);
        String salt2 = generateSalt(150);
        String salt3 = generateSalt(150);

        String hash = getSecureHash(userPassword, salt + salt2 + salt3);
        return hash;
    }
}
