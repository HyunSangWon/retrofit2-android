package com.qr.app.vo;

import com.google.gson.annotations.SerializedName;

public class QrVO {
    @SerializedName("status")
    public boolean status;
    @SerializedName("data")
    public boolean data;
    @SerializedName("decode")
    public String decode;

    public QrVO(){}
    public QrVO(String decode){
        this.decode = decode;
    }
}