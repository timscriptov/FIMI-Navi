package com.fimi.network.entity;

import java.util.Date;


public class UserDto extends BaseModel {
    private Date createTime;
    private String email;
    private String fimiId;
    private String name;
    private String nickName;
    private String phone;
    private String status;
    private String thirdId;
    private String userImgUrl;

    public String getFimiId() {
        return this.fimiId;
    }

    public void setFimiId(String fimiId) {
        this.fimiId = fimiId;
    }

    public String getThirdId() {
        return this.thirdId;
    }

    public void setThirdId(String thirdId) {
        this.thirdId = thirdId;
    }

    public String getNickName() {
        return this.nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserImgUrl() {
        return this.userImgUrl;
    }

    public void setUserImgUrl(String userImgUrl) {
        this.userImgUrl = userImgUrl;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String toString() {
        return "UserDto{fimiId='" + this.fimiId + "'" + ", thirdId='" + this.thirdId + "'" + ", nickName='" + this.nickName + "'" + ", name='" + this.name + "'" + ", email='" + this.email + "'" + ", phone='" + this.phone + "'" + ", userImgUrl='" + this.userImgUrl + "'" + ", status='" + this.status + "'" + ", createTime=" + this.createTime + '}';
    }
}
