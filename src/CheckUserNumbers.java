import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.List;

@WebServlet("/CheckUserNumbers")
public class CheckUserNumbers extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
            String USER = "user";
            String PASS = "password";
            String DB_URL = "jdbc:mysql://localhost:33333/lottery";
            Class.forName(JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            String query = String.format("SELECT Numbers FROM RandomLotteryDraw ORDER BY ID DESC LIMIT 1;");
            ResultSet rs = stmt.executeQuery(query);
            String numbers = rs.getString("Numbers");
            var numsArray = numbers.split(",");
            List<String> draws = (List<String>) request.getAttribute("draws");
            Boolean valid = true;
            for (int j = 0; j < draws.size(); j++) {
                if(draws.get(j).equals(numsArray[j]) == false){
                    valid = false;
                }
            }

            if(valid == true){
                RequestDispatcher dispatcher = request.getRequestDispatcher("/output.jsp");
                request.setAttribute("data", "Lottery match with one of your numbers !! (" + numbers + ")   You win !!!!!!");
                dispatcher.forward(request, response);
            }
            conn.close();
        }catch(SQLException | ClassNotFoundException e){

        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
