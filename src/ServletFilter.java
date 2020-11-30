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
     * filters any data being submitted through the forms to ensure it does contain any SQL injection
     * @param req the HttpServletRequest being passed to the servlet.
     * @param resp the HttpServletResponse being passed to the servlet.
     * @param chain order of filters
     * @throws ServletException if error occurs during the calling of the next filter in the chain
     * @throws IOException if error occurs during the calling of the next filter in the chain
     */
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
          boolean valid = true;
          Map parameters = req.getParameterMap();

          // iterates through all the values inputted in different input fields in the form
         // checks if any of the input values contains SQL command if true it stops the iteration
          if(parameters != null) {
              Object[] inputFieldKeys = parameters.keySet().toArray();
              for (var key : inputFieldKeys ) {
                  String[] inputValues = (String[])parameters.get(key.toString());

                  for (var value:inputValues) {
                      if(checkCharacters(value) == true) {
                         valid = false;
                         break;
                      }
                  }

                  if(valid == false ){break;}
              }
          }

          // if any of the input fields contain sql injection it diplays a error message to the user
          if(valid == false) {
              try{
                  RequestDispatcher dispatcher = req.getRequestDispatcher("/error.jsp");
                  req.setAttribute("message", "Unsuccesful action. Form contains sql commands such as '<,  >,  !,  {,  } , insert, into, where, script, delete, input.'");
                  dispatcher.forward(req, resp);
              }
              catch (Exception ex) {
                  ex.printStackTrace();
              }
          }else{
              // calls the next filter in the filtering chain
              chain.doFilter(req, resp);
          }
    }

    /**
     * iterates through the SQL injection characters and check any can be found in the input value
     * @param inputValue  the value inputted in the input field
     * @return weather the input value contains any SQL injection
     */
    public static boolean checkCharacters(String inputValue) {
        boolean invalid = false;
        String[] sqlInjectionChars = { "<", ">", "script", "alert", "truncate", "delete",
                "insert", "drop", "null", "xp_", "<>", "!", "{", "}", "`",
                "input" };

        for(int i = 0; i < sqlInjectionChars.length; i++){
            if(inputValue.indexOf(sqlInjectionChars[i]) >=0){
                invalid = true;
                break;
            }
        }
        return invalid;
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
