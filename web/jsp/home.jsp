<%@ page contentType="text/html;charset=UTF-8"%>
<html>
<head>
    <title>Bartender Home Page</title>
    <meta charset="utf-8"/>
    <link href="../css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
    <link href="../css/bartender.css" rel="stylesheet">
    <script src="../js/bootstrap.min.js"></script>
</head>
<body>
    <div class="float-right">
        <h3><a href="controller?command=login">Sign In</a> or <a href="controller?command=register">Sign Up</a></h3></br>
    </div>

    <h1>Welcome to online bartender!</h1>

    <form action="controller">
        <input type="hidden" name="command" value="register"/>
        <input type="text" name="name" autofocus required placeholder="Your Name"></br>
        <input type="text" name="email" required placeholder="Your Email"></br>
        <input type="text" name="password" required placeholder="Your Password"></br>
        <input type="submit" value="submit">
    </form>
<p>${reg_result}</p>
</body>
</html>
