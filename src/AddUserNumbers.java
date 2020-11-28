import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet({"/AddUserNumbers"})
public class AddUserNumbers extends HttpServlet {
    /**
     * when post request is sent, get all 6 of the user numbers
     * adds number to a file. File name is made up of the first 20 characters of the users hashed password
     * @param request the HttpServletRequest being passed to the servlet
     * @param response the HttpServletResponse being passed to the servlet
     * @throws ServletException if error occurs during the forwarding of request and response objects
     * @throws IOException if error occurs during the writing of a file
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session  = request.getSession();

        //gets all of the user's numbers entered and concatenates the numbers using a comma.
        List<String> parameterNames = new ArrayList<String>(request.getParameterMap().keySet());
        String userNumbers = "";
        for(var i = 0; i <= parameterNames.size()-1; i++){
            var name = parameterNames.get(i);
            userNumbers += i == parameterNames.size()-1 ? request.getParameter(name) : request.getParameter(name) + ",";
        }

        // encrypts the user numbers and stores it in a file named after the first 20 characters of the users hashed password.
        EncryptionHelperMap  encryptionHelperMap = (EncryptionHelperMap) session.getAttribute("encryptionHelperMap");
        EncryptionHelper encryptionHelper = new EncryptionHelper();
        String filename = session.getAttribute("password").toString().substring(0,20);
        encryptionHelper.bytesFileWriter(filename, encryptionHelper.encryptData(userNumbers));
        System.out.println("Encpytion helper class id is " + encryptionHelper.id);
        System.out.println("workng e 1 ");
        // stores encryption object in a list for when the program needs to get the correct key to decrypt set of 6 numbers

        List<EncryptionHelper> encryptionHelperList = encryptionHelperMap
                                                        .getEncryptionHashMap()
                                                        .get(session.getAttribute("username"));
        encryptionHelperList.add(encryptionHelper);
        encryptionHelperMap.updateKeyPair((String) session.getAttribute("username"),encryptionHelperList);
        //stores list in a session so it can be accessible by other servlets.
        session.setAttribute("encryptionHelperMap", encryptionHelperMap);
        //calls GetUserNumbers so the page can display the user's draws.
        request.getRequestDispatcher("/GetUserNumbers").forward(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
