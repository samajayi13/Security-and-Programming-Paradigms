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
     * If the user has a set of 6 numbers which is the same as the lottery draw numbers then we wins.
     * @param request the HttpServletRequest being passed to the servlet.
     * @param response the HttpServletResponse being passed to the servlet.
     * @throws ServletException if error occurs during the forwarding of request and response objects.
     * @throws IOException  if error occurs during the forwarding of request and response objects.
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            System.out.println("IN CHECK FOR USER NUMBERS !!!");
            // gets latest lottery draw numbers from the database.
            String query = String.format("SELECT Numbers FROM RandomLotteryDraw ORDER BY ID DESC LIMIT 1;");
            ResultSet rs = databaseConnection.runQuery(query);
            System.out.println("working 1");

            // points result set to the first row and gets lottery draw numbers.
            rs.first();            System.out.println("working 2");

            String numbers = rs.getString("Numbers");
            System.out.println("working 3");

            // gets the user draws (set of all of the 6 numbers the user has generated).
            List<String> draws = (List<String>) request.getSession().getAttribute("draws");
            System.out.println("numbers :" + numbers);
            // checks any of the user numbers is the same as the lottery draw numbers.
            Boolean valid = false;
            for (int j = 0; j < draws.size(); j++) {
                if(draws.get(j).equals(numbers) == true){
                    valid = true;
                }
            }

            // if the user has 6 numbers which matches with the lottery draw numbers a victory message will be displayed
            // otherwise the program will stay on account.jsp
            if(valid == true){
                var session = request.getSession();
                var filename = session .getAttribute("password").toString().substring(0,20);
                File file = new File(filename);
                if(file.delete()){
                    System.out.println("file deleted");
                }



                EncryptionHelperMap encryptionHelperMap = (EncryptionHelperMap) request.getSession().getAttribute("encryptionHelperMap");
                List<EncryptionHelper> encryptionHelperList = new ArrayList<>();
                encryptionHelperMap.updateKeyPair((String) session.getAttribute("username"),encryptionHelperList);

                session.setAttribute("encryptionHelperMap",encryptionHelperMap);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/output.jsp");
                request.setAttribute("data", "Lottery match with one of your numbers !! (" + numbers + ")   You win !!!!!!");
                encryptionHelperMap = (EncryptionHelperMap) request.getSession().getAttribute("encryptionHelperMap");
                System.out.println("Won users helper size is " + encryptionHelperMap.getEncryptionHashMap().get(session.getAttribute("username")).size());

                dispatcher.forward(request, response);
            }else{
                RequestDispatcher dispatcher = request.getRequestDispatcher("/account.jsp");
                dispatcher.forward(request, response);
            }

        }catch(SQLException e){}
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
