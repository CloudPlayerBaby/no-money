package com.yuriyuri.service;

import com.yuriyuri.pojo.Record;

import java.math.BigDecimal;
import java.util.List;

public interface BankService {
    boolean deposit(String username, BigDecimal amount);
    boolean withdraw(String username,BigDecimal amount);
    boolean transfer(String username1, String username2, BigDecimal amount);
    void transaction(Record record);
    List<Record> selectTransactions(int userId);
    List<Record> selectAllTransactions();
//    boolean manageRegular(int id,BigDecimal amount);
//    boolean manageDemand(int id,BigDecimal amount);
//    void manageAllRegular();
}
