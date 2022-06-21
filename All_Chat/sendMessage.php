<?php
session_start();
if (!(isset($_SESSION) && isset($_SESSION["who"]) && isset($_REQUEST) && isset($_REQUEST["with"]) && isset($_REQUEST["message"]) && isset($_REQUEST["lastI"]) && isset($_REQUEST["tname"]))) {
    header("Location:index.php");
}
else{
    $message=$_REQUEST["message"];
    $towho=$_REQUEST["with"];
    $you=$_SESSION["who"];
    $tname=$_REQUEST["tname"];
    $lastI=$_REQUEST["lastI"];
    if(trim($message)!=""){
        if(strlen($message)<=700){
            sendMessage($message,$you,$towho,$tname,$lastI);
        }
        else{
            echo "Error : Message Length More Than 700";
        }
    }
    else{
        echo "Error : Field Cannot Be Empty";
    }
}
function sendMessage($message,$you,$towho,$tname,$lastI){
    $lastI++;
    require_once("DB.php");
    $query="INSERT INTO ".$tname." VALUES ('".$you."','".$message."','".$lastI."')";
    if(mysqli_query($conn,$query)){
        echo "ok";
    }
    else{
        echo "Connection Error";
    }
    mysqli_close($conn);
}
?>