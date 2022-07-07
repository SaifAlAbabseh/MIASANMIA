<?php
session_start();

if (!(isset($_SESSION) && isset($_SESSION["username"]) && isset($_SESSION["id"]))) {
    header("Location:index.php");
}

?>
<!DOCTYPE html>
<html>

<head>
    <title>Free Cloud Storage | Change Password</title>
    <meta name="viewport" content="width=device-width,initial-scale=1.0">
    <link rel="stylesheet" href="styles/styles.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
    <script src="scripts/scripts.js"></script>
</head>

<body>
    <div id="loadingScreen">
        <img id="loadingIcon" src="images/mainIcon.png" alt="Webiste Icon">
    </div>
    <div class="getBack">
        <a class="getBackButton" href="main.php">&lt;&lt; Back</a>
    </div>
    <div class="changePasswordMainBox">
        <form action="changePassword.php" method="post" style="display:flex;flex-direction:column" autocomplete="off">
            <input type="password" id="oldPassword" name="oldPassword" class="inputField" style="width:100%" required placeholder="Old Password">
            <div class="errorStar" id="oldPass"></div>
            <input style="width:100%" id="newPasswordToChange" type="password" name="newPasswordToChange" class="inputField" required placeholder="New Password">
            <div class="errorStar" id="newPass"></div>
            <input style="width:100%" id="newPasswordToChangeConfirm" class="inputField" type="password" name="newPasswordToChangeConfirm" class="inputField" required placeholder="Confirm New Password">
            <div class="errorStar" id="newPassCon"></div>
            <input type="submit" name="changePasswordButton1" value="Change" class="button" style="width:100%">
        </form>
    </div>
</body>

</html>

<?php 

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
if(isset($_POST) && isset($_POST["changePasswordButton1"])){
    $oldPass=$_POST["oldPassword"];
    $newPass=$_POST["newPasswordToChange"];
    $newPassCon=$_POST["newPasswordToChangeConfirm"];


    if(trim($oldPass)!="" && trim($newPass)!="" && trim($newPassCon)!=""){
        if($newPass==$newPassCon){
            require_once("DB.php");

            $query="SELECT password FROM user WHERE username='".$_SESSION["username"]."'";
            $result=mysqli_query($conn, $query);
            if(mysqli_num_rows($result)){
                $row=mysqli_fetch_row($result);
                
                $oPass=$row[0];
                
                if($oPass==$oldPass){
                    if($newPass!=$oPass){
                        if(checkPassword($newPass)){
                            $queryUpdate="UPDATE user SET password='".$newPass."' WHERE username='".$_SESSION["username"]."'";
                            if(mysqli_query($conn, $queryUpdate)){
                                echo "<script>document.getElementById('newPassCon').innerHTML='Successfully changed the passsword!';</script>";
                            }
                            else{
                                echo "<script>document.getElementById('newPassCon').innerHTML='Unknown Error!';</script>";
                            }
                        }
                        else{
                            echo "<script>document.getElementById('newPass').innerHTML='Password does not match the requirements.';</script>";
                        }
                    }
                    else{
                        echo "<script>document.getElementById('newPass').innerHTML='Cannot enter the same old password!';</script>";
                    }
                }
                else{
                    echo "<script>document.getElementById('oldPass').innerHTML='Incorrect old password!';</script>";
                }
            }
            else{
                echo "<script>document.getElementById('oldPass').innerHTML='Unknown Error!';</script>";
            }
            mysqli_close($conn);
        }
        else{
            echo "<script>document.getElementById('newPassCon').innerHTML='Passwords does not match!';</script>";
        }
    }
    else{
        if(trim($oldPass)=="")  {
            echo "<script>document.getElementById('oldPass').innerHTML='Empty Field!';</script>";
        }
        if(trim($newPass)==""){
            echo "<script>document.getElementById('newPass').innerHTML='Empty Field!';</script>";
        }
        if(trim($newPassCon)==""){
            echo "<script>document.getElementById('newPassCon').innerHTML='Empty Field!';</script>";
        }
    }
}



?>