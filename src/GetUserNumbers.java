import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet({"/GetUserNumbers"})
public class GetUserNumbers extends HttpServlet {
    /**
     * gets the user numbers from their file, decrypts all draws and displays them on account.jsp 
     * @param request the HttpServletRequest being passed to the servlet.
     * @param response the HttpServletResponse being passed to the servlet.
     * @throws ServletException if error occurs during the forwarding of request and response objects.
     * @throws IOException if error occurs during the writing of a file.
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       var session = request.getSession();
       // filename is made up of the first 20 characters of the user's hashed password
       String filename = session.getAttribute("password").toString().substring(0,20);

       // gets all the encryption objects that has been used to encrypt the user numbers

        EncryptionHelperMap  encryptionHelperMap = (EncryptionHelperMap) session.getAttribute("encryptionHelperMap");
        List<EncryptionHelper> encryptionHelperList = encryptionHelperMap
                                                      .getEncryptionHashMap()
                                                      .get(session.getAttribute("username"));
        if(encryptionHelperList.size() > 0 ){
            System.out.println("workng e 3 ");

            // reads the byte array representation of the cipher text stored in the file
            var bytes = encryptionHelperList.get(0).bytesFileReader(filename);
            System.out.println("workng e 4 ");

            // creates byte holder array to store bytes as they are being read for the file
            byte[] tempByte = new byte[256];
            // the plaintext version of the user numbers will be stores in this array list
            List<String> userDraws = new ArrayList<String>();
            var i = 0;
            //iterates through each byte in the file
            for (var singleByte:bytes
            ) {
                // if the 256 byte(which has been read from the file) has been stored in the array tempByte.
                // the program looks for the right decryption key and decrypts the cipher text.
                if ((i + 1) % 256 == 0) {
                    tempByte[i % 256] = singleByte;

                    // loops through all encryption objects that were used to encrypt the user numbers.
                    for (var ob : encryptionHelperList
                    ) {
                        if ((i + 1) / 256 == ob.id) {
                            // decrypts cipher text and adds plaintext version of the user numbers into the draws arraylist.
                            userDraws.add(ob.decryptData(tempByte));
                        }
                    }
                } else {
                    // adds each byte read from the file into byte array.
                    tempByte[i % 256] = singleByte;
                }
                // counter keeps track of number of bytes read from the file.
                i++;
            }

            // adds plaintext version of the user draws as session attribute and calls account.jsp.
            System.out.println("workng e 5 ");
            session.setAttribute("draws",userDraws);
            request.setAttribute("draws",userDraws);
        }



        RequestDispatcher dispatcher = request.getRequestDispatcher("/account.jsp");
        request.setAttribute("message", session.getAttribute("firstname")+", you have successfully created an account");
        dispatcher.forward(request, response);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
