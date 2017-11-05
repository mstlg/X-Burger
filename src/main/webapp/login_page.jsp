<%--
  Created by IntelliJ IDEA.
  User: julia
  Date: 5/11/2017
  Time: 12:52 PM
--%>
<%--Setup .jsp file with jstl tag library--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Login</title>
</head>
<body>

    <c:if test="${loginStatus == 'active'}">
        <c:redirect url="/"/>
    </c:if>

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
