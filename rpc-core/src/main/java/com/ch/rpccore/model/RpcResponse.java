package com.ch.rpccore.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Description: TODO
 * @Author chenhao
 * @Date 2020/4/23
 **/
@Data
@Accessors(chain = true)
public class RpcResponse {

    private String id;
    private Object data;
    private String result;
    private int code;

    public RpcResponse success (){
        this.setCode(200);
        this.setResult("success");
        return this;
    }

    public RpcResponse fail (){
        this.setCode(201);
        this.setResult("fail");
        return this;
    }
}
