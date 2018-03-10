<%--
  Created by IntelliJ IDEA.
  User: asuspc
  Date: 2018/3/9
  Time: 20:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form action="/registerServlet" method="POST">
    username:<input name="username" type="text"/><br>
    password:<input name="password" type="text"/><br>
    <input type="submit" value="提交" />

</form>
</body>
</html>
