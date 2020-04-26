package com.ch.rpccore.handler;

;
import com.ch.rpccore.config.ServiceConfig;
import com.ch.rpccore.parser.RpcServiceBeanDefinitionParser;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * @Description: 命名空间处理
 * @Author chenhao
 * @Date 2020/4/21
 **/
public class RpcNamespaceHandler extends NamespaceHandlerSupport {
    @Override
    public void init() {
        this.registerBeanDefinitionParser("service", new RpcServiceBeanDefinitionParser(ServiceConfig.class));
    }
}
