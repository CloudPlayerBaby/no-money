package com.yuriyuri.dao;

import com.yuriyuri.pojo.User;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
    /**
     * 账号密码查询是否存在用户
     *
     * @param conn
     * @param user
     * @return
     * @throws SQLException
     */
    public static boolean select(Connection conn, User user) throws SQLException {
        //简单做个非空判断，防止空指针异常
        if (user == null) {
            return false;
        }
        String sql = "select * from user where username=? and password=?;";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());

            ResultSet res = pstmt.executeQuery();

            return res.next();
        }
    }

    /**
     * 添加用户
     *
     * @param conn
     * @param user
     * @throws SQLException
     */
    public static void add(Connection conn, User user) throws SQLException {
        if (user == null) {
            return;
        }

        String sql = "insert into user(username,password) values(?,?);";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());

            pstmt.executeUpdate();
        }
    }

    /**
     * 添加boss用户的方法，逻辑与添加小弟的一致
     *
     * @param conn
     * @param user
     * @throws SQLException
     */
    public static void addBoss(Connection conn, User user) throws SQLException {
        if (user == null) {
            return;
        }

        String sql = "insert into user(username,password,identity) values(?,?,?);";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, "boss");

            pstmt.executeUpdate();
        }
    }

    /**
     * 根据用户名查询是否存在用户
     *
     * @param conn
     * @param username
     * @return
     * @throws SQLException
     */
    public static boolean selectByUsername(Connection conn, String username) throws SQLException {
        String sql = "select * from user where username=?;";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);

            ResultSet res = pstmt.executeQuery();
            return res.next();
        }
    }

    /**
     * 通过用户名查询用户信息
     *
     * @param conn
     * @param username
     * @return
     * @throws SQLException
     */
    public static User selectInfo(Connection conn, String username) throws SQLException {
        String sql = "select * from user where username=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet res = pstmt.executeQuery();
            if (res.next()) {
                //如果存在用户
                User user = new User();
                user.setId(res.getInt("id"));
                user.setUsername(res.getString("username"));
                user.setPassword(res.getString("password"));
                user.setBalance(res.getBigDecimal("balance"));
                user.setIdentity(res.getString("identity"));
                user.setBalanceRegular(res.getBigDecimal("balance_regular"));
                user.setBalanceDemand(res.getBigDecimal("balance_demand"));
                return user;
            }
            //不存在用户则返回null
            else return null;
        }


    }

    /**
     * 根据id查询姓名
     *
     * @param conn
     * @param id
     * @return
     * @throws SQLException
     */
    public static String selectUsername(Connection conn, int id) throws SQLException {
        String sql = "select username from user where id=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet res = pstmt.executeQuery();
            String username = "";
            if (res.next()) {
                username = res.getString("username");
            }
            return username;
        }
    }

    /**
     * 修改用户信息
     *
     * @param conn
     * @param user
     * @throws SQLException
     */
    public static void modify(Connection conn, User user) throws SQLException {
        if (user == null) {
            return;
        }

        String sql = "update user set username=?,password=? where id=?;";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setInt(3, user.getId());

            pstmt.executeUpdate();
        }
    }

    /**
     * 查询所有人
     * @param conn
     * @return
     * @throws SQLException
     */
    public static List<User> selectAll(Connection conn) throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "select * from user";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet res = pstmt.executeQuery();
            while (res.next()){
                int id = res.getInt("id");
                String username = res.getString("username");
                String password = res.getString("password");
                BigDecimal balance = res.getBigDecimal("balance");
                String identity = res.getString("identity");
                BigDecimal balanceRegular = res.getBigDecimal("balance_regular");
                BigDecimal balanceDemand = res.getBigDecimal("balance_demand");
                User user = new User(id,username,password,balance,identity,balanceRegular,balanceDemand);
                users.add(user);
            }
        }
        return users;
    }
}
