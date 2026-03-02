package com.yuriyuri.pojo;

import java.math.BigDecimal;

public class User {
    private int id;
    private String username;
    private String password;
    private BigDecimal balance;
    private String identity;
    private BigDecimal balanceRegular;
    private BigDecimal balanceDemand;

    private String captcha;

    public User() {
    }

    public User(int id, String username, String password, BigDecimal balance, String identity, BigDecimal balanceRegular, BigDecimal balanceDemand) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.balance = balance;
        this.identity = identity;
        this.balanceRegular = balanceRegular;
        this.balanceDemand = balanceDemand;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public BigDecimal getBalanceRegular() {
        return balanceRegular;
    }

    public void setBalanceRegular(BigDecimal balanceRegular) {
        this.balanceRegular = balanceRegular;
    }

    public BigDecimal getBalanceDemand() {
        return balanceDemand;
    }

    public void setBalanceDemand(BigDecimal balanceDemand) {
        this.balanceDemand = balanceDemand;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", balance=" + balance +
                ", identity='" + identity + '\'' +
                ", balanceRegular=" + balanceRegular +
                ", balanceDemand=" + balanceDemand +
                ", captcha='" + captcha + '\'' +
                '}';
    }
}
