import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/CheckForWinningNumbers")
public class CheckForWinningNumbers extends HttpServlet {
     private DatabaseConnection databaseConnection = new DatabaseConnection();

    /**
     * get's the latest lottery draw numbers and checks user's numbers against them.
     * If the user has a set of 6 numbers which is the same as the lottery draw numbers then he wins.
     * all of the user's draws are deleted if the user wins the lottery.
     * @param request the HttpServletRequest being passed to the servlet.
     * @param response the HttpServletResponse being passed to the servlet.
     * @throws ServletException if error occurs during the forwarding of request and response objects.
     * @throws IOException  if error occurs during the forwarding of request and response objects.
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            // gets latest lottery draw numbers from the database.
            String query = String.format("SELECT Numbers FROM RandomLotteryDraw ORDER BY ID DESC LIMIT 1;");
            ResultSet rs = databaseConnection.runQuery(query);

            // points result set to the first row and gets lottery draw numbers.
            rs.first();
            String numbers = rs.getString("Numbers");

            // gets the user lottery draws and checks if any of the user numbers is the same as the lottery draw numbers.
            List<String> draws = (List<String>) request.getSession().getAttribute("draws");
            Boolean matchedNumbers = false;

            for (int j = 0; j < draws.size(); j++) {
                if(draws.get(j).equals(numbers) == true){
                    matchedNumbers = true;
                }
            }

            // if the user has 6 numbers which matches with the lottery draw numbers a victory message will be displayed
            // otherwise the program will stay on account.jsp
            if(matchedNumbers == true){
                var session = request.getSession();

                // gets the application current directory and points to the user text file in "EncryptedFiles" folder
                // deletes user previous draws as they have won the lottery
                var filename = System.getProperty("user.dir")+ "\\EncryptedFiles"+session .getAttribute("password").toString().substring(0,20);
                File file = new File(filename);
                file.delete();

                // removes all previous encryption objects and keys associated with the user by creating a new empty list of encryption objects.
                // updates the session's hash maps key pair value for the user.
                EncryptionHelperMap encryptionHelperMap = (EncryptionHelperMap) request.getSession().getAttribute("encryptionHelperMap");
                List<EncryptionHelper> encryptionHelperList = new ArrayList<>();
                encryptionHelperMap.updateKeyPair((String) session.getAttribute("username"),encryptionHelperList);
                session.setAttribute("encryptionHelperMap",encryptionHelperMap);

                // redirects user to output page informing them they have won the lottery.
                RequestDispatcher dispatcher = request.getRequestDispatcher("/output.jsp");
                request.setAttribute("data", "Lottery match with one of your numbers !! (" + numbers + ")   You win !!!!!!");
                dispatcher.forward(request, response);
            }else{
                // if they have not won the lottery it refreshes the the page and informs the user.
                RequestDispatcher dispatcher = request.getRequestDispatcher("/account.jsp");
                request.setAttribute("message", "Unfortunately none of your numbers match with the lottery draw numbers");
                dispatcher.forward(request, response);
            }
        }catch(SQLException e){}
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
