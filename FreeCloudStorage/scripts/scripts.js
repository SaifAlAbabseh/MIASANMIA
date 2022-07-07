$(document).ready(startLoading);


function startLoading(){
    setTimeout(function(){
        $("#loadingScreen").slideToggle();
    },2000);
}

function showReqsForUsername(){
    var usernameReqs="Username Requirements:<br>&gt;Must be in length of [6-10] inclusive.<br>&gt;Must be without symbols.(Only alpha-numeric)";
    

    var reqsBox=document.getElementById("reqs");

    reqsBox.innerHTML=usernameReqs;

    $("#reqs").animate({width:"toggle"});
}
function showReqsForPassword(){
    var usernameReqs="Password Requirements:<br>&gt;Must be in length of [8-16] inclusive.<br>&gt;Must be without spaces.";
    

    var reqsBox=document.getElementById("reqs");

    reqsBox.innerHTML=usernameReqs;

    $("#reqs").animate({width:"toggle"});
}

function hideReqs(){
    $("#reqs").hide();
}

function dropMenu(){
    $("#mainDropDownMenu").slideToggle();
}

function redirectToUpload(){
    location.replace("uploadFile.php");
}

function redirectToChangeUsername(){
    location.replace("changeUsername.php");
}
function redirectToChangePassword(){
    location.replace("changePassword.php");
}