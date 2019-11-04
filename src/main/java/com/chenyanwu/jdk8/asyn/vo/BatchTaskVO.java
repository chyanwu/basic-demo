package com.chenyanwu.jdk8.asyn.vo;

import java.util.List;

/**
 * @Auther: chenyanwu
 * @Date: 2019/9/24 10:32
 * @Description:
 * @Version 1.0
 */
public class BatchTaskVO {
    private String batchno;
    private int grade;
    private List<User> userList;
    private boolean success;
    private String message;

    public String getBatchno() {
        return batchno;
    }

    public void setBatchno(String batchno) {
        this.batchno = batchno;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
