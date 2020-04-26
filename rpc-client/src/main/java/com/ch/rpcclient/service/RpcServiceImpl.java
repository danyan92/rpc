package com.ch.rpcclient.service;

import com.ch.rpcclient.Client;
import com.ch.rpccore.client.NettyClient;
import com.ch.rpccore.model.RpcRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description: TODO
 * @Author chenhao
 * @Date 2020/4/23
 **/
@Service
public class RpcServiceImpl implements RpcService{

    @Autowired
    private NettyClient client;

    @Override
    public void send(RpcRequest request) {
        client.send(request);
    }
}
