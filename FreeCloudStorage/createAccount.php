<!DOCTYPE html>
<html>
    <head>
        <title>Free Cloud Storage | Sign Up</title>
        <meta name="viewport" content="width=device-width,initial-scale=1.0">
        <link rel="stylesheet" href="styles/styles.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
        <script src="scripts/scripts.js"></script>
    </head>
    <body>
        <div id="reqs" style="display:none">
        </div>
        <div onmousedown="hideReqs()"> 
            <div id="loadingScreen">
                <img id="loadingIcon" src="images/mainIcon.png" alt="Webiste Icon">
            </div>
            <div class="imageBar">
                <img class="mainIcon" src="images/mainIcon.png" alt="Webiste Icon">
            </div>
            <div class="body">
                <div class="loginOuterBox">
                    <form class="loginBox" action="createAccount.php" method="post" autocomplete="off">
                        <input onclick="showReqsForUsername()" required type="text" name="createUsername" class="inputField" id="createUsernameField" placeholder="Username">
                        <label class="errorStar" id="createUsernameErrorStar"></label>
                        <input onclick="showReqsForPassword()" required type="password" name="createPassword" class="inputField" id="passwordField" placeholder="Password">
                        <label class="errorStar" id="createPasswordErrorStar"></label>
                        <input required type="password" name="createconfirmPassword" class="inputField" id="confirmPasswordField" placeholder="Confirm Password">
                        <label class="errorStar" id="createConPasswordErrorStar"></label>
                        <input type="submit" value="Sign Up" class="button" name="createAccountButton" id="createAccountButton">
                    </form>
                </div>
            </div>
        </div>
    </body>
</html>
<?php 

function addNewUser($username, $password){


    require_once("DB.php");




    $checkBefore="SELECT id FROM user WHERE username='".$username."'";

    $result=mysqli_query($conn,$checkBefore);


    if(mysqli_num_rows($result)){
        printError("Username already in use!","createConPasswordErrorStar");

    }
    else{
        $lastID=0;
        $q="SELECT id FROM user";
        $result22=mysqli_query($conn, $q);
        while($row=mysqli_fetch_row($result22)){
            $lastID=$row[0];
        }
        $lastID++;
        $query="INSERT INTO user VALUES ('".$lastID."','".$username."', '".$password."','0')";

        if(mysqli_query($conn, $query)){
            printError("Successfully Created a New Account!<a style=color:blue href=index.php>Return to login</a>", "createConPasswordErrorStar");
        }
        else{
            printError("Connection Error!", "createConPasswordErrorStar");
        }
    }
    

    if(!addNewTable($conn,$lastID)){
        printError("Unknown Error!", "createConPasswordErrorStar");
    }
    mysqli_close($conn);


    addNewDir($username);

}

function addNewTable($conn, $id){
    $query="CREATE TABLE u".$id." (
        userfilename VARCHAR(1000),
        filepath VARCHAR(1000),
        filesize VARCHAR(1000),
        fileuploaddate VARCHAR(1000)
    )";
    if(mysqli_query($conn,$query)){
        return true;
    }
    else{
        return false;
    }
}
function addNewDir($username){
    mkdir("users/".$username."/");
}


function printError($str, $whichOne){
    echo "<script>document.getElementById('".$whichOne."').innerHTML='".$str."';</script>";
}

function checkChar($char){

    $code=ord($char);

    if(!(($code>=48 && $code<=57) || ($code>=65 && $code<=122) || ($code==32))){
        return false;
    }
    return true;
}
function checkUsername($username){
    if(strlen($username)<6 || strlen($username)>10){
       
        return false;
    }
    else{
        for($i=0;$i<strlen($username);$i++){
            if(!checkChar("".$username[$i])){
                return false;
            }
        }
        return true;
    }
}
function checkPassword($pass){
    if(strlen($pass)>16 || strlen($pass)<8){
        return false;
    }
    else{
        for($i=0;$i<strlen($pass);$i++){
            if($pass[$i] == ' '){
                return false;
            }
        }
        return true;
    }
}
function checkPasswordsEquality($pass, $conpass){

    if($pass == $conpass) return true;
    return false;

}


if(isset($_POST) && isset($_POST["createAccountButton"])){
    $username=$_POST["createUsername"];
    $password=$_POST["createPassword"];
    $conPassword=$_POST["createconfirmPassword"];

    if(trim($password)!="" && trim($username)!="" && trim($conPassword)!=""){
        if(checkPasswordsEquality($password, $conPassword)){
            if(checkPassword($password)){
                if(checkUsername($username)){
                    addNewUser($username, $password);
                }
                else{
                    printError("Username does not match the requirements!", "createUsernameErrorStar");
                }
            }
            else{
                printError("Password does not match the requirements!","createPasswordErrorStar");
            }
        }
        else{
            printError("Passwords does not match!","createConPasswordErrorStar");
        }
    }
    else{
        if(trim($username) == ""){
            printError("You must type a username!","createUsernameErrorStar");
        }
        if(trim($password) == ""){
            printError("You must type a password!","createPasswordErrorStar");
        }
        if(trim($conPassword) == ""){
            printError("You must retype the password!","createConPasswordErrorStar");
        }
    }

}

?>