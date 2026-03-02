package com.yuriyuri.dao;

import com.yuriyuri.pojo.Record;
import com.yuriyuri.pojo.User;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BankDao {
    /**
     * 创建流水
     *
     * @param conn
     * @param record
     * @throws SQLException
     */
    //需要的形参：conn、整个流水对象
    public static void transaction(Connection conn, Record record) throws SQLException {
        if (record == null) {
            return;
        }
        //获取时间
        Timestamp now = new Timestamp(System.currentTimeMillis());
        //流水标识由QG+时间戳组成
        String transfer_no = "QG" + System.currentTimeMillis();

        //需要自己填的：用户id，操作类型，操作金额，操作后的余额，目标用户id
        String sql = "insert into transaction_record values(null,?,?,?,?,?,?,?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, record.getUserId());
            pstmt.setString(2, record.getType());
            pstmt.setBigDecimal(3, record.getAmount());
            pstmt.setString(4, transfer_no);
            pstmt.setTimestamp(5, now);
            pstmt.setBigDecimal(6, record.getBalanceAfter());
            pstmt.setInt(7, record.getTargetUserId());

            pstmt.executeUpdate();
        }
    }

    /**
     * 存款
     *
     * @param conn
     * @param username
     * @param newBalance
     * @return
     * @throws SQLException
     */
    //存款，那就需要用户名和钱钱吧（应该还需要支付密码？但是做个简单系统就无所谓了吧）
    public static int deposit(Connection conn, String username, BigDecimal newBalance) throws SQLException {
        String sql = "update user set balance=? where username=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setBigDecimal(1, newBalance);
            pstmt.setString(2, username);
            return pstmt.executeUpdate();
        }
    }

    /**
     * 取款
     *
     * @param conn
     * @param username
     * @param newBalance
     * @return
     * @throws SQLException
     */
    public static int withdraw(Connection conn, String username, BigDecimal newBalance) throws SQLException {
        String sql = "update user set balance=? where username=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setBigDecimal(1, newBalance);
            pstmt.setString(2, username);
            return pstmt.executeUpdate();
        }
    }

    /**
     * 转账
     *
     * @param conn
     * @param username1
     * @param username2
     * @param newBalance1
     * @param newBalance2
     * @return
     * @throws SQLException
     */
    public static boolean transfer(Connection conn, String username1, String username2, BigDecimal newBalance1, BigDecimal newBalance2) throws SQLException {
        //用户1给用户2转账，相当于用户1取款，用户2存款
        int withdraw = withdraw(conn, username1, newBalance1);
        int deposit = deposit(conn, username2, newBalance2);
        //当两个事务都成功才返回true
        return withdraw > 0 && deposit > 0;
    }

    /**
     * 查看流水
     *
     * @param conn
     * @param userId
     * @return
     */
    public static List<Record> selectTransactions(Connection conn, int userId) {
        String sql = "select * from transaction_record where user_id=?";
        List<Record> records = new ArrayList<>();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            return getRecords(records, pstmt);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 查看所有人的流水账单
     *
     * @param conn
     * @return
     */
    public static List<Record> selectAllTransactions(Connection conn) {
        String sql = "select * from transaction_record";
        List<Record> records = new ArrayList<>();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            return getRecords(records, pstmt);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //以下是方法
    public static List<Record> getRecords(List<Record> records, PreparedStatement pstmt) throws SQLException {
        ResultSet res = pstmt.executeQuery();
        while (res.next()) {
            int id = res.getInt("id");
            int _userId = res.getInt("user_id");
            String type = res.getString("type");
            BigDecimal amount = res.getBigDecimal("amount");
            String transferNo = res.getString("transfer_no");
            Timestamp createTime = res.getTimestamp("create_time");
            BigDecimal balanceAfter = res.getBigDecimal("balance_after");
            int targetUserId = res.getInt("target_user_id");
            Record record = new Record(id, _userId, type, amount, transferNo, createTime, balanceAfter, targetUserId);
            records.add(record);
        }
        return records;
    }

}
