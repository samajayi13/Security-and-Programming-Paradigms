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
    // calls database helper class which reduces the redundant code.
    private DatabaseConnection databaseConnection = new DatabaseConnection();

    /**
     * gets value entered in input fields and creates a new user account
     * redirects user to their homepage based on which role they hold
     * @param request the HttpServletRequest being passed to the servlet.
     * @param response the HttpServletResponse being passed to the servlet.
     * @throws ServletException if error occurs during the forwarding of request and response objects.
     * @throws IOException  if error occurs during the forwarding of request and response objects.
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // database settings can be found in DatabaseConnection class.
        // created DatabaseConnection class to help reduce redundant code.
        // i was having to declare the same settings multiple times in different parts of the program.

        // get parameter data that was submitted in HTML form (use form attributes 'name')
        String firstname = request.getParameter("firstname");
        String lastname = request.getParameter("lastname");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String userRole = request.getParameter("userrole");


        try{
            // creates a hashed value from the user's password
            String hashedPassword = createHashedPassword(password);

            // Creates SQL query to insert user data into userAccounts table
            String query = "INSERT INTO userAccounts (Firstname, Lastname, Email, Phone, Username, Pwd,UserRole)"
                    + " VALUES (?, ?, ?, ?, ?, ?,?)";

            // set values into SQL query statement
            var stmt = databaseConnection.getConn().prepareStatement(query);
            stmt.setString(1,firstname);
            stmt.setString(2,lastname);
            stmt.setString(3,email);
            stmt.setString(4,phone);
            stmt.setString(5,username);
            stmt.setString(6,hashedPassword);
            stmt.setString(7,userRole);
            System.out.println(userRole);
            // execute query and close connection
            stmt.execute();
            databaseConnection.getConn().close();

            //gets parameter names from request and assigns each parameter value as a session attribute
            //sets password session attribute as the hashed version of the value entered by the user
            HttpSession session  = request.getSession();
            List<String> parameterNames = new ArrayList<>(request.getParameterMap().keySet());
            for (var name : parameterNames
            ) {
                if(name.equalsIgnoreCase("password"))
                    session.setAttribute(name,hashedPassword);
                else
                    session.setAttribute(name,request.getParameter(name));
            }

             //Creates a list of encryption objects and stores it as a session attribute
             //This will allow the program to be able to encrypt and decrypt multiple user numbers
            if(session.getAttribute("encryptionHelperMap") == null){
                EncryptionHelperMap encryptionHelperMap = new EncryptionHelperMap();
                List<EncryptionHelper> encryptionHelpers  = new ArrayList<>();
                encryptionHelperMap.createNewKeyPair((String) session.getAttribute("username"),encryptionHelpers);
                session.setAttribute("encryptionHelperMap",encryptionHelperMap);
            }

            //Redirects user to correct page based on user role
            if(userRole.equals("Admin")) {
                RequestDispatcher dispatcher = request.getRequestDispatcher("/Admin/admin_home.jsp");
                dispatcher.forward(request, response);
            }else if(userRole.equals("User")) {
                RequestDispatcher dispatcher = request.getRequestDispatcher("/account.jsp");
                request.setAttribute("message", firstname+", you have successfully created an account");
                dispatcher.forward(request, response);
            }

        } catch(Exception se){
            se.printStackTrace();
            // display error.jsp page with given message if unsuccessful
            RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
            request.setAttribute("message", firstname+", this username/password combination already exists. Please try again");
            dispatcher.forward(request, response);
        }
        finally{
            try{
                if(databaseConnection.getConn()!=null)
                    databaseConnection.getConn().close();
            }
            catch(SQLException se2){}
            try{
                if(databaseConnection.getConn()!=null)
                    databaseConnection.getConn().close();
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


    public static String createHashedPassword(String userPassword) {
        String salt = generateSalt(150);
        String salt2 = generateSalt(150);
        String salt3 = generateSalt(150);

        String hash = getSecureHash(userPassword, salt + salt2 + salt3);
        return hash;
    }
}
