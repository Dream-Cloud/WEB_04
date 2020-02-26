package org.lanqiao.web;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
//cookie实现自动登录
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
