<%--
  Created by IntelliJ IDEA.
  User: lifubao
  Date: 2020/2/14
  Time: 15:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>$Title$</title>
  </head>
  <body>
 index 页面
  <%
    String lastAccessTime = (String) request.getAttribute("lastAccessTime");
    if (lastAccessTime != null && !"".equals(lastAccessTime)) {
  %>
 <h1>您的最后一次访问时间为：<%=lastAccessTime%></h1>
  <%
    }else {

    }
  %>
  </body>
</html>
