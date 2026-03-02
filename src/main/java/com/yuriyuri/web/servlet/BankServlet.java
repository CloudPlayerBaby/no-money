package com.yuriyuri.web.servlet;

import com.alibaba.fastjson.JSON;
import com.yuriyuri.pojo.Record;
import com.yuriyuri.pojo.User;
import com.yuriyuri.service.BankService;
import com.yuriyuri.service.UserService;
import com.yuriyuri.service.impl.BankServiceImpl;
import com.yuriyuri.service.impl.UserServiceImpl;
import com.yuriyuri.util.CheckUtil;
import com.yuriyuri.util.CookieUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/bank/*")
public class BankServlet extends BaseServlet {
    private BankService bankService = new BankServiceImpl();
    private UserService userService = new UserServiceImpl();

    /**
     * 存款
     *
     * @param req
     * @param resp
     * @throws IOException
     */
    public void depositServlet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //先获取字符串，再转成BigDecimal类型
        String amountStr = req.getParameter("amount");
        BigDecimal amount = new BigDecimal(amountStr);

        String username = CookieUtil.getCookie(req, "username");
        String id = CookieUtil.getCookie(req, "id");

        boolean flag = bankService.deposit(username, amount);
        if (flag) {
            resp.getWriter().write("success");
            //插入流水记录，需要Record对象
            //需要自己填的：用户id，操作类型，操作金额，操作后的余额
            Record record = new Record();
            record.setUserId(Integer.parseInt(id));
            record.setType("存款");
            record.setAmount(amount);

            //这里已经操作完毕了，所以直接获取现在的余额就可以了
            User userInfo = userService.selectInfo(username);

            BigDecimal balance = userInfo.getBalance();
            record.setBalanceAfter(balance);

            //这里不需要目标用户id
            bankService.transaction(record);
            //更新cookie
            CookieUtil.addCookie(resp, username);
        } else {
            resp.getWriter().write("failed");
        }
    }

    /**
     * 取款
     *
     * @param req
     * @param resp
     * @throws IOException
     */
    public void withdrawServlet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //先获取字符串，再转成BigDecimal类型
        String amountStr = req.getParameter("amount");
        BigDecimal amount = new BigDecimal(amountStr);

        String username = CookieUtil.getCookie(req, "username");
        String id = CookieUtil.getCookie(req, "id");

        //如果余额校验不通过，直接返回
        if (!CheckUtil.checkMoney(username, amount)) {
            resp.getWriter().write("amount_err");
            return;
        }

        boolean flag = bankService.withdraw(username, amount);

        if (flag) {
            resp.getWriter().write("success");
            //插入流水记录，需要Record对象
            //需要自己填的：用户id，操作类型，操作金额，操作后的余额
            Record record = new Record();
            record.setUserId(Integer.parseInt(id));
            record.setType("取款");
            record.setAmount(amount);

            //这里已经操作完毕了，所以直接获取现在的余额就可以了
            User userInfo = userService.selectInfo(username);

            BigDecimal balance = userInfo.getBalance();
            record.setBalanceAfter(balance);

            //这里不需要目标用户id

            bankService.transaction(record);
            //更新cookie
            CookieUtil.addCookie(resp, username);
        } else {
            resp.getWriter().write("failed");
        }
    }

    /**
     * 转账
     *
     * @param req
     * @param resp
     * @throws IOException
     */
    public void transferServlet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //前端传来两个数据，一个是金币，另一个是需要转给的人（另一个用户，即已登录的用户由cookie获取）
        //从前端获取另一个用户和金额
        String username2 = req.getParameter("username2");

        if (username2 == null || username2.isEmpty()) {
            resp.getWriter().write("no_user");
            return;
        }

        User exist = userService.selectInfo(username2);

        if (exist == null) {
            resp.getWriter().write("no_user");
            return;
        }

        String amountStr = req.getParameter("amount");
        BigDecimal amount = new BigDecimal(amountStr);

        String username1 = CookieUtil.getCookie(req, "username");
        String id1 = CookieUtil.getCookie(req, "id");

        //检查一下用户1的余额够不够
        if (!CheckUtil.checkMoney(username1, amount)) {
            resp.getWriter().write("amount_err");
            return;
        }

        int userId1 = Integer.parseInt(id1);
        //通过username2获取user_id2
        User userInfo2 = userService.selectInfo(username2);
        int userId2 = userInfo2.getId();

        //不能给自己转账
        if (userId2 == userId1) {
            resp.getWriter().write("equal_err");
            return;
        }

        boolean flag = bankService.transfer(username1, username2, amount);
        if (flag) {
            resp.getWriter().write("success");
            //更新cookie
            CookieUtil.addCookie(resp, username1);
            Record record1 = createRecord(userId1, "转出", amount, username1, userId2);
            Record record2 = createRecord(userId2, "转入", amount, username2, userId1);

            //更新两条流水记录，一条是自己转出，一条是对方转入
            bankService.transaction(record1);
            bankService.transaction(record2);
        } else {
            resp.getWriter().write("failed");
        }
    }

    /**
     * 查看流水
     *
     * @param req
     * @param resp
     * @throws IOException
     */
    public void inspectServlet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //从cookie获取用户id
        String id = CookieUtil.getCookie(req, "id");

        List<Record> records = bankService.selectTransactions(Integer.parseInt(id));

        //如果是转入转出，加上目标用户字段
        for (Record record : records) {
            if ("转入".equals(record.getType()) || "转出".equals(record.getType())) {
                String targetUsername = userService.selectUsername(record.getTargetUserId());
                record.setTargetUsername(targetUsername);
            }
        }

        //转换成json提交给前端，指定格式便于显示
        String jsonString = JSON.toJSONStringWithDateFormat(records, "yyyy-MM-dd HH:mm:ss");

        resp.setContentType("text/html;charset=utf-8");
        resp.getWriter().write(jsonString);
    }

    /**
     * 查看所有的流水
     *
     * @param req
     * @param resp
     * @throws IOException
     */
    public void inspectAllServlet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<Record> records = bankService.selectAllTransactions();

        for (Record record : records) {
            //加上用户名
            String username = userService.selectUsername(record.getUserId());
            record.setUsername(username);

            //如果是转入转出，加上目标用户字段
            if ("转入".equals(record.getType()) || "转出".equals(record.getType())) {
                String targetUsername = userService.selectUsername(record.getTargetUserId());
                record.setTargetUsername(targetUsername);
            }
        }

        //转换成json提交给前端，指定格式便于显示
        String jsonString = JSON.toJSONStringWithDateFormat(records, "yyyy-MM-dd HH:mm:ss");

        resp.setContentType("text/html;charset=utf-8");
        resp.getWriter().write(jsonString);
    }


    /**
     * 登出
     *
     * @param req
     * @param resp
     * @throws IOException
     */
    public void exitServlet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        CookieUtil.deleteCookie(resp);
        resp.getWriter().write("success");
    }

    //以下是方法
    public static Record createRecord(int userId, String type, BigDecimal amount, String username, int targetUserId) {
        UserService us = new UserServiceImpl();
        //插入流水记录，需要Record对象
        //需要自己填的：用户id，操作类型，操作金额，操作后的余额，用户id
        Record record = new Record();
        record.setUserId(userId);
        record.setType(type);
        record.setAmount(amount);

        //这里已经操作完毕了，所以直接获取现在的余额就可以了
        User userInfo = us.selectInfo(username);

        BigDecimal balance = userInfo.getBalance();
        record.setBalanceAfter(balance);
        record.setTargetUserId(targetUserId);

        return record;
    }
}
