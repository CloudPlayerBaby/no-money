package com.yuriyuri.pojo;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Record {
    private int id;
    private int userId;
    private String type;
    private BigDecimal amount;
    private String transferNo;
    private Timestamp time;
    private BigDecimal balanceAfter;
    private int targetUserId;

    //额外携带
    private String targetUsername;
    private String username;

    public Record() {
    }

    //取款存款可用的有参构造
    public Record(int id, int userId, String type, BigDecimal amount, String transferNo, Timestamp time, BigDecimal balanceAfter, int targetUserId) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.amount = amount;
        this.transferNo = transferNo;
        this.time = time;
        this.balanceAfter = balanceAfter;
        this.targetUserId = targetUserId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getTransferNo() {
        return transferNo;
    }

    public void setTransferNo(String transferNo) {
        this.transferNo = transferNo;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public BigDecimal getBalanceAfter() {
        return balanceAfter;
    }

    public void setBalanceAfter(BigDecimal balanceAfter) {
        this.balanceAfter = balanceAfter;
    }

    public int getTargetUserId() {
        return targetUserId;
    }

    public void setTargetUserId(int targetUserId) {
        this.targetUserId = targetUserId;
    }

    public String getTargetUsername() {
        return targetUsername;
    }

    public void setTargetUsername(String targetUsername) {
        this.targetUsername = targetUsername;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "Record{" +
                "id=" + id +
                ", userId=" + userId +
                ", type='" + type + '\'' +
                ", amount=" + amount +
                ", transferNo='" + transferNo + '\'' +
                ", time=" + time +
                ", balanceAfter=" + balanceAfter +
                ", targetUserId=" + targetUserId +
                ", targetUsername='" + targetUsername + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
