<%--
  Created by IntelliJ IDEA.
  User: lifubao
  Date: 2020/2/14
  Time: 15:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <%
//    获取所有cookie对象
    Cookie[] cookies = request.getCookies();
    if (cookies != null && cookies.length > 0){
        for (Cookie cookie : cookies){
            String name = cookie.getName();
            String value;
            //寻找关注的cookie信息
            if (name.equals("username")){
                value = cookie.getValue();

    %>
    <h1><%=name%></h1>---<h1><%=value%></h1>
    <%
            }
        }
    }
    %>
</body>
</html>
