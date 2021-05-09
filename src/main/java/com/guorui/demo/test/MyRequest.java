package com.guorui.demo.test;

import java.io.IOException;
import java.io.InputStream;

public class MyRequest {
    private String url;
    private String method;
    private String param;
    public MyRequest(InputStream inputStream) throws IOException {
        String httpRequest ="";
        byte[] httpRequestBytes =new byte[1024];
        int length =0;
        if((length=inputStream.read(httpRequestBytes)) >0){
            httpRequest=new String(httpRequestBytes,0,length);
        }
        String httpHead = httpRequest.split("\n")[0];
        System.out.println("myRequest httpHead length:" + httpHead.length());
        String a=httpHead.split("\\s")[1];
        method =httpHead.split("\\s")[0];
        url=a.split("\\?")[0];
        if (a.split("\\?").length > 1){
        param = a.split("\\?")[1];}
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getMethod() {
        return method;
    }
    public void setMethod(String method) {
        this.method = method;
    }
}