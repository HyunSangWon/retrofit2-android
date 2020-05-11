package com.qr.app.vo;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ListVO {

    @SerializedName("status")
    public boolean status;
    @SerializedName("data")
    public List<ListVO.Data> data = new ArrayList<>();

    public class Data{
        @SerializedName("companySrl")
        public String companySrl;
        @SerializedName("count")
        public String cnt;
        @SerializedName("addr")
        public String addr;
        @SerializedName("name")
        public String name;
        @SerializedName("nowObjStatus")
        public String nowObjStatus;
        @SerializedName("auth")
        public String auth;
    }
}