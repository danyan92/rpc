package com.ch.rpccore.parser;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

/**
 * @Description: 解析
 * @Author chenhao
 * @Date 2020/4/21
 **/
@Slf4j
public class RpcServiceBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

    private Class<?> clazz;

    public RpcServiceBeanDefinitionParser(Class clazz){
        this.clazz = clazz;
    }

    @Override
    protected Class getBeanClass(Element element) {
        return clazz;
    }


    @Override
    protected void doParse(Element element, BeanDefinitionBuilder bean) {
        bean.addPropertyValue("ref", StringUtils.isEmpty(element.getAttribute("ref"))?"1":element.getAttribute("ref"));
        bean.addPropertyValue("interfaceClass", element.getAttribute("interfaceClass"));
        bean.addPropertyValue("version", element.getAttribute("version"));
    }

}
