<%--
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
    var email = document.querySelector('#email');
    var password = document.querySelector("#password");
    var phone = document.querySelector("#phone");
    var errorMessage = document.querySelector("#error-message")

    document.addEventListener("input",function(e){
        if(email.value.trim().length > 0 && phone.value.trim().length > 0 && password.value.trim().length > 0 && errorMessage.children.length <= 0){
                activateSubmitButton();
        }else{
            disableSubmitButton();
        }
    });
    disableSubmitButton();
    function disableSubmitButton(){
        $('#submit_btn').addClass('disabled');
        $('#submit_btn').prop('disabled', true);
    }

    function activateSubmitButton(){
        $('#submit_btn').removeClass('disabled');
        $('#submit_btn').prop('disabled', false);
    }
    function removePasswordErrors(){
        deleteErrorMessage("Password must contain lowercase character");
        deleteErrorMessage("Password must contain uppercase character");
        deleteErrorMessage("Password must contain a digit");
        deleteErrorMessage("Password must be between 8 and 15 characters");
    }

    password.addEventListener("input",function(e){
        removePasswordErrors();
        validatePassword(password.value);
    })

    function validatePassword(text){
        console.log(text);
        if(/[a-z]/.test(text) === false){
            addToErrorMessage("Password must contain lowercase character");
        }
        if(/[A-Z]/.test(text) === false){
            addToErrorMessage("Password must contain uppercase character");
        }

        if(/\d/.test(text) === false){
            addToErrorMessage("Password must contain a digit");
        }

        if(!(text.length >= 8 && text.length <= 15)){
            addToErrorMessage("Password must be between 8 and 15 characters");
        }
    }

    phone.addEventListener("input",function (e) {
        deleteErrorMessage("Phone number invalid must be of the form xx-xxxx-xxxxxxx (e.g. 44-0191-1234567)");
        validatePhoneNumber(phone.value.trim());
    })
    email.addEventListener("input",function(e){
        deleteErrorMessage("Email is invalid");
        var inputtedText = email.value;
        if(checkIfEmailIsValid(inputtedText) === false){
            addToErrorMessage("Email is invalid");
        }
    })

    function addToErrorMessage(text){
        let error = document.createElement("li");
        error.innerText = text;
        errorMessage.appendChild(error);
    }
    function validatePhoneNumber(number){
        var  textParts = number.split("-");
        let valid = false;

        if(/^\d{2}$/.test(textParts[0]) === true && textParts.length === 3){
            if(/^\d{4}$/.test(textParts[1]) === true){
                if(/^\d{7}$/.test(textParts[2]) === true){
                    valid = true;
                }
            }
        }
        if(valid === false){
            addToErrorMessage("Phone number invalid must be of the form xx-xxxx-xxxxxxx (e.g. 44-0191-1234567)");
        }
    }
    function deleteErrorMessage(message){
        for (let i = 0; i <= errorMessage.children.length-1; i++) {
            if(errorMessage.children[i].innerText === message){
                errorMessage.removeChild(errorMessage.children[i]);
            }
        }
    }


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