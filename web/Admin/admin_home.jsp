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
    <form action="UserLogin" method="post">
        <input type="submit" value="Get User data" class="submit_btn" style="display: inline-block">
        <a href="index.jsp"><button class="submit_btn" style="display:inline-block">Home Page</button></a>
    </form>

</body>
</html>
