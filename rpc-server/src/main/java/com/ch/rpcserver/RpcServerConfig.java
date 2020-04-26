package com.ch.rpcserver;

import com.ch.rpccore.server.NettyServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: TODO
 * @Author chenhao
 * @Date 2020/4/26
 **/
@Configuration
public class RpcServerConfig {

    @Bean
    public NettyServer nettyServer(@Value("${rpc.server.host}")String host,
                                   @Value("${rpc.server.port}")int port){
        NettyServer server=new NettyServer();
        server.setHost(host);
        server.setPort(port);
        return server;
    }
}
