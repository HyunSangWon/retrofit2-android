package com.qr.app.api;

import com.qr.app.vo.ObjListVO;
import com.qr.app.vo.QrVO;
import com.qr.app.vo.ListVO;
import com.qr.app.vo.UserVO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface QrAPI {

    @POST("/api/user/login")
    Call<UserVO> login(@Body UserVO.Data data);
    //신규 업체 리스트 불러오기
    @POST("/api/obj/new/list")
    Call<ListVO> doPostNewCompanyList(@Body UserVO.Data data);
    //해당 업체 신규 담보물 번호 조회
    @POST("/api/obj/new/list/objs")
    Call<ObjListVO> doPostFetNewObjs(@Body ObjListVO objListVO);
    //QR코드값 전송
    @POST("/api/obj/qr/decode")
    Call<QrVO> doPostQrDecode(@Body QrVO qrVO);
}
