<%--
  Created by IntelliJ IDEA.
  User: asuspc
  Date: 2018/3/8
  Time: 19:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>index</title>
</head>
<body>
<form action="/loginServlet" method="POST">
    username:<input name="username" type="text"/><br>
    password:<input name="password" type="text"/><br>
    <input type="submit" value="提交" />
    <<a href="register.jsp">注册</a>
</form>
</body>
</html>