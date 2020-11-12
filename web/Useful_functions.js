var errorMessage = document.querySelector("#error-message");

function disableSubmitButton(){
    $('#submit_btn').addClass('disabled');
    $('#submit_btn').prop('disabled', true);
}

function activateSubmitButton(){
    $('#submit_btn').removeClass('disabled');
    $('#submit_btn').prop('disabled', false);
}
function addToErrorMessage(text){
    let error = document.createElement("li");
    error.innerText = text;
    errorMessage.appendChild(error);
}
function deleteErrorMessage(message){
    for (let i = 0; i <= errorMessage.children.length-1; i++) {
        if(errorMessage.children[i].innerText === message){
            errorMessage.removeChild(errorMessage.children[i]);
        }
    }
}
