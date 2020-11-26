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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       var session = request.getSession();
       String filename = session.getAttribute("password").toString().substring(0,20);

        List<EncryptedData> encryptedDataList = (List<EncryptedData>) session.getAttribute("encryptedDatas");

        var bytes = encryptedDataList.get(0).bytesFileReader(filename);

        byte[] tempByte = new byte[256];
        List<String> userNumbers = new ArrayList<String>();
        var i = 0;
        for (var singleByte:bytes
        ) {
            if((i+1)% 256 == 0){
                tempByte[i % 256] = singleByte;

                for (var ob: encryptedDataList
                ) {
                    if((i+1)/256 == ob.id){
                        userNumbers.add(ob.decryptData(tempByte));
                    }
                }
            }else{
                tempByte[i % 256] = singleByte;
            }
            i++;
        }
        request.setAttribute("draws",userNumbers);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/account.jsp");
        request.setAttribute("message", session.getAttribute("firstname")+", you have successfully created an account");
        dispatcher.forward(request, response);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
