<?php

session_start();
if (isset($_SESSION) && isset($_SESSION["username"]) && isset($_SESSION["id"]) && isset($_REQUEST) && isset($_REQUEST["n"])) {
    if (file_exists("users/" . $_SESSION["username"] . "/" . $_REQUEST["n"])) {
        header('Content-Description: File Transfer');
        header('Content-Type: application/octet-stream');
        header("Cache-Control: no-cache, must-revalidate");
        header("Expires: 0");
        header('Content-Disposition: attachment; filename="' . basename("users/" . $_SESSION["username"] . "/" . $_REQUEST["n"]) . '"');
        header('Content-Length: ' . filesize("users/" . $_SESSION["username"] . "/" . $_REQUEST["n"]));
        header('Pragma: public');
        readfile("users/" . $_SESSION["username"] . "/" . $_REQUEST["n"]);
    }
    else{
        header("Location:main.php");
    }
} else {
    header("Location:index.php");
}
