import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

@WebFilter(filterName = "Servlet Filter")
public class ServletFilter implements Filter {
    public void destroy() {
    }

    /**
     *
     * @param req
     * @param resp
     * @param chain
     * @throws ServletException
     * @throws IOException
     */
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
          boolean valid = true;
          Map parameters = req.getParameterMap();
          
          if(parameters != null){
              Object[] keys = parameters.keySet().toArray();
              var i = 0;
              for (var key :keys
                   ) {
                  String[] values = (String[])parameters.get(key.toString());
                  for (var value:values
                       ) {
                      if(checkCharacters(value) == true){
                         valid = false;
                         break;
                      }
                  }
                  if(valid == false ){break;}
              }
          }
          if(valid == false){
              try{
                  RequestDispatcher dispatcher = req.getRequestDispatcher("/error.jsp");
                  req.setAttribute("message", "Unsuccesful action. Form contains sql commands such as '<,  >,  !,  {,  } , insert, into, where, script, delete, input.'");
                  dispatcher.forward(req, resp);
              }
              catch (Exception ex) {
                  ex.printStackTrace();
              }
          }else{
              chain.doFilter(req, resp);
          }
    }

    public static boolean checkCharacters(String value) {
        boolean invalid = false;
        String[] badChars = { "<", ">", "script", "alert", "truncate", "delete",
                "insert", "drop", "null", "xp_", "<>", "!", "{", "}", "`",
                "input" };

        for(int i = 0; i < badChars.length; i++){
            if(value.indexOf(badChars[i]) >=0){
                invalid = true;
                break;
            }
        }
        return invalid;
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
