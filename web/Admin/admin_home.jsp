<%--
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
    <div class="main_div">
        <h1 class="text-center mb-3">Admin Account</h1>
    </div>
    <form action="UserLogin" method="post">
        <input type="submit" value="Get User data" class="submit_btn" style="display: inline-block">
        <a href="index.jsp"><button class="submit_btn" style="display:inline-block">Home Page</button></a>
    </form>

</body>
</html>
