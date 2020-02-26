# 会话与状态管理                                                                                                                                                                                                                                                                                                      

- WEB应用中的会话是指一个客户端浏览器与WEB服务器之间连续发生的一系列请求和响应过程。
- WEB应用的会话状态是指WEB服务器与浏览器在会话过程中产生的状态信息，借助会话状态，WEB服务器能够把属于同一会话中的一系列的请求和响应过程关联起来。

在 Servlet 规范中，常用以下两种机制完成会话跟踪

- Cookie
- Session  

# cookie

- Cookie是在浏览器访问WEB服务器的某个资源时，由WEB服务器在HTTP响应消息头中附带传送给浏览器的一个小文本文件
- 一旦WEB浏览器保存了某个Cookie，那么它在以后每次访问该WEB服务器时，都会在HTTP请求头中将这个Cookie回传给WEB服务器。
- 底层的实现原理： WEB服务器通过在HTTP响应消息中**增加Set-Cookie响应头字段**将Cookie信息发送给浏览器，浏览器则通过在HTTP请求消息中**增加Cookie请求头字段将Cookie回传给WEB服务器**。
- 一个Cookie只能标识一种信息，它至少含有一个标识该信息的名称（NAME）和设置值（VALUE）。
- 一个WEB站点可以给一个WEB浏览器发送多个Cookie，一个WEB浏览器也可以存储多个WEB站点提供的Cookie。
- 浏览器一般只允许存放300个Cookie，每个站点最多存放20个Cookie，每个Cookie的大小限制为4KB。

# Cookie的使用：

```java
@Override
protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
  //创建一个cookie  并保存信息
    Cookie cookie = new Cookie("username","admin");
    //将cookie信息响应给客户端
    resp.addCookie(cookie);
    req.getRequestDispatcher("/success.jsp").forward(req,resp);
}
```

![img](E:\YouDaoYun\m15234512314@163.com\a7e9ec60b6dc444691c6341ebddad950\clipboard.png)

# cookie的获取

```jsp
	<%--获取所有的cookie对象 --%>
    <%
         Cookie[]  cookies =   request.getCookies();
         for(Cookie cookie : cookies){
         String name =  cookie.getName();
         String value = cookie.getValue();
    %>
    <h1><%=name%></h1>--- <h1><%=value%></h1>
    <br/>
    <%
        }
    %>
```

![img](E:\YouDaoYun\m15234512314@163.com\83f519cd60ba477f842555f1d746a83e\clipboard.png)

JSSESIONID:是服务端产生 来标记一次会话的数据  会话ID 每一个会话值都是唯一的

存储我们自定义的信息

username= admin

在cookie中寻找自己关注的cookie

```java
Cookie[]  cookies =   request.getCookies();
for(Cookie cookie : cookies){
    String name =  cookie.getName();
    String value="";
    //寻找关注的cookie信息
    if(name.equals("username")){
        value = cookie.getValue();
    }
}
```

# Cookie的分类：

cookie分为会话cookie和持久cookie

setMaxAge()设置cookie的存储时间  

时间单位为秒

如果是-1 则表示永久存储  无限期

如果cookie没有设置maxAge 则为会话cookie 

```java
//存储时间为一天 
cookie.setMaxAge(60*60*24);
```

# Cookie实现自动登录

```java
@WebServlet("/login.do")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取用户名
        //如果用户名不为空 则进行用户名密码的校验 如果校验通过 则直接登录 并将登录信息保存到cookie
        //如果用户名为空 获取cookie中的数据 cookie中是否有登录所需信息 如果有 则直接校验 校验通过登录
        // 校验不通过则返回登录页面 让用户重新登录
        String username = req.getParameter("username");
        if (username != null && !username.equals("")){
            if ("admin".equals(username)){//校验通过
                //并将信息保存到cookie
                Cookie cookie = new Cookie("username",username);
                cookie.setMaxAge(60*60);
                resp.addCookie(cookie);
                resp.sendRedirect("/home.jsp");//重定向到主页
            }else{
                resp.sendRedirect("/login.jsp");//重定向到登录页面
            }
        }else {
            Cookie[] cookies = req.getCookies();
            if (cookies != null && cookies.length > 0) {
                for (Cookie cookie : cookies) {
                    String cookieName = cookie.getName();
                    if ("username".equals(cookieName)){
                        String cookieValue = cookie.getValue();
                        if ("admin".equals(cookieValue)){
                            resp.sendRedirect("/home.jsp");//重定向到主页
                        }else {
                            resp.sendRedirect("/login.jsp");//重定向到登录页面
                        }
                    }else {
                        resp.sendRedirect("/login.jsp");//重定向到登录页面
                    }
                }
            }else {
                resp.sendRedirect("/login.jsp");//重定向到登录页面
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
```

login.jsp

```jsp
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
```

success.jsp

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <%
	//获取所有cookie对象
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
```

# 显示用户最后访问该应用的时间

```java
@WebServlet("/index")
public class IndexServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Cookie[] cookies = req.getCookies();
        String lastAccessTime = "";
        if (cookies != null && cookies.length > 1){
            for (Cookie cookie : cookies) {
                String cookieName = cookie.getName();
                if ("lastAccessTime".equals(cookieName)) {
                    lastAccessTime = cookie.getValue();
                }
            }
        }
        //生成日期并格式化成字符串
        Date date = new Date();
        String dateStr = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss").format(date);//cookie字符串不能用空格
        //将生成的字符串写入cookie
        Cookie cookie1 = new Cookie("lastAccessTime",dateStr);
        //设置cookie寿命
        cookie1.setMaxAge(60*60);
        resp.addCookie(cookie1);

        req.setAttribute("lastAccessTime",lastAccessTime);
        req.getRequestDispatcher("/index.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
```

index.jsp

```jsp
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
```

# Cookie中存取中文

要想在cookie中存储中文，那么必须使用URLEncoder类里面的encode(String s, String enc)方法进行中文转码，例如：

```java
Cookie cookie = new Cookie("userName", URLEncoder.encode("北京", "UTF-8"));
response.addCookie(cookie);
```

在获取cookie中的中文数据时，再使用URLDecoder类里面的decode(String s, String enc)进行解码，例如：

```java
 URLDecoder.decode(cookies[i].getValue(), "UTF-8")
```



