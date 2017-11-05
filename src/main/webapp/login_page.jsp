<%--
  Created by IntelliJ IDEA.
  User: julia
  Date: 5/11/2017
  Time: 12:52 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
</head>
<body>

    <form action="/login_validation" method="post">
        <label for="username">Username</label>
        <input id="username" name="username" type="text" placeholder="Username">

        <label for="email">Email</label>
        <input id="email" name="email" type="email" placeholder="Email">

        <label for="password"></label>
        <input id="password" name="password" type="password" placeholder="Password">

        <button type="submit">Login</button>

    </form>

</body>
</html>
