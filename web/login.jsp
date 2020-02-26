<%--
  Created by IntelliJ IDEA.
  User: lifubao
  Date: 2020/2/21
  Time: 12:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>登录页面</title>
</head>
<body>
<form action="/login.do" method="post">
    <input type="text" name="username" value=""><br><br>
    <input type="checkbox" name="isAutoLogin">
    <input type="submit" value="登录">
</form>
</body>
</html>
