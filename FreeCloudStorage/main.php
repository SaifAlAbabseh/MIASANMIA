<?php
session_start();

if (!(isset($_SESSION) && isset($_SESSION["username"]) && isset($_SESSION["id"]))) {
    header("Location:index.php");
}

?>
<!DOCTYPE html>
<html>

<head>
    <title>Free Cloud Storage | Main</title>
    <meta name="viewport" content="width=device-width,initial-scale=1.0">
    <link rel="stylesheet" href="styles/styles.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
    <script src="scripts/scripts.js"></script>
</head>

<body>
    <div id="loadingScreen">
        <img id="loadingIcon" src="images/mainIcon.png" alt="Webiste Icon">
    </div>
    <div class="firstNavBar">
        <div class="mainUserData">
            <div onmousedown="dropMenu()" class="mainUsernameAndArrow">
                <h2 class="mainUsername">
                    <?php
                    echo $_SESSION["username"];
                    ?>
                    <img class="menuArrowDown" src="images/downArrow.png" alt="downArrow">
                </h2>
            </div>

            <div id="mainDropDownMenu" class="dropDownMenu">
                <a class="logoutButton" href="logout.php">Logout</a>
            </div>
        </div>
        <div class="UploadButtonBox">
            <button id="uploadButton" class="button" onclick="redirectToUpload()">Upload New Files</button>
        </div>
        <div class="changeUsernameButtonBox">
            <button id="changeUsernameButton" class="button" onclick="redirectToChangeUsername()">Change Username</button>
        </div>
        <div class="changePasswordButton">
            <button id="changePasswordButton" class="button" onclick="redirectToChangePassword()">Change Password</button>
        </div>
    </div>
    <div class="storageCapacityUsageOuter">
        <div class="storageCapacityUsage">
            <?php 
            require_once("DB.php");
            
            $getUsageQuery="SELECT storageusage FROM user WHERE username='".$_SESSION["username"]."'";
            $getUsageresult=mysqli_query($conn, $getUsageQuery);

            if(mysqli_num_rows($getUsageresult)){
                $row=mysqli_fetch_row($getUsageresult);


                $left=round((2*1024)-floatval($row[0]),2);
                $usage= round(floatval($row[0]),2);


                echo "<div><h2 class='usageBox'>Storage Capacity Left: ".$left." MB</h2></div><div><sub>0</sub><meter class='usageMeter' min='0' max='2048' value='".$usage."'></meter><sub>2048</sub></div>";
            }
            
            
            
            ?>
        </div>
    </div>
    <div class="mainBodyOuter">
        <div class="mainBody">
            <table>
                <tr style="color:darkred;text-shadow:0 0 5px red;">
                    <th></th>
                    <th>
                        File Name
                    </th>
                    <th>
                        File Size
                    </th>
                    <th>
                        Upload Date
                    </th>
                    <th>
                    </th>
                </tr>
                <?php
                $query = "SELECT * FROM u" . $_SESSION["id"] . "";
                $result = mysqli_query($conn, $query);
                if (mysqli_num_rows($result)) {
                    while ($row = mysqli_fetch_row($result)) {
                        addRow($row[0], $row[2], $row[3]);
                    }
                } else {
                    echo "<tr><td></td><th><h1>No Files!</h1></th><td></td></tr>";
                }

                mysqli_close($conn);


                function addRow($fileName, $fileSize, $fileDate)
                {
                    $row = "
                    <tr>
                        <form action='deleteFile.php' method='post'>
                            <td><img src='images/fileIcon.png' class='fileIcon'></td>
                            <td><input type='hidden' name='filename' value='" . $fileName . "'><a class='downloadLink' href='downloadFile.php?n=" . $fileName . "'>" . $fileName . "</a></td>
                            <td style='padding-left:40px;padding-right:40px;font-weight:bolder;'>" . $fileSize . " MB </td>
                            <td style='font-weight:bolder;'>" . $fileDate . "</td>
                            <td><input style='width:100%' type='submit' value='Delete' class='button'></td>
                        </form>
                    </tr><tr><td></td><td><br></td><td></td></tr>";

                    echo $row;
                }
                ?>
            </table>
        </div>
    </div>
</body>

</html>