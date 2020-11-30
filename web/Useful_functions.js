/**
 * errorMessage is the unordered list of error message being displayed to the user on the a form
 * @type {Element}
 */
var errorMessage = document.querySelector("#error-message");

/**
 * @desc disables submit button preventing the user from being able to submit the form
 */
function disableSubmitButton(){
    $('#submit_btn').addClass('disabled');
    $('#submit_btn').prop('disabled', true);
}

/**
 * @desc activates the submit button allowing the user to submit the form
 */
function activateSubmitButton(){
    $('#submit_btn').removeClass('disabled');
    $('#submit_btn').prop('disabled', false);
}

/**
 * addes a new error message to the list of error messages being shown to the user
 * @param text error message being added to the list of error messages being shown to the user
 */
function addToErrorMessage(text){
    let error = document.createElement("li");
    error.innerText = text;
    errorMessage.appendChild(error);
}

/**
 * deletes a specified error message from the error message list
 * @param message is the specified error message to be deleted from the error message list
 */
function deleteErrorMessage(message){
    for (let i = 0; i <= errorMessage.children.length-1; i++) {
        if(errorMessage.children[i].innerText === message){
            errorMessage.removeChild(errorMessage.children[i]);
        }
    }
}

/**
 * @desc a error message will be shown if input fields(except passwords) contain white space
 */
function validateWhiteSpaceInputs(formId) {
    deleteErrorMessage("Inputs cannot contain white space");
    let inputs = document.getElementById(formId).elements;
    // Iterate over the form controls
    for (let i = 0; i < inputs.length; i++) {
        if (inputs[i].nodeName === "INPUT" && inputs[i].type === "text") {
            // checks if input contains white space
            if(inputs[i].value.indexOf(" ") > -1){
                addToErrorMessage("Inputs cannot contain white space");
            }
        }
    }
}