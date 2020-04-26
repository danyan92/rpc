package com.ch.rpcclient;

import com.ch.rpcclient.service.RpcService;
import com.ch.rpccore.model.RpcRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import java.io.Serializable;
import java.util.UUID;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class})
public class RpcClientApplicationTests {

    @Autowired
    private RpcService rpcService;

    @Data
    @Accessors(chain = true)
    class RequestDemo{
        private String id;
        private String name;
    }

    @Test
    public void test1() throws Exception {
        RpcRequest rpcRequest=new RpcRequest();
        rpcRequest.setId(UUID.randomUUID().toString());
        rpcRequest.setClassName("com.ch.rpcserver.service.Service1");
        rpcRequest.setMethodName("test1");
        rpcService.send(rpcRequest);
        Thread.sleep(10000);
    }

    @Test
    public void test2() throws Exception {
        RpcRequest rpcRequest=new RpcRequest();
        rpcRequest.setId(UUID.randomUUID().toString());
        rpcRequest.setParameters(new Object[]{"2"});
        rpcRequest.setParameterTypes(new String[]{"java.lang.String"});
        rpcRequest.setClassName("com.ch.rpcserver.service.Service1");
        rpcRequest.setMethodName("test2");
        rpcService.send(rpcRequest);
        Thread.sleep(10000);
    }

    @Data
    @AllArgsConstructor
    class Student implements Serializable {
      private String id;
      private String name;
    }

    @Test
    public void test3() throws Exception {
        RpcRequest rpcRequest=new RpcRequest();
        rpcRequest.setId(UUID.randomUUID().toString());
        Student student=new Student("1","张三");
        rpcRequest.setParameters(new Object[]{student});
        rpcRequest.setParameterTypes(new String[]{"com.ch.rpcserver.model.Student"});
        rpcRequest.setClassName("com.ch.rpcserver.service.Service1");
        rpcRequest.setMethodName("test3");
        rpcService.send(rpcRequest);
        Thread.sleep(10000);
    }

    @Test
    public void test4() throws Exception {
        RpcRequest rpcRequest=new RpcRequest();
        rpcRequest.setId(UUID.randomUUID().toString());
        Student student=new Student("2","李四");
        rpcRequest.setParameters(new Object[]{"4",student});
        rpcRequest.setParameterTypes(new String[]{"java.lang.String","com.ch.rpcserver.model.Student"});
        rpcRequest.setClassName("com.ch.rpcserver.service.Service1");
        rpcRequest.setMethodName("test4");
        rpcService.send(rpcRequest);
        Thread.sleep(10000);
    }

    @Test
    public void test5() throws Exception {
        RpcRequest rpcRequest=new RpcRequest();
        rpcRequest.setId(UUID.randomUUID().toString());
        rpcRequest.setClassName("com.ch.rpcserver.service.Service1");
        rpcRequest.setMethodName("test1");
        rpcService.send(rpcRequest);
        rpcService.send(rpcRequest);
        rpcService.send(rpcRequest);
        Thread.sleep(10000);
    }
}
