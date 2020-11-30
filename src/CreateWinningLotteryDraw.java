import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.crypto.Data;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/CreateWinningLotteryDraw")
public class CreateWinningLotteryDraw extends HttpServlet {
    private DatabaseConnection databaseConnection;
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        databaseConnection = new DatabaseConnection();
        String winningLotteryNumbers  = "";

        List<String> parameterNames = new ArrayList<>(request.getParameterMap().keySet());
        var i = 0;
        for (var name : parameterNames
        ) {
                winningLotteryNumbers += i == 5 ? request.getParameter(name) : request.getParameter(name) + ",";
                i++;
        }

        // stores the 6 random numbers as the winning random lottery draw numbers
        String query = "INSERT INTO RandomLotteryDraw(Numbers,TimeCreated) VALUES('"+winningLotteryNumbers +"',NOW())";
        try {
            databaseConnection.getStmt().execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher("/output.jsp");
        request.setAttribute("data", "Winning lottery draws successfully created");
        dispatcher.forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
