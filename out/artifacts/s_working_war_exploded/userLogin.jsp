<%--
  Created by IntelliJ IDEA.
  User: samaj
  Date: 09/11/2020
  Time: 13:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <link rel="stylesheet" href="index.css">
    <title>Log In!</title>

</head>
<body>
<%

    ;%>
<input id="session-login-attempts" type="hidden" value="<%= session.getAttribute("loginAttempts")%>">
<div class="d-flex justify-content-center align-items-center container mt-3">
    <form action="UserLogin" method="post" class="main_div">
        <h1 class="text-center">User Log In</h1>
        <div class="fields">
            <div><ul id="error-message" class="text-danger"></ul></div>
            <div class="form-group">
                <input type="text" class="form-control"  id="username" name="username" placeholder="Username" required>
            </div>

            <div class="form-group">
                <input type="password" class="form-control" id="password" name="password" placeholder="Password" required>
            </div>

            <input type="submit" id="submit_btn" value="Submit" name="submit">
            <h4 class="userInfo text-center mt-3"><a href="index.jsp">New user? Sign up !</a></h4>

        </div>



    </form>
</div>



<!-- Optional JavaScript -->
<!-- jQuery first, then Popper.js, then Bootstrap JS -->
<script src="https://code.jquery.com/jquery-3.4.1.slim.min.js" integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js" integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo" crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js" integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6" crossorigin="anonymous"></script>
<script src="Useful_functions.js"></script>
<script type="application/javascript">
    if(document.querySelector("#session-login-attempts").value >= 3){
        addToErrorMessage("You have reached the 3 login attempts limit. Form has been disabled");
        document.getElementById("username").disabled=true;
        document.getElementById("password").disabled=true;
        document.querySelector("form").disabled=true;
    }
    var password = document.querySelector("#password");
    var username = document.querySelector("#username");
    document.addEventListener("input",function(e){
        if(password.value.length > 0 && username.value.length > 0 ){
            activateSubmitButton();
        }else{
            disableSubmitButton();
        }
    })
    disableSubmitButton();
    function disableSubmitButton(){
        $('#submit_btn').addClass('disabled');
        $('#submit_btn').prop('disabled', true);
    }

    function activateSubmitButton(){
        $('#submit_btn').removeClass('disabled');
        $('#submit_btn').prop('disabled', false);
    }
</script>
</body>
</html>
