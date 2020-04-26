package com.ch.rpcserver;

import com.ch.rpccore.config.ServiceConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@SpringBootTest
class RpcServerApplicationTests {

    @Test
    void contextLoads() {
        ApplicationContext context = new ClassPathXmlApplicationContext("rpc-server.xml");
        ServiceConfig config = (ServiceConfig) context.getBean("serviceConfig");
        System.out.println(config.toString());
    }

}
