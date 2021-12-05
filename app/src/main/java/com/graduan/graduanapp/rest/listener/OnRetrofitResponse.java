package com.graduan.graduanapp.rest.listener;

import com.graduan.graduanapp.model.ErrorResponse;

import okhttp3.ResponseBody;

public interface OnRetrofitResponse<T> {

    void onResponse(T response);
    void onFailureHeader(int responseCode, ResponseBody errorBody);
    void onFailure(int status, ErrorResponse errorResponse);
}
