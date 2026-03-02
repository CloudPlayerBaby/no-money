package com.yuriyuri.util;

import com.yuriyuri.pojo.User;
import com.yuriyuri.service.UserService;
import com.yuriyuri.service.impl.UserServiceImpl;

import java.math.BigDecimal;

public final class CheckUtil {
    private CheckUtil() {
        throw new AssertionError("no");
    }

    //对存钱取钱的校验，传username获取信息，再传需要存多少钱钱进行比较
    public static boolean checkMoney(String username, BigDecimal amount) {
        UserService userService = new UserServiceImpl();
        //根据用户名获取余额
        User userInfo = userService.selectInfo(username);
        BigDecimal balance = userInfo.getBalance();

        //余额不小于0，之后余额和需要存取多少金额进行比较
        if (balance.compareTo(amount) < 0) {
            //如果需要存取的金额大于余额，当然不能存取了
            return false;
        }

        return true;
    }

}
