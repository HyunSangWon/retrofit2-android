package com.qr.app.vo;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ObjListVO {
    @SerializedName("status")
    public boolean status;
    @SerializedName("data")
    public List<ObjListVO.Data> data = new ArrayList<>();

    public class Data {
        @SerializedName("objNo")
        public String objNo;
    }

    private String companySrl;
    public String getCompanySrl() {
        return companySrl;
    }
    public void setCompanySrl(String companySrl) {
        this.companySrl = companySrl;
    }
}
