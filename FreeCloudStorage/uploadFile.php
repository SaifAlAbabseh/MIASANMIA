<?php
session_start();


if (!(isset($_SESSION) && isset($_SESSION["username"]) && isset($_SESSION["id"]))) {
    header("Location:index.php");
}

?>
<!DOCTYPE html>
<html>

<head>
    <title>Free Cloud Storage | Upload Files</title>
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
    <div style="height:100%;display:flex;align-items:center;justify-content:center">
        <form action="uploadFile.php" method="post" enctype="multipart/form-data">
            <div class="fileReqs">
                File must not be bigger than 5 MB.
            </div>
            <input type="file" name="userFile" class="inputField2" id="fileField" required>
            <br><br>
            <input type="submit" name="uploadButton2" class="button" value="Upload" id="uploadButton2">
            <div style="text-align:center;" class="errorStar" id="uploadError">
            </div>
        </form>
    </div>
</body>

</html>

<?php

function addFileToDB($conn, $fileName, $filePath, $fileSize, $fileUploadDate, $usageAfter)
{

    $query = "INSERT INTO u" . $_SESSION["id"] . " VALUES ('" . $fileName . "','" . $filePath . "','" . $fileSize . "','" . $fileUploadDate . "')";
    if (mysqli_query($conn, $query)) {
        $query2="UPDATE user SET storageusage='".$usageAfter."' WHERE username='".$_SESSION["username"]."'";
        if(mysqli_query($conn, $query2)){
            return true;
        }
        else{
            return false;
        }
    } else {
        return false;
    }
}
if (isset($_POST) && isset($_POST["uploadButton2"])) {

    $fileSize = $_FILES["userFile"]["size"];


    require_once("DB.php");

    $query = "SELECT storageusage FROM user WHERE username='" . $_SESSION["username"] . "'";

    $result = mysqli_query($conn, $query);
    if (mysqli_num_rows($result)) {
        $row = mysqli_fetch_row($result);


        $lastTotal = floatval($row[0]);

        $afterTotal=($lastTotal + ($fileSize / 1024 / 1024));

        if ($afterTotal <= (2 * 1024)) {
            if (($fileSize / 1024 / 1024) <= 5) {
                $fileName = basename($_FILES["userFile"]["name"]);
                $dir = "users/" . $_SESSION["username"] . "/";
                $temp = $_FILES["userFile"]["tmp_name"];
                $des = $dir . $fileName;
                if (move_uploaded_file($temp, $des)) {
                    date_default_timezone_set("Asia/Amman");
                    $date = date("d/M/Y h:i:s A");
                    if (addFileToDB($conn, $fileName, $des, round(($fileSize / 1024 / 1024), 2), $date, $afterTotal)) {
                        echo "<script>document.getElementById('uploadError').innerHTML='Successfully Uploaded.';</script>";
                    } else {
                        echo "<script>document.getElementById('uploadError').innerHTML='Unknown Error!';</script>";
                    }
                } else {
                    echo "<script>document.getElementById('uploadError').innerHTML='Unknown Error!';</script>";
                }
            } else {
                echo "<script>document.getElementById('uploadError').innerHTML='File size is bigger than 1 MB!';</script>";
            }
        } else {
            echo "<script>document.getElementById('uploadError').innerHTML='You exceeded the limit of storage capacity!';</script>";
        }
    } else {
        header("Location:index.php");
    }

    mysqli_close($conn);
}


?>