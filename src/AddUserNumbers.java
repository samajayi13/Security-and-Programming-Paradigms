import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebServlet({"/AddUserNumbers"})
public class AddUserNumbers extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session  = request.getSession();
        List<String> parameterNames = new ArrayList<String>(request.getParameterMap().keySet());
        String userNumbers = "";
        for(var i = 0; i <= parameterNames.size()-1; i++){
            var name = parameterNames.get(i);
            userNumbers += i == parameterNames.size()-1 ? request.getParameter(name) : request.getParameter(name) + ",";
        }

        EncryptedData encryptedData = new EncryptedData();
        String filename = session.getAttribute("password").toString().substring(0,20);
        encryptedData.bytesFileWriter(filename,encryptedData.encryptData(userNumbers));
        List<EncryptedData> encryptedDataList = (List<EncryptedData>) session.getAttribute("encryptedDatas");
        encryptedDataList.add(encryptedData);
        session.setAttribute("encryptedDatas",encryptedDataList);
        request.getRequestDispatcher("/GetUserNumbers").forward(request,response);


//        //delete the following after :
//        var testString1 = encryptedData.decryptData(encryptedData.bytesFileReader(filename));
//        RequestDispatcher dispatcher = request.getRequestDispatcher("/account.jsp");
//        request.setAttribute("message", testString1+" you have successfully created an account");
//        dispatcher.forward(request, response);

        //delete block above

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
