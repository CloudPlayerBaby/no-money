package com.yuriyuri.service.impl;

import com.yuriyuri.dao.BankDao;
import com.yuriyuri.pojo.Record;
import com.yuriyuri.pojo.User;
import com.yuriyuri.service.BankService;
import com.yuriyuri.service.UserService;
import com.yuriyuri.util.ConnectionUtil;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class BankServiceImpl implements BankService {
    private UserService userService = new UserServiceImpl();
    private BankDao bankDao = new BankDao();

    /**
     * 创建流水
     * @param record
     */
    @Override
    public void transaction(Record record) {
        try(Connection conn = ConnectionUtil.createConnection()){
            bankDao.transaction(conn,record);
        } catch (SQLException e) {
            throw new RuntimeException("创建流水记录失败",e);
        }
    }

    /**
     * 存款
     * @param username
     * @param amount
     * @return
     */
    @Override
    public boolean deposit(String username, BigDecimal amount) {
        try (Connection conn = ConnectionUtil.createConnection()) {
            conn.setAutoCommit(false);
            //查询原来的余额
            User userInfo = userService.selectInfo(username);
            BigDecimal balance = userInfo.getBalance();
            //更新余额
            BigDecimal newBalance = balance.add(amount);
            int rows = bankDao.deposit(conn, username, newBalance);
            if (rows > 0) {
                conn.commit();
                return true;
            } else {
                conn.rollback();
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException("存款失败", e);
        }
    }

    /**
     * 取款
     * @param username
     * @param amount
     * @return
     */
    @Override
    public boolean withdraw(String username, BigDecimal amount) {
        try (Connection conn = ConnectionUtil.createConnection()) {
            conn.setAutoCommit(false);
            //查询原来的余额
            User userInfo = userService.selectInfo(username);
            BigDecimal balance = userInfo.getBalance();
            //更新余额
            BigDecimal newBalance = balance.subtract(amount);
            int rows = bankDao.withdraw(conn, username, newBalance);
            if (rows > 0) {
                conn.commit();
                return true;
            } else {
                conn.rollback();
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException("取款失败", e);
        }
    }

    /**
     * 转账
     * @param username1
     * @param username2
     * @param amount
     * @return
     */
    @Override
    public boolean transfer(String username1, String username2, BigDecimal amount) {
        try(Connection conn = ConnectionUtil.createConnection()){
            conn.setAutoCommit(false);
            //查询原来的余额
            User userInfo1 = userService.selectInfo(username1);
            BigDecimal balance1 = userInfo1.getBalance();
            //用户1向用户2转账，1是减少，2是增加
            BigDecimal newBalance1 = balance1.subtract(amount);
            
            User userInfo2 = userService.selectInfo(username2);
            BigDecimal balance2 = userInfo2.getBalance();
            //用户1向用户2转账，1是减少，2是增加
            BigDecimal newBalance2 = balance2.add(amount);

            if(bankDao.transfer(conn, username1, username2, newBalance1, newBalance2)){
                conn.commit();
                return true;
            }else {
                conn.rollback();
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException("转账失败",e);
        }
    }

    /**
     * 查看流水
     * @param userId
     * @return
     */
    @Override
    public List<Record> selectTransactions(int userId) {
        try(Connection conn = ConnectionUtil.createConnection()){
            return bankDao.selectTransactions(conn,userId);
        } catch (SQLException e) {
            throw new RuntimeException("查询错误",e);
        }
    }

    /**
     * 查看所有人的流水
     * @return
     */
    @Override
    public List<Record> selectAllTransactions() {
        try (Connection conn = ConnectionUtil.createConnection()) {
            return bankDao.selectAllTransactions(conn);
        } catch (SQLException e) {
            throw new RuntimeException("查询错误", e);
        }
    }

}
