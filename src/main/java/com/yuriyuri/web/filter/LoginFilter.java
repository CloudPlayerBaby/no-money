package com.yuriyuri.web.filter;


import com.yuriyuri.util.CookieUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebFilter("/*")

public class LoginFilter implements Filter {


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        //如果是资源相关的，直接放行（我这里只有axios，所以就写.js）
        String uri = req.getRequestURI();
        if (uri.endsWith(".js")) {
            chain.doFilter(req, resp);
            return;
        }

        //如果是登陆注册相关页面，直接放行
        String[] urls = {"/login.html","/user/loginServlet","/register.html","/user/registerServlet","/user/captchaServlet"};

        //url内如果包含这些
        for (String url : urls) {
            if(uri.contains(url)){
                chain.doFilter(req,resp);
                return;
            }
        }

        //如果cookie里有登录信息，放行。不需要校验是否正确了，因为已经在登陆时校验过，如果不正确是不会有cookie的
        String username = CookieUtil.getCookie(req, "username");
        if (!username.isEmpty()) {
            //如果不是空的，就放行
            chain.doFilter(req, resp);
        } else {
            //如果没有cookie，说明没登陆，重定向到登陆界面（这一步为止已经完成拦截，接下来需要根据携带的msg=notlogin去前端给予提示信息）
            resp.sendRedirect(req.getContextPath() + "/login.html?msg=notlogin");
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }
}
