package com.yuriyuri.web.servlet;

import com.alibaba.fastjson.JSON;
import com.yuriyuri.pojo.User;
import com.yuriyuri.service.UserService;
import com.yuriyuri.service.impl.UserServiceImpl;
import com.yuriyuri.util.CaptchaUtil;
import com.yuriyuri.util.CookieUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/user/*")
public class UserServlet extends BaseServlet {
    private UserService userService = new UserServiceImpl();

    /**
     * 注册
     *
     * @param req
     * @param resp
     * @throws IOException
     */
    public void registerServlet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/plain;charset=UTF-8");

        String params = req.getReader().readLine();
        User user = JSON.parseObject(params, User.class);

        String inputCaptcha = user.getCaptcha();
        String rightCaptcha = (String) req.getSession().getAttribute("rightCaptcha");

        System.out.println(inputCaptcha);
        System.out.println(rightCaptcha);

        //不填写验证码
        if (inputCaptcha == null) {
            resp.getWriter().write("noCaptcha");
            return;
        }

        //验证码错误
        if (!rightCaptcha.equalsIgnoreCase(inputCaptcha)) {
            resp.getWriter().write("captchaFalse");
            return;
        }
        //add函数里已经进行了存在判断，不需要再进行
        String identity = user.getIdentity();
        //如果是233，说明是牢大，就加上牢大身份
        if("233".equals(identity)){
            user.setIdentity("boss");
            //老大注册
            boolean flag = userService.addBoss(user);

            if (flag) {
                resp.getWriter().write("success");
            } else {
                resp.getWriter().write("failed");
            }
            return;
        }

        //小弟注册
        boolean flag = userService.add(user);

        if (flag) {
            resp.getWriter().write("success");
        } else {
            resp.getWriter().write("failed");
        }
    }

    /**
     * 登录
     *
     * @param req
     * @param resp
     * @throws IOException
     */
    public void loginServlet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String params = req.getReader().readLine();

        User user = JSON.parseObject(params, User.class);

        boolean flag = userService.select(user);

        if (flag) {
            resp.getWriter().write("success");
            //成功了，就要把cookie传过去
            CookieUtil.addCookie(resp, user.getUsername());
        } else {
            resp.getWriter().write("failed");
        }
    }

    /**
     * 验证码
     *
     * @param req
     * @param resp
     * @throws IOException
     */
    public void captchaServlet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //生成正确的验证码
        String rightCaptcha = CaptchaUtil.createCaptcha(6);
        HttpSession session = req.getSession();
        session.setAttribute("rightCaptcha", rightCaptcha);

        resp.setContentType("text/plain;charset=UTF-8");
        resp.getWriter().write(rightCaptcha);
    }

    /**
     * 修改用户信息
     *
     * @param req
     * @param resp
     * @throws IOException
     */
    public void modifyServlet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //从前端获取修改后的username和password
        String params = req.getReader().readLine();
        User userInfo = JSON.parseObject(params, User.class);

        String username = userInfo.getUsername();
        String password = userInfo.getPassword();

        //从cookie获取id
        String id = CookieUtil.getCookie(req, "id");

        //封装user
        User user = new User();
        user.setId(Integer.parseInt(id));

        user.setUsername(username);
        user.setPassword(password);

        //新的user可以是自己的名字，但是如果不是，就进行校验
        String existed = CookieUtil.getCookie(req, "username");
        if (!username.equals(existed)) {
            //如果该用户名已存在，直接返回
            if (userService.selectByUsername(username)) {
                resp.getWriter().write("exist");
                return;
            }
        }


        boolean flag = userService.modify(user);
        if (flag) {
            //更新cookie
            CookieUtil.addCookie(resp, username);
            //发送信息
            resp.getWriter().write("success");
        } else {
            resp.getWriter().write("failed");
        }
    }

    public void checkAllServlet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<User> users = userService.selectAll();
        String usersJson = JSON.toJSONString(users);

        resp.setContentType("application/json;charset=utf-8");
        resp.getWriter().write(usersJson);
    }

    /**
     * 理财相关使用
     * @param req
     * @param resp
     * @throws IOException
     */
    //即时发送，即时刷新的方法
    public void infoServlet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String username = CookieUtil.getCookie(req, "username");
        User user = userService.selectInfo(username);
        resp.setContentType("application/json");
        resp.getWriter().write(JSON.toJSONString(user));
    }
}
