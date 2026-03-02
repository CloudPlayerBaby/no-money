package com.yuriyuri.util;

import com.yuriyuri.pojo.User;
import com.yuriyuri.service.UserService;
import com.yuriyuri.service.impl.UserServiceImpl;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;

public final class CookieUtil {
    private CookieUtil() {
        throw new AssertionError("no");
    }

    /**
     * 存cookie
     *
     * @param resp
     * @param username
     */
    //存cookie，三个形参为resp对象，用户名，存储时间
    public static void addCookie(HttpServletResponse resp, String username) {
        //存username
        setCookie(resp,"username",username);
        //根据username获取balance
        UserService userService = new UserServiceImpl();
        User userInfo = userService.selectInfo(username);

        BigDecimal balance = userInfo.getBalance();
        //存balance
        setCookie(resp,"balance", String.valueOf(balance));

        //存id和password和identity
        int id = userInfo.getId();
//        String password = userInfo.getPassword();
        String identity = userInfo.getIdentity();

        setCookie(resp,"id",String.valueOf(id));
//        setCookie(resp,"password",password);
        setCookie(resp,"identity",identity);

        //存理财方案金额
        BigDecimal balanceRegular = userInfo.getBalanceRegular();
        BigDecimal balanceDemand = userInfo.getBalanceDemand();

        setCookie(resp,"balanceRegular", String.valueOf(balanceRegular));
        setCookie(resp,"balanceDemand", String.valueOf(balanceDemand));
    }

    //三个形参分别代表resp对象，cookie的名字和值
    public static void setCookie(HttpServletResponse resp,String name,String value){
        Cookie cookie = new Cookie(name,value);
        cookie.setMaxAge(60*60*24*7);
        cookie.setPath("/");
        resp.addCookie(cookie);
    }

    /**
     * 获取cookie
     *
     * @param req
     * @param name
     * @return
     */
    public static String getCookie(HttpServletRequest req, String name) {
        Cookie[] cookies = req.getCookies();
        if (cookies == null) {
            return "";
        }

        String value = "";
        for (Cookie cookie : cookies) {
            if (name.equals(cookie.getName())) {
                value = cookie.getValue();
                break;
            }
        }
        return value;
    }

    /**
     * 删除cookie
     *
     * @param resp
     */
    public static void deleteCookie(HttpServletResponse resp) {
        String[] names = {"username", "balance", "id", "password", "identity","balanceRegular","balanceDemand"};
        for (String name : names) {
            Cookie cookie = new Cookie(name, null);
            cookie.setPath("/");
            cookie.setMaxAge(0);
            resp.addCookie(cookie);
        }
    }

}
