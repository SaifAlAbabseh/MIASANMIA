<?php
session_start();
if (!(isset($_SESSION) && isset($_SESSION["who"]))) {
    header("Location:index.php");
}
?>
<!DOCTYPE html>
<html>

<head>
    <title>
        All Chat | Add Friend
    </title>
    <meta name="viewport" content="width=device-width,initial-scale=1.0" />
    <link rel="stylesheet" href="Extra/styles/cssFiles/themes.css" />
    <style>
        body {
            background-color: rgb(247, 247, 247);
        }

        body,
        html {
            height: 100%;
        }
    </style>
</head>

<body>
    <div class="backButton">
        <a href="Main.php"><img class="backbuttonlink" src="Extra/styles/images/backButton.png" alt="Back Button" width="50px" height="50px" /></a>
    </div>
    <div class="add_box">
        <form action="Add.php" method="post">
            <table>
                <tr>
                    <td>
                        <input type="text" class="inputfield" name="friendUsername" id="friendUsername_field" required />
                    </td>
                    <td>
                        <input type="submit" name="addFriendButton" class="buttontag" id="addFriendButton" value="ADD">
                    </td>
                </tr>
                <tr>
                    <th colspan="2">
                        <h2 id="addfriendLabel">
                        </h2>
                    </th>
                </tr>
            </table>
        </form>
    </div>
</body>

</html>
<?php
if (isset($_POST) && isset($_POST["addFriendButton"])) {
    extract($_POST);
    $fusername = $friendUsername;
    $you = $_SESSION["who"];
    if (trim($fusername) != "" && $fusername != $you) {
        require_once("DB.php");
        $query = "SELECT * FROM users WHERE BINARY username='" . $fusername . "'";
        $result = mysqli_query($conn, $query);
        if ($result) {
            if (mysqli_num_rows($result)) {
                $query2 = "SELECT * FROM friends WHERE BINARY user1='" . $you . "' AND BINARY user2='" . $fusername . "' OR BINARY user1='" . $fusername . "' AND BINARY user2='" . $you . "'";
                $result2 = mysqli_query($conn, $query2);
                if ($result2) {
                    if (mysqli_num_rows($result2)) {
                        echo
                        "
                        <script>
                            var errorfield=document.getElementById('addfriendLabel');
                            errorfield.style.color='red';
                            errorfield.innerHTML='Already A Friend.';
                        </script>
                        ";
                    } else {
                        $query3 = "INSERT INTO friends VALUES ('" . $you . "','" . $fusername . "')";
                        if (mysqli_query($conn, $query3)) {
                            $tablename = "" . $_SESSION["who"] . "" . $fusername;
                            $query4 = "CREATE TABLE " . $tablename . " (fromwho varchar(1000) , message varchar(1000),pos varchar(1000))";
                            if (mysqli_query($conn, $query4)) {
                                echo
                                "
                                <script>
                                    var errorfield=document.getElementById('addfriendLabel');
                                    errorfield.style.color='green';
                                    errorfield.innerHTML='Successfully Added.';
                                </script>
                            ";
                            }
                        } else {
                            echo "<script>window.alert('Connection Error.');</script>";
                        }
                    }
                } else {
                    echo "<script>window.alert('Connection Error.');</script>";
                }
            } else {
                echo
                "
                <script>
                    var errorfield=document.getElementById('addfriendLabel');
                    errorfield.style.color='red';
                    errorfield.innerHTML='Username Not Valid.';
                </script>
                ";
            }
        } else {
            echo "<script>window.alert('Connection Error.');</script>";
        }
        mysqli_close($conn);
    } else {
        echo
        "
        <script>
        var errorfield=document.getElementById('addfriendLabel');
        errorfield.style.color='red';
        errorfield.innerHTML='INVALID';
        </script>
        ";
    }
} else {
    header("Add.php");
}
?>