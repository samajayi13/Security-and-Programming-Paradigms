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
<div class="d-flex justify-content-center align-items-center container mt-3">
    <form action="CreateAccount" method="post" class="main_div" id="signUp-form">
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

<!--IMPORTANT: Useful_function contains javascript functions that are frequnetly used crossed the applicaiton. This stop duplication of code -->
<script src="Useful_functions.js"></script>

<script type="application/javascript">
    /**
     * Input fields assigned to variable names
    */
    var email = document.querySelector('#email');
    var password = document.querySelector("#password");
    var phone = document.querySelector("#phone");
    var errorMessage = document.querySelector("#error-message");

    disableSubmitButton();

    /**
     *@desc function is ran when the user enters any character in the document's input fields
     * function checks if all input fields contain a value and that there is no errors with
     the data entered in the input fields. Activates submit button if all conditions are met
     */
    document.addEventListener("input",function() {
        validateWhiteSpaceInputs("signUp-form");
        if(email.value.trim().length > 0 && phone.value.trim().length > 0 && password.value.trim().length > 0 && errorMessage.children.length <= 0){
            activateSubmitButton();
        }else{
            disableSubmitButton();
        }
    });

    /**
     * @desc removes any email related error message and calls for the value in email input field to be validated
     */
    email.addEventListener("input",function() {
        deleteErrorMessage("Email is invalid");
        let inputtedText = email.value;
        if(checkIfEmailIsValid(inputtedText) === false){
            addToErrorMessage("Email is invalid");
        }
    });

    /**
     * @desc checks if value entered in email can be a valid email account
     * @param email string - the value inputted in the email input field
     * @returns {boolean}
     */
    function checkIfEmailIsValid(email) {
        let emailRegex = /^[A-Z0-9_'%=+!`#~$*?^{}&|-]+([\.][A-Z0-9_'%=+!`#~$*?^{}&|-]+)*@[A-Z0-9-]+(\.[A-Z0-9-]+)+$/i;
        return emailRegex.test(email);
    }

    /**
     * @desc Removes any phone related error message and calls for the value in phone input field to be validated
     */
    phone.addEventListener("input",function() {
        deleteErrorMessage("Phone number invalid must be of the form xx-xxxx-xxxxxxx (e.g. 44-0191-1234567)");
        validatePhoneNumber(phone.value.trim());
    });

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
     * @desc validates password against criteria:
     * must contain a lower case and upper case character
     * must contain a number
     * must be between the length of 8 to 15 characters long
     * @param text string - the value inputted into password field
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
     *@desc when a value is inputted into password field, function removes existing password errors from the error
     *  list and passes value inputted to be validated
     */
    password.addEventListener("input",function() {
        removePasswordErrors();
        validatePassword(password.value);
    });

    /**
     * @desc removes all possible password error from the error message list
     */
    function removePasswordErrors() {
        deleteErrorMessage("Password must contain lowercase character");
        deleteErrorMessage("Password must contain uppercase character");
        deleteErrorMessage("Password must contain a digit");
        deleteErrorMessage("Password must be between 8 and 15 characters");
    }
</script>
</body>
</html>