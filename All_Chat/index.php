<?php
session_start();
?>
<!DOCTYPE html>
<html>

<head>
    <title>
        ALL CHAT
    </title>
    <meta name="viewport" content="width=device-width,initial-scale=1.0" />
    <link rel="stylesheet" href="Extra/styles/cssFiles/themes.css" />
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
    <style>
        body,
        html {
            width: 100%;
            height: 100%;
        }

        body {
            display: flex;
            justify-content: center;
            align-items: center;
        }
    </style>
    <script>
        $(document).ready(function() {
            var temp = 1;
            temp = parseInt("<?php if (isset($_REQUEST) && isset($_REQUEST["t"])) {echo "0";} ?>");

            if (isNaN(temp)) {
                $("#main_box-id").css({
                    "pointer-events": "none",
                    "opacity": "0.2",
                    "user-select": "none"
                });
                $("#mainDialogBox").show();
            }
            $("#exit").click(exitDialog);


            $("#userreq").click(function() {
                $(".outer_reqs_box").show();
                $("#username_reqs").show();
                $("#password_reqs").hide();
            });
            $("#passreq").click(function() {
                $(".outer_reqs_box").show();
                $("#password_reqs").show();
                $("#username_reqs").hide();
            });
        });

        function hide() {
            $(".outer_reqs_box").hide();
            $("#password_reqs").hide();
            $("#username_reqs").hide();
        }

        function exitDialog() {
            $("#main_box-id").css({
                "pointer-events": "initial",
                "opacity": "1",
                "user-select": "auto"
            });
            $("#mainDialogBox").hide();

        }
    </script>
</head>

<body>
    <div id="mainDialogBox">
        <button id="exit">X</button>
        <div id="main-dialog-inner">
            <a class="otherPlatformDownloadLink" download href="Extra/Other Platforms/All Chat.exe">Desktop Version Download</a>
            <br>
            <a class="otherPlatformDownloadLink" download href="Extra/Other Platforms/All Chat.apk">Android Version Download</a>
        </div>
    </div>
    <div class="outer_reqs_box" onclick="hide()">
        <div class="reqs" id="username_reqs">
            <p>
                Username shoud not contain spaces , symbols and it should be at least 6 characters in length and at most 12.
            </p>
        </div>
        <div class="reqs" id="password_reqs">
            <p>
                Password should not contain spaces , and it should be at least 8 characters in length and at most 16.
            </p>
        </div>
    </div>
    <div class="main_box" id="main_box-id">
        <div class="allchat_image_box">
            <img src="Extra/styles/images/main.png" alt="all_chat image" width="400px" height="400px" />
        </div>
        <div class="login_box">
            <h1 class="boxupperlabel">
                Login
            </h1>
            <form action="index.php?t=0" method="post" autocomplete="off" class="forms">
                <table>
                    <tr>
                        <td colspan="2">
                            <input type="text" name="username" class="inputfield" id="username_inputfield" placeholder="Enter Username.." required />
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <input type="password" name="userpassword" class="inputfield" id="userpassword_field" placeholder="Enter Password.." required />
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <input type="submit" name="loginButton" class="buttontag" id="login_buttontag" value="LOGIN" />
                        </td>
                        <td>
                            <input type="reset" name="login_clearbutton" class="buttontag" id="login_buttontagclear" value="CLEAR" />
                        </td>
                    </tr>
                    <tr>
                        <th colspan="2">
                            <h2 class="invalid" id="invalidforlogin">
                                INVALID
                            </h2>
                        </th>
                    </tr>
                </table>
            </form>
        </div>
        <div class="signup_box">
            <h1 class="boxupperlabel">
                Sign Up
            </h1>
            <form action="index.php?t=0" method="post" autocomplete="off" class="forms">
                <table>
                    <tr>
                        <td colspan="2">
                            <input type="text" name="signupusername" class="inputfield" id="signupusername_inputfield" placeholder="Enter A Username.." required />
                        </td>
                        <td>
                            <input type="button" value=" i " class="i" id="userreq">
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <input type="password" name="signupuserpassword" class="inputfield" id="signupuserpassword_field" placeholder="Enter A Password.." required />
                        </td>
                        <td>
                            <input type="button" value=" i " class="i" id="passreq">
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <input type="password" name="signupuserconfirmpassword" class="inputfield" id="signupuserconfirmpassword_field" placeholder="Re-Enter The Password.." required />
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <input type="submit" name="signupButton" class="buttontag" id="signup_buttontag" value="SIGNUP" />
                        </td>
                        <td>
                            <input type="reset" name="signup_clearbutton" class="buttontag" id="signup_buttontagclear" value="CLEAR" />
                        </td>
                    </tr>
                    <tr>
                        <th colspan="2">
                            <h2 class="invalid" id="invalidforsignup">
                                INVALID
                            </h2>
                        </th>
                    </tr>
                </table>
            </form>
        </div>
    </div>
</body>

</html>
<?php
if (isset($_POST) && (isset($_POST["loginButton"]) || isset($_POST["signupButton"]))) {
    extract($_POST);
    if (isset($loginButton)) {
        $un = $username;
        $password = $userpassword;
        if (trim($un) != "" && trim($password) != "") {
            require_once("DB.php");
            $query = "SELECT * FROM users WHERE BINARY username='" . $un . "' AND BINARY password='" . $password . "'";
            $result = mysqli_query($conn, $query);
            if ($result) {
                if (mysqli_num_rows($result)) {
                    $_SESSION["who"] = $un;
                    $av = "UPDATE users SET available='1' WHERE BINARY username='" . $un . "'";
                    mysqli_query($conn, $av);
                    mysqli_close($conn);
                    echo "<script>window.location.replace('Main.php');</script>";
                } else {
                    echo
                    "<script>
                         var invalidfield=document.getElementById('invalidforlogin');
                         invalidfield.style.display='block';
                    </script>";
                }
            } else {
                echo
                "<script>
                         var invalidfield=document.getElementById('invalidforlogin');
                         invalidfield.style.display='block';
                         invalidforsignup.innerHTML='Connection Error';
                    </script>";
            }
        } else {
            echo
            "<script>
                var invalidfield=document.getElementById('invalidforlogin');
                invalidfield.style.display='block';
            </script>";
        }
    } else if (isset($signupButton)) {
        $un = $signupusername;
        $password = $signupuserpassword;
        $cpass = $signupuserconfirmpassword;
        if (trim($un) != "" && trim($password) != "" && trim($cpass) != "") {
            $everythingIsGood = true;
            if (strpos($un, " ") || !(strlen($un) >= 6 && strlen($un) <= 12)) {
                $everythingIsGood = false;
            }
            if ($everythingIsGood) {
                for ($i = 0; $i < strlen($un); $i++) {
                    if (!((ord(substr($un, $i, 1)) >= 48 && ord(substr($un, $i, 1)) <= 57) || (ord(substr($un, $i, 1)) >= 65 && ord(substr($un, $i, 1)) <= 90) || (ord(substr($un, $i, 1)) >= 97 && ord(substr($un, $i, 1)) <= 122))) {
                        $everythingIsGood = false;
                        break;
                    }
                }
            }
            if (strpos($password, " ") || !(strlen($password) >= 8 && strlen($password) <= 16)) {
                $everythingIsGood = false;
            }
            if ($everythingIsGood) {
                if ($password == $cpass) {
                    require_once("DB.php");
                    $query = "SELECT username FROM users WHERE BINARY username='" . $un . "'";
                    $result = mysqli_query($conn, $query);
                    if ($result) {
                        if (mysqli_num_rows($result)) {
                            echo
                            "<script>
                                var invalidfield=document.getElementById('invalidforsignup');
                                invalidfield.style.display='block';
                                invalidforsignup.innerHTML='Username is already in use';
                            </script>";
                        } else {
                            $squery = "INSERT INTO users VALUES ('" . $un . "','" . $password . "','defaultUser','0')";
                            if (mysqli_query($conn, $squery)) {
                                echo
                                "<script>
                                    var invalidfield=document.getElementById('invalidforsignup');
                                    invalidfield.style.display='block';
                                    invalidfield.style.color='green';
                                    invalidforsignup.innerHTML='Successful';
                                </script>";
                            } else {
                                echo
                                "<script>
                                var invalidfield=document.getElementById('invalidforsignup');
                                invalidfield.style.display='block';
                                invalidforsignup.innerHTML='Connection Error';
                             </script>";
                            }
                        }
                    } else {
                        echo
                        "<script>
                            var invalidfield=document.getElementById('invalidforsignup');
                            invalidfield.style.display='block';
                            invalidforsignup.innerHTML='Connection Error';
                        </script>";
                    }
                } else {
                    echo
                    "<script>
                    var invalidfield=document.getElementById('invalidforsignup');
                    invalidfield.style.display='block';
                    invalidforsignup.innerHTML='Passwords not matching';
                    </script>";
                }
            } else {
                echo
                "<script>
                            var invalidfield=document.getElementById('invalidforsignup');
                            invalidfield.style.display='block';
                            invalidforsignup.innerHTML='Error';
                        </script>";
            }
        } else {
            echo
            "<script>
                var invalidfield=document.getElementById('invalidforsignup');
                invalidfield.style.display='block';
            </script>";
        }
    } else {
        echo "<script>window.location.replace('index.php');</script>";
    }
}
?>