package com.yuriyuri.service;

import com.yuriyuri.pojo.User;

import java.util.List;

public interface UserService {
    boolean select(User user);
    boolean add(User user);
    boolean addBoss(User user);
    boolean selectByUsername(String username);
    User selectInfo(String username);
    String selectUsername(int id);
    boolean modify(User user);
    List<User> selectAll();
}
