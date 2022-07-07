<?php
session_start();

if (!(isset($_SESSION) && isset($_SESSION["username"]) && isset($_SESSION["id"]))) {
    header("Location:index.php");
}

?>
<!DOCTYPE html>
<html>

<head>
    <title>Free Cloud Storage | Change Username</title>
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
    <div class="changeUsernameMainBox">
        <form action="changeUsername.php" method="post" style="display:flex;flex-direction:column" autocomplete="off">
            <input style="width:100%" type="text" name="changeUsernameField" required class="inputField" placeholder="Enter a New Username">
            <div class="errorStar" id="changeUsernameError"></div>
            <input style="width:100%" type="submit" value="Change" name="changeUsernameButton" class="button">
        </form>
    </div>
</body>


</html>


<?php
function checkChar($char)
{

    $code = ord($char);

    if (!(($code >= 48 && $code <= 57) || ($code >= 65 && $code <= 122) || ($code == 32))) {
        return false;
    }
    return true;
}
function checkUsername($username)
{
    if (strlen($username) < 6 || strlen($username) > 10) {

        return false;
    } else {
        for ($i = 0; $i < strlen($username); $i++) {
            if (!checkChar("" . $username[$i])) {
                return false;
            }
        }
        return true;
    }
}

if (isset($_POST) && isset($_POST["changeUsernameField"])) {
    $newUsername = $_POST["changeUsernameField"];

    if (trim($newUsername) != "") {
        if ($_SESSION["username"] != $newUsername) {
            require_once("DB.php");
            $queryCheck = "SELECT * FROM user WHERE username='" . $newUsername . "'";
            $resultCheck = mysqli_query($conn, $queryCheck);
            if (mysqli_num_rows($resultCheck)) {
                echo "<script>document.getElementById('changeUsernameError').innerHTML='This username already exists!';</script>";
            } else {
                if (checkUsername($newUsername)) {
                    $querySet = "UPDATE user SET username='" . $newUsername . "' WHERE id='" . $_SESSION["id"] . "'";
                    if (mysqli_query($conn, $querySet)) {
                        rename("users/" . $_SESSION["username"], "users/" . $newUsername);
                        echo "<script>document.getElementById('changeUsernameError').innerHTML='Successfully changed the username!';</script>";
                        $_SESSION["username"] = $newUsername;
                    } else {
                        echo "<script>document.getElementById('changeUsernameError').innerHTML='Unknown Error!';</script>";
                    }
                }
                else{
                    echo "<script>document.getElementById('changeUsernameError').innerHTML='Username does not match the requirements.';</script>";
                }
            }

            mysqli_close($conn);
        } else {
            echo "<script>document.getElementById('changeUsernameError').innerHTML='Cannot enter your old username!';</script>";
        }
    } else {
        echo "<script>document.getElementById('changeUsernameError').innerHTML='Type a new username!';</script>";
    }
}

?>