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
public class RpcRequest {
    private String id;
    private String methodName;
    private String className;
    private String[] parameterTypes;
    private Object[] parameters;
}
