<?php
session_start();
function deleteFromServer($username, $filename){
    unlink("users/".$username."/".$filename);

}
if(isset($_SESSION) && isset($_SESSION["username"]) && isset($_SESSION["id"]) && isset($_POST) && isset($_POST["filename"])){
    $filename=$_POST["filename"];
    $username=$_SESSION["username"];

    if(file_exists("users/".$username."/".$filename)){
        require_once("DB.php");


        $query="DELETE FROM u".$_SESSION["id"]." WHERE userfilename='".$filename."'";
        
        if(mysqli_query($conn, $query)){
            $queryGet="SELECT storageusage FROM user WHERE username='".$username."'";
            $resultGet=mysqli_query($conn, $queryGet);
            if(mysqli_num_rows($resultGet)){
                $row=mysqli_fetch_row($resultGet);

                $usage=floatval($row[0]);

                $total=$usage-(filesize("users/".$username."/".$filename)/1024/1024);

                $queryUpdate="UPDATE user SET storageusage='".$total."' WHERE username='".$username."' ";

                mysqli_query($conn, $queryUpdate);


                deleteFromServer($username, $filename);
            }
        }
        mysqli_close($conn);
        header("Location:main.php");
    }
    else{
        header("Location:main.php");
    }
}
else{
    header("Location:index.php");
}




?>