package com.gongdol.http;

import com.gongdol.http.vo.AckResponse;
import com.gongdol.http.vo.TokenRegResponse;

import retrofit.Callback;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by Gongdol ZZisoo on 15. 9. 10..
 */


public interface RequestInterface {

    /**
     * RequestInterface gcm token resisteration 3rd party Server with UserID
     * Callback implement by TokenRegResponse class
     *
     * @param userId
     * @param token
     * @param callback
     *
     */
    @POST("/gcm/registeration/toekn/{userId}/{token}")
    void GcmTokenReg(
            @Path("userId") String userId,
            @Path("token") String token,
            Callback<TokenRegResponse> callback
    );

    @POST("/gcm/{userId}/push/ack")
    void GcmAck(
            @Path("userId") String userId,
            Callback<AckResponse> callback
    );

}