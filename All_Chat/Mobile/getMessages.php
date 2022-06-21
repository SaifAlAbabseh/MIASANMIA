<?php 
if(isset($_REQUEST) && isset($_REQUEST["check"]) && $_REQUEST["check"]=="fromMobile1090" && isset($_REQUEST["username"]) && isset($_REQUEST["password"]) && isset($_REQUEST["tableName"])){
    $tablename=$_REQUEST["tableName"];

    require_once("../DB.php");

    $query="SELECT * FROM ".$tablename."";
    $result=mysqli_query($conn,$query);
    if($result){
        if(mysqli_num_rows($result)){
               $res="";
               
               while($row=mysqli_fetch_row($result)){
                   $res.=$row[0]."|".$row[1]."&";
               }
               echo $res;
        }
        else{
            echo "No Messages";
        }
    }
    else{
        echo "Unknown Error";
    }
}
else{
    header("../Location:index.php");
}
?>