package com.ch.rpcclient;

import com.ch.rpccore.client.NettyClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: TODO
 * @Author chenhao
 * @Date 2020/4/23
 **/
@Configuration
public class Client{

    @Bean
    NettyClient nettyClient(@Value("${rpc.server.host}")String host,
                            @Value("${rpc.server.port}")int port){
        NettyClient nettyClient=new NettyClient();
        nettyClient.setHost(host);
        nettyClient.setPort(port);
        return nettyClient;
    }
}
