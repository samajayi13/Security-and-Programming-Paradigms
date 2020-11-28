<%@ page import="java.util.Random" %>
<%@ page import="java.sql.*" %><%--
  Created by IntelliJ IDEA.
  User: johnmace
  Date: 21/10/2020
  Time: 15:57
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
    <title>Create Account!</title>

</head>
<body>
<%
    /**
     * If a winning random lottery draw numbers has not be made it and user has not signed in yet
     * it creates 6 random numbers as the lucky lottery numbers and adds it to the database
     */
    session = request.getSession();
    if(session.getAttribute("username") == null && session.getAttribute("drawCreated") == null) {
        String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
        String USER = "user";
        String PASS = "password";
        String DB_URL = "jdbc:mysql://localhost:33333/lottery";
        Class.forName(JDBC_DRIVER);
        Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
        Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
        String winningLotteryNumbers  = "";
        // Creates 6 random numbers and concatenates them using commas
        for (int i = 0; i < 6; i++) {
            int randomNumber = (int) ((Math.random() * ((60 - 0) + 1)) + 0);
            //if i points to the last number to prevent a extra unnecessary comma being added
            winningLotteryNumbers += i != 5 ? randomNumber + "," : randomNumber;
        }
        // stores the 6 random numbers as the winning random lottery draw numbers
        String query = String.format("INSERT INTO RandomLotteryDraw(Numbers,TimeCreated) VALUES('%s',NOW())",winningLotteryNumbers);
        stmt.execute(query);
        conn.close();
        // sets drawCreated attribute to true so a new winning random lottery draw numbers won't be made more than once during a session
        session.setAttribute("drawCreated",true);
    }
%>
<div class="d-flex justify-content-center align-items-center container mt-3">
    <form action="CreateAccount" method="post" class="main_div">
        <h1 class="text-center">Home Page</h1>
        <div class="fields">
            <div><ul id="error-message" class="text-danger"></ul></div>
            <div class="form-group">
                <input class="form-control" type="text" id="firstname" name="firstname"placeholder="First name" required>
            </div>
            <div class="form-group">
                <input type="text" class="form-control" id="lastname" name="lastname" placeholder="Last name " required>
            </div>

            <div class="form-group">
                <input type="email" class="form-control"  id="email" name="email" placeholder="Email" required>
            </div>

            <div class="form-group">
                <input type="text" class="form-control" id="phone" name="phone" placeholder="Phone number" required>
            </div>

            <div class="form-group">
                <input type="text" class="form-control" id="username" name="username" placeholder="Username" required>
            </div>

            <div class="form-group">
                <input type="password" class="form-control" id="password" name="password" placeholder="Password" required>
            </div>

            <div class="form-group">
                <label for="userRole">Select a role</label>
                <select class="form-control" id="userRole" name="userrole">
                    <option>Admin</option>
                    <option>User</option>
                </select>
            </div>
            <input type="submit" id="submit_btn" value="Submit" name="submit">
            <h4 class="userInfo text-center mt-3"><a href="userLogin.jsp">Already a user? Login</a></h4>
        </div>
    </form>
</div>
<!-- Optional JavaScript -->
<!-- jQuery first, then Popper.js, then Bootstrap JS -->
<script src="https://code.jquery.com/jquery-3.4.1.slim.min.js" integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js" integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo" crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js" integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6" crossorigin="anonymous"></script>

<script type="application/javascript">
    /**
        Input fields assigned to variable names
    */
    var email = document.querySelector('#email');
    var password = document.querySelector("#password");
    var phone = document.querySelector("#phone");
    var errorMessage = document.querySelector("#error-message");

    disableSubmitButton();

    /**
        *@desc Function is ran when the user enters any character in the document.
        Function checks if all input fields contain a value and that there is no error with
        the data entered in the input fields. Activates submit button if all conditions are met.
     */
    document.addEventListener("input",function() {
        if(email.value.trim().length > 0 && phone.value.trim().length > 0 && password.value.trim().length > 0 && errorMessage.children.length <= 0){
            activateSubmitButton();
        }else{
            disableSubmitButton();
        }
    });

    /**
     *@desc disables submit button and stops form from being submitted
     */
    function disableSubmitButton(){
        $('#submit_btn').addClass('disabled');
        $('#submit_btn').prop('disabled', true);
    }

    /**
      @desc allows form to be submitted
     */
    function activateSubmitButton() {
        $('#submit_btn').removeClass('disabled');
        $('#submit_btn').prop('disabled', false);
    }

    /**
     * @desc removes all possible password error from the error message list
     */
    function removePasswordErrors() {
        deleteErrorMessage("Password must contain lowercase character");
        deleteErrorMessage("Password must contain uppercase character");
        deleteErrorMessage("Password must contain a digit");
        deleteErrorMessage("Password must be between 8 and 15 characters");
    }

    /**
        *@desc when a value is inputted into password field, function removes existing password errors from the error
        *  list and passes value inputted to be validated
     */
    password.addEventListener("input",function() {
        removePasswordErrors();
        validatePassword(password.value);
    });

    /**
     * @desc validates password against criteria:
     * must contain a lower case and upper case character
     * must contain a number
     * must be between the length of 8 to 15 characters long
     * @param string text - the value inputted into password field
     */
    function validatePassword(text) {
        if(/[a-z]/.test(text) === false) {
            addToErrorMessage("Password must contain lowercase character");
        }
        if(/[A-Z]/.test(text) === false) {
            addToErrorMessage("Password must contain uppercase character");
        }
        if(/\d/.test(text) === false) {
            addToErrorMessage("Password must contain a digit");
        }
        if(!(text.length >= 8 && text.length <= 15)) {
            addToErrorMessage("Password must be between 8 and 15 characters");
        }
    }

    /**
     * @desc Removes any phone related error message and calls for the value in phone input field to be validated
     */
    phone.addEventListener("input",function() {
        deleteErrorMessage("Phone number invalid must be of the form xx-xxxx-xxxxxxx (e.g. 44-0191-1234567)");
        validatePhoneNumber(phone.value.trim());
    });

    /**
     * @desc Removes any email related error message and calls for the value in email input field to be validated
     */
    email.addEventListener("input",function() {
        deleteErrorMessage("Email is invalid");
        var inputtedText = email.value;
        if(checkIfEmailIsValid(inputtedText) === false){
            addToErrorMessage("Email is invalid");
        }
    });

    /**
     * @desc adds a error message to the list of error messages
     * @param text string - error message to add to the list of error messages
     */
    function addToErrorMessage(text) {
        let error = document.createElement("li");
        error.innerText = text;
        errorMessage.appendChild(error);
    }

    /**
     * @desc validates phone number against criteria:
     * must be of the form xx-xxxx-xxxxxxx (e.g. 44-0191-1234567)
     * if it is not valid adds a phone error into error message list
     * @param number string - value entered in the phone input valid
     */
    function validatePhoneNumber(number) {
        let phoneNumberParts = number.split("-");
        let valid = false;

        if(/^\d{2}$/.test(phoneNumberParts[0]) === true && phoneNumberParts.length === 3) {
            if(/^\d{4}$/.test(phoneNumberParts[1]) === true) {
                if(/^\d{7}$/.test(phoneNumberParts[2]) === true) {
                    valid = true;
                }
            }
        }

        if(valid === false) {
            addToErrorMessage("Phone number invalid must be of the form xx-xxxx-xxxxxxx (e.g. 44-0191-1234567)");
        }
    }

    /**
     * @desc deletes a error message from the list of error messages
     * @param message string - the error message to delete from list of error messages
     */
    function deleteErrorMessage(message) {
        for (let i = 0; i <= errorMessage.children.length-1; i++) {
            if(errorMessage.children[i].innerText === message) {
                errorMessage.removeChild(errorMessage.children[i]);
            }
        }
    }

    /**
     * @desc checks if value entered in email can be a valid email account
     * @param email
     * @returns {boolean}
     */
    function checkIfEmailIsValid(email) {
        var emailRegex = /^[A-Z0-9_'%=+!`#~$*?^{}&|-]+([\.][A-Z0-9_'%=+!`#~$*?^{}&|-]+)*@[A-Z0-9-]+(\.[A-Z0-9-]+)+$/i;
        if(emailRegex.test(email))
            return true;
        else
            return false;
    }
</script>
</body>
</html>