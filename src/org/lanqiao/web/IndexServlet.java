package org.lanqiao.web;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

//显示用户最后访问改应用的时间
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
