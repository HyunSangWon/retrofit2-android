package com.qr.app.vo;

import com.google.gson.annotations.SerializedName;

public class UserVO {

    @SerializedName("status")
    public boolean status;
    @SerializedName("data")
    public Data data;

    public class Data{

        public Data(String clientNo){
            this.clientNo = clientNo;
        }
        public Data(String id,String password){
            this.id = id;
            this.password = password;
        }
        @SerializedName("clientNo")
        public String clientNo;
        @SerializedName("id")
        public String id;
        @SerializedName("password")
        public String password;
        @SerializedName("name")
        public String name;
        @SerializedName("manager")
        public String manager;
        @SerializedName("phone")
        public String phone;
        @SerializedName("addr")
        public String addr;
        @SerializedName("mail")
        public String mail;
        @SerializedName("auth")
        public String auth;

    }
}
