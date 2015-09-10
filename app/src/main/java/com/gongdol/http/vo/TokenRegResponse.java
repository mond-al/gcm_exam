package com.gongdol.http.vo;

/**
 * Created by yangjisoo on 15. 9. 10..
 */
public class TokenRegResponse {
    public int getResultCode() {
        return ResultCode;
    }

    public String getMsg() {
        return Msg;
    }

    private int ResultCode;
    private String Msg;
}
