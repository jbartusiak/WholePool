package com.jba.utils;

public class RestRequestBuilder {
    private StringBuilder stringBuilder;

    private RestRequestBuilder(String baseUrl) {
        stringBuilder = new StringBuilder(baseUrl);
    }

    public static RestRequestBuilder builder(String baseUrl) {
        return new RestRequestBuilder(baseUrl);
    }

    public RestRequestBuilder addPathParam(String path) {
        stringBuilder.append("/");
        stringBuilder.append(path);
        return this;
    }

    public RestRequestBuilder addParam(String paramName, Object paramValue) {
        if (stringBuilder.indexOf("?") == -1) {
            stringBuilder.append("?");
            stringBuilder.append(paramName);
            stringBuilder.append("=");
            stringBuilder.append(paramValue.toString());
        } else {
            stringBuilder.append("&");
            stringBuilder.append(paramName);
            stringBuilder.append("=");
            stringBuilder.append(paramValue.toString());
        }
        return this;
    }

    public String build(){
        return stringBuilder.toString();
    }
}