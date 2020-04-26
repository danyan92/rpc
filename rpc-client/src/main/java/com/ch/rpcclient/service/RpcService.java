package com.ch.rpcclient.service;

import com.ch.rpccore.model.RpcRequest;

/**
 * @Description: TODO
 * @Author chenhao
 * @Date 2020/4/23
 **/
public interface RpcService<T> {
    void send(RpcRequest request);
}
