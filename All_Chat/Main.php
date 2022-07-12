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
        Welcome To All Chat
    </title>
    <meta name="viewport" content="width=device-width,initial-scale=1.0" />
    <link rel="stylesheet" href="Extra/styles/cssFiles/themes.css" />
    <style>
        body,
        html {
            height: 100%;
        }
        @media only screen and (max-width:1430px) {
            body {
                display: flex;
                align-items: center;
                justify-content: center;
            }
            .mainHeaderImage{
                display:none;
            }
        }
    </style>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
    <script>
        function toggle(){
            if ($("#m").hasClass("menuC1")) {
                    $("#m").toggleClass("menuC2");
                } else if ($("#m").hasClass("menuC2")) {
                    $("#m").toggleClass("menuC1");
                } else {
                    $("#m").toggleClass("menuC1");
                }
                $("#mainHeader").slideToggle();
        }
        $(document).ready(function() {
            $("#exit_menu_button").click(function(){
                toggle();
                $("#friendBox").css({"pointer-events": "initial","opacity": "1","user-select": "auto"});
            });
            $("#m").click(function(){
                toggle();
                $("#friendBox").css({"pointer-events": "none","opacity": "0.2","user-select": "none"});
            });
            setInterval(function() {
                $("#innerData").load("friendsync.php");
            }, 2000);
        });
        function gotout(){
            window.location.replace("logout.php");
        }
    </script>
</head>

<body onbeforeunload ="gotout()">
    <div class="whole-box">
        <div class="menuBar">
            <img style="cursor:pointer" id="m" width="40px" height="40px" src="Extra/styles/images/menu.png" alt="menu icon">
        </div>
        <div class="mainHeaderImage" id="mainHeader">
            <img src="Extra/styles/images/main.png" alt="All Chat Image" width="100%" height="30%" />
            <a href="Add.php" class="link" id="addLink">Add New Friend</a>
            <br /><br /><br /><br /><br /><br />
            <a href="Edit.php" class="link" id="editLink">Edit Profile</a>
            <br /><br /><br /><br />
            <hr class="hrLine" />
            <?php
            require_once("DB.php");
            $query = "SELECT picture FROM users WHERE BINARY username='" . $_SESSION["who"] . "'";
            $result = mysqli_query($conn, $query);
            if ($result) {
                if (mysqli_num_rows($result)) {
                    $row = mysqli_fetch_row($result);
                    echo
                    "
                    <div class='profileBox'>
                        <img src='showImg.php?u=".$_SESSION["who"]."' width='100px' height='100px' style='border-radius:50%'/>
                        <br />
                        <h2 style='color:yellow'>" . $_SESSION["who"] . "</h2>
                        <br />
                        <a class='link' href='logout.php'>Logout</a>
                    </div>
                    ";
                } else {
                    destroy();
                }
            } else {
                destroy();
            }
            function destroy()
            {
                session_destroy();
                header("Location:index.php");
            }
            ?>
            <br />
            <div id="exit_menu_button_box">
                <div id="exit_menu_button">X</div>
            </div>
        </div>
        
        <div class="friendsListBox" id="friendBox">
            <table>
                <thead>
                    <tr>
                        <th colspan="3" style="color:red">
                            > Your Friends :
                        </th>
                    </tr>
                </thead>
                <tbody id="innerData">
                </tbody>
            </table>
        </div>
    </div>
</body>

</html>
<?php 
mysqli_close($conn);
?>