<!DOCTYPE html>
<html>
    <head>
        <title>Free Cloud Storage | Login</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="styles/styles.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
        <script src="scripts/scripts.js"></script>
    </head>
    <body>
        <div id="loadingScreen">
            <img id="loadingIcon" src="images/mainIcon.png" alt="Webiste Icon">
        </div>
        <div class="imageBar">
            <img class="mainIcon" src="images/mainIcon.png" alt="Webiste Icon">
        </div>
        <div class="body">
            <div class="loginOuterBox">
                <form class="loginBox" action="index.php" method="post" autocomplete="off">
                    <input required type="text" name="username" class="inputField" id="usernameField" placeholder="Username">
                    <label class="errorStar" id="usernameErrorStar"></label>
                    <input required type="password" name="password" class="inputField" id="passwordField" placeholder="Password">
                    <label class="errorStar" id="passwordErrorStar"></label>
                    <input type="submit" value="Login" class="button" id="loginButton" name="loginButton">
                </form>
                <div class="dontHaveAccount">
                    <p class="dontText">Don't have an account? <a class="signUpLink" href="createAccount.php">Sign Up</a></p>
                </div>
            </div>
        </div>
    </body>
</html>


<?php  

function printError($str, $whichOne){
    echo "<script>document.getElementById('".$whichOne."').innerHTML='".$str."';</script>";
}

function searchForUser($username, $password){
    require_once("DB.php");
    

    $query="SELECT id FROM user WHERE username='".$username."' AND password='".$password."'";

    $result=mysqli_query($conn, $query);

    mysqli_close($conn);

    if(mysqli_num_rows($result)){
        $row=mysqli_fetch_row($result);
        return "".$row[0];
    }


    return false;

    
}

if(isset($_POST) && isset($_POST["loginButton"])){
    $username=$_POST["username"];
    $password=$_POST["password"];


    if(trim($username)!="" && trim($password)!=""){
        $res=searchForUser($username, $password);
        if($res!=false){
            session_start();
            $_SESSION["username"]=$username;
            $_SESSION["id"]=$res;
            echo "

            <script>
                setTimeout(function(){
                    $('#loadingScreen').slideToggle();
                    location.replace('main.php');
                },2000);
            </script>
            
            ";
        }
        else{
            printError("Username or password is incorrect!","usernameErrorStar");
        }
    }
    else{
        if(trim($username)==""){
            printError("You must type your username!","usernameErrorStar");
        }
        if(trim($password)==""){
            printError("You must type your password!","passwordErrorStar");
        }
    }
}






?>