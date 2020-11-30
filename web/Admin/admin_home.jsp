<%@ page import="java.sql.DriverManager" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.Statement" %>
<%@ page import="java.sql.ResultSet" %><%--
  Created by IntelliJ IDEA.
  User: samaj
  Date: 18/11/2020
  Time: 15:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Admin HomePage</title>
    <link rel="stylesheet" href="../index.css">
</head>
<body>
    <%
        //checks if the user is logged to an account and has a authorisation role of a admin
        if(session.getAttribute("userrole") == null){
            RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
            request.setAttribute("message", "Incorrect Authorisation. You do not have the authorisation to access this page please login as a admin");
            dispatcher.forward(request, response);
        }else if(session.getAttribute("userrole").toString().toLowerCase().equals("admin") == false){
            RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
            request.setAttribute("message", "Incorrect Authorisation. You do not have the authorisation to access this page please login as a admin");
            dispatcher.forward(request, response);
        }
    %>
    <div class="main_div">
        <h1 class="text-center mb-3">Admin Account</h1>
    </div>

    <h1>User account details</h1>
    <div>
        <%= request.getAttribute("data") %>
    </div>
    <form action="CreateWinningLotteryDraw" method="post">
            <h1 class="text-center">Create winning lottery draw numbers</h1>
            <div><ul id="error-message" class="text-danger"></ul></div>
            <div class="form-group">
                <input type="number" min="0" max="60" class="form-control"  id="number-one" name="numberone" placeholder="Number one" required>
            </div>
            <div class="form-group">
                <input type="number" min="0" max="60" class="form-control"  id="number-two" name="numbertwo" placeholder="Number two" required>
            </div>
            <div class="form-group">
                <input type="number" min="0" max="60"  class="form-control"  id="number-three" name="numberthree" placeholder="Number three" required>
            </div>
            <div class="form-group">
                <input type="number" min="0" max="60" class="form-control"  id="number-four" name="numberfour" placeholder="Number four" required>
            </div>
            <div class="form-group">
                <input type="number" min="0" max="60" class="form-control"  id="number-five" name="numberfive" placeholder="Number five" required>
            </div>
            <div class="form-group">
                <input type="number" min="0" max="60" class="form-control"  id="number-six" name="numbersix" placeholder="Number six" required>
            </div>
            <input type="submit"  value="Submit">
    </form>
</body>
</html>
