package com.zzisoo.gcmsenderapp;

import org.json.JSONObject;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by Gongdol ZZisoo on 15. 9. 10..
 */


public interface GoogleApiInterface {

    @POST("/gcm/send")
    void GcmSend( @Body GcmData data,
            Callback<JSONObject> callback
    );

}