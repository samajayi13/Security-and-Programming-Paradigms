<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="java.sql.DriverManager" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.Statement" %>
<%@ page import="java.sql.ResultSet" %>
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
    <title>Account!</title>
    <style type="text/css">
        table{
            width: 100% !important;
        }
    </style>
</head>
<body>
<div class="container main_div">
    <%
        //checks if the user is logged to an account and has a authorisation role of a user
        session = request.getSession();

        if(session.getAttribute("userrole") == null){
            RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
            request.setAttribute("message", "Incorrect Authorisation. You do not have the authorisation to access this page please login as a user");
            dispatcher.forward(request, response);
        }else if(session.getAttribute("userrole").toString().toLowerCase().equals("user") == false){
            RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
            request.setAttribute("message", "Incorrect Authorisation. You do not have the authorisation to access this page please login as a user");
            dispatcher.forward(request, response);
        }

    ;%>
    <h1 class="text-center mb-3">User Account</h1>
    <p class="ml-5"><%= request.getAttribute("message") %></p>
    <table class="table table-striped table-dark">
        <thead>
        <tr>
            <th scope="col">First Name</th>
            <th scope="col">Last Name</th>
            <th scope="col">Email</th>
            <th scope="col">Phone Number</th>
            <th scope="col">Username</th>
        </tr>
        </thead>
        <tbody>
            <tr>
                <td><%= session.getAttribute("firstname") %></td>
                <td><%= session.getAttribute("lastname") %></td>
                <td><%= session.getAttribute("email") %></td>
                <td><%= session.getAttribute("phone") %></td>
                <td><%= session.getAttribute("username") %></td>
            </tr>
        </tbody>
    </table>
    <a href="index.jsp"><button class="submit_btn" style="display:inline-block">Log out</button></a>


    <div>
        <h3 class="text-center mt-5"><u>Your Draws</u></h3>
        <small>click on get draws button to get your latest draws</small>
        <p>
            <%
            int i = 1;
            if(request.getAttribute("draws") != null){
                List<String> draws = (List<String>) request.getAttribute("draws");
                %>
                <% for (String draw: draws
                ) { %>
                <span style="font-weight: bold">Draw<%=i%>: <%=draw%></span><br>
                    <%i++; %>
                <%} %>
            <%} %>

        </p>

        <div class="row">
            <div class="col"><form method="post" action="GetUserNumbers">
                <input style="margin-left: 30vw" type="submit" value="Get Draws">
            </form></div>
            <div class="col"><form method="post" action="CheckForWinningNumbers">
                <input type="submit" value="Check Against Lottery">
            </form></div>
        </div>
    </div>

    <form action="AddUserNumbers" method="post" class="mt-5">
        <h1 class="text-center">User Numbers</h1>
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

        <button  id="random-btn" class="btn-large">Random Numbers</button>
        <input type="submit" id="submit_btn" value="Submit">
    </form>
</div>

<!-- Optional JavaScript -->
<!-- jQuery first, then Popper.js, then Bootstrap JS -->
<script src="https://code.jquery.com/jquery-3.4.1.slim.min.js" integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js" integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo" crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js" integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6" crossorigin="anonymous"></script>

<!--IMPORTANT: Useful_function contains javascript functions that are frequnetly used crossed the applicaiton. This stop duplication of code -->
<script src="Useful_functions.js"></script>

<script>
    var  inputs = document.getElementsByTagName("input");

    /**
     * disables the submit button when document has loaded
     */
    disableSubmitButton();

    /**
     * @desc when the user inputs a value into the document it checks all input fields that is a lotter draw number
     * checks if they are between 0 and 60 inclusive. If they are not an error message is displayed to the user
     * if they are and there is no error message on the page the submit button is activated
     */
    document.addEventListener("input",function() {
        let valid = true ;

        for(let i = 0; i <= inputs.length-1; i++) {
            deleteErrorMessage(inputs[i].id.replace("-"," ")+" needs to be between the range of 0 and 60");
            console.log(inputs[i].value);
            if(inputs[i].value.toString().length <= 0 && inputs[i].id.includes("number") === true){
                valid = false;
            }else if(inputs[i].value > 60 || inputs[i].value < 0){
                addToErrorMessage(inputs[i].id.replace("-"," ")+" needs to be between the range of 0 and 60");
                valid = false;
            }
        }

        if(valid === true && errorMessage.children.length <=0){
            activateSubmitButton();
        }
        else{
            disableSubmitButton();

        }
    });

    /**
     * @desc if the user decides to get a randomly secure draw it populates the lottery number input form with  6 numbers
     * each number will be between 0 and 60 inclusive
     * activates submit button and allows the user to submit the numbers
     */
    document.querySelector("#random-btn").addEventListener("click",function(e){
        const upperBound = 60;

        // Creates an array and fills with cryptographically secure random numbers
        let randomNumbers = new Uint8Array(100);
        window.crypto.getRandomValues(randomNumbers);

        // MOD 60 to make sure number is falls in the boundaries of 0 to 60 inclusive
        for(let i = 0; i <= inputs.length -1 ;i++){
            if(inputs[i].id.includes("number")){
                inputs[i].value = (randomNumbers[i] % upperBound).toString();
            }
        }

        e.preventDefault();
        activateSubmitButton();
    });

</script>
</body>
</html>
