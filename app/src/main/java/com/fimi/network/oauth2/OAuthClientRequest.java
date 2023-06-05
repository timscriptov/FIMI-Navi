package com.fimi.network.oauth2;

/* loaded from: classes.dex */
public class OAuthClientRequest {
    private String mClientId;
    private String mClientSecret;
    private String mCode;
    private String mGrantType;
    private String mRedirectURI;
    private String mResponseType;

    public String getCode() {
        return this.mCode;
    }

    public void setCode(String code) {
        this.mCode = code;
    }

    public String getClientId() {
        return this.mClientId;
    }

    public void setClientId(String clientId) {
        this.mClientId = clientId;
    }

    public String getResponseType() {
        return this.mResponseType;
    }

    public void setResponseType(String responseType) {
        this.mResponseType = responseType;
    }

    public String getRedirectURI() {
        return this.mRedirectURI;
    }

    public void setRedirectURI(String redirectURI) {
        this.mRedirectURI = redirectURI;
    }

    public String getClientSecret() {
        return this.mClientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.mClientSecret = clientSecret;
    }

    public String getGrantType() {
        return this.mGrantType;
    }

    public void setGrantType(String grantType) {
        this.mGrantType = grantType;
    }
}
