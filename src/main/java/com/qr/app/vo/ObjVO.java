package com.qr.app.vo;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ObjVO {

    @SerializedName("status")
    public boolean status;
    @SerializedName("data")
    public List<Data> data = new ArrayList<>();

    public class Data{
        @SerializedName("orderNo")
        private String orderNo; //주문번호
        @SerializedName("clientNo")
        private String clientNo; //업체,지사,본사 고유번호
        @SerializedName("companySrl")
        private String companySrl; //업체 번호
        @SerializedName("count")
        private String count;//수량 합
        @SerializedName("addr")
        private String addr;//업체 주소
        @SerializedName("name")
        private String name;//업체 이름
        @SerializedName("orderStatus")
        private String orderStatus; //주문 상태 (y = '배송완료' i='배송중' n='배송 전')
        @SerializedName("nowObjStatus")
        private String nowObjStatus;//현재 담보물 상태 (i='담보물 등록전' s='담보물 등록 검수필요' y='검수완료' w='검수대기' n='검수실패')
        @SerializedName("auth")
        private String auth; //권한 이름
    }
}
