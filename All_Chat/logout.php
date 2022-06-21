<?php
session_start();
if(isset($_SESSION) && isset($_SESSION["who"])){
    session_destroy();
    require_once("DB.php");
    $query="UPDATE users SET available='0' WHERE BINARY username='".$_SESSION["who"]."'";
    mysqli_query($conn,$query);
    mysqli_close($conn);
    header("Location:index.php");
}
else{
    header("Location:index.php");
}
?>