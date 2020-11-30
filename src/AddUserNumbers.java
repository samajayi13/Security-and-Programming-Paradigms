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
     * gets all 6 of the user numbers creates a single string from the numbers and encrypts the string before adding it to a text file.
     * file name is made up of the first 20 characters of the users hashed password
     * @param request the HttpServletRequest being passed to the servlet
     * @param response the HttpServletResponse being passed to the servlet
     * @throws ServletException if error occurs during the forwarding of request and response objects
     * @throws IOException if error occurs during the writing of a file
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session  = request.getSession();

        //gets all of the user's numbers entered and concatenates the numbers into a single string using a comma to separate each number
        List<String> parameterNames = new ArrayList<String>(request.getParameterMap().keySet());
        String userNumbers = "";

        for(var i = 0; i <= parameterNames.size()-1; i++) {
            var parameterName = parameterNames.get(i);
            // checks if the counter vairable is on the last element. if it is, adds a comma the end of the string
            userNumbers += i == parameterNames.size()-1 ? request.getParameter(parameterName) : request.getParameter(parameterName) + ",";
        }

        // creates new encryption class which will contain the unique key for the encryption process
        EncryptionHelper encryptionHelper = new EncryptionHelper();
        // encrypts the user numbers and stores it in a file named after the first 20 characters of the users hashed password.
        String filename = session.getAttribute("password").toString().substring(0,20);
        encryptionHelper.bytesFileWriter(filename, encryptionHelper.encryptData(userNumbers));
        // encryptionHelperMap is a hash map which holds the list of encryption classes as the value and the username as the key
        EncryptionHelperMap  encryptionHelperMap = (EncryptionHelperMap) session.getAttribute("encryptionHelperMap");
        List<EncryptionHelper> encryptionHelperList = encryptionHelperMap
                                                        .getEncryptionHashMap()
                                                        .get(session.getAttribute("username"));
        // stores encryption object in a list for when the program needs to get the correct key to decrypt the user's numbers
        encryptionHelperList.add(encryptionHelper);
        encryptionHelperMap.updateKeyPair((String) session.getAttribute("username"),encryptionHelperList);
        //stores hash map back in a session so it can be accessible by other servlets.
        session.setAttribute("encryptionHelperMap", encryptionHelperMap);
        //calls GetUserNumbers so the application can display the user's draws.
        request.getRequestDispatcher("/GetUserNumbers").forward(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
