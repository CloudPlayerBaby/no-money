package com.yuriyuri.service.impl;

import com.yuriyuri.dao.UserDao;
import com.yuriyuri.pojo.User;
import com.yuriyuri.service.UserService;
import com.yuriyuri.util.ConnectionUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class UserServiceImpl implements UserService {
    private UserDao userDao = new UserDao();
    /**
     * 账号密码查询是否存在用户
     * @param user
     * @return
     */
    @Override
    public boolean select(User user){
        try(Connection conn = ConnectionUtil.createConnection()){
            return userDao.select(conn,user);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 添加用户
     * @param user
     * @return
     */
    @Override
    public boolean add(User user){
        //不能同用户名，已经在dao层处理过空指针异常，应该不用再处理了
        try(Connection conn = ConnectionUtil.createConnection()){
            boolean flag = userDao.selectByUsername(conn,user.getUsername());
            if (flag) {
                //如果存在，则不添加
                return false;
            }
            userDao.add(conn,user);
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean addBoss(User user){
        //不能同用户名，已经在dao层处理过空指针异常，应该不用再处理了
        try(Connection conn = ConnectionUtil.createConnection()){
            boolean flag = userDao.selectByUsername(conn,user.getUsername());
            if (flag) {
                //如果存在，则不添加
                return false;
            }
            userDao.addBoss(conn,user);
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据用户名查询是否存在用户
     * @param username
     * @return
     */
    @Override
    public boolean selectByUsername(String username){
        try(Connection conn = ConnectionUtil.createConnection()){
            return userDao.selectByUsername(conn,username);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 根据用户名查询用户信息
     * @param username
     * @return
     */
    @Override
    public User selectInfo(String username){
        try(Connection conn = ConnectionUtil.createConnection()){
            return userDao.selectInfo(conn,username);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 根据id查询用户名
     * @param id
     * @return
     */
    @Override
    public String selectUsername(int id){
        try(Connection conn = ConnectionUtil.createConnection()){
            return userDao.selectUsername(conn,id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 修改用户信息
     * @param user
     * @return
     */
    @Override
    public boolean modify(User user) {
        try(Connection conn = ConnectionUtil.createConnection()){
            //应该不会存在是null的情况，不过还是写一下
            if(user==null){
                return false;
            }

            userDao.modify(conn,user);
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 查询所有人的信息
     * @return
     */
    @Override
    public List<User> selectAll() {
        try(Connection conn = ConnectionUtil.createConnection()){
            return userDao.selectAll(conn);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
