package com.oauth2.client.service.model;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class TokenModel {
    @SerializedName("access_token")
    private String accessToken;
    @SerializedName("token_type")
    private String tokenType;
    @SerializedName("refresh_token")
    private String refreshToken;
    @SerializedName("expires_in")
    private int expiresIn;
    private String scope;
}
