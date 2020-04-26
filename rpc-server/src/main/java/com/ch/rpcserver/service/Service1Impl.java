package com.ch.rpcserver.service;

import com.alibaba.fastjson.JSON;
import com.ch.rpccore.annotation.RpcService;
import com.ch.rpcserver.model.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Description: TODO
 * @Author chenhao
 * @Date 2020/4/22
 **/
@RpcService
@Service
@Slf4j
public class Service1Impl implements Service1 {

    @Override
    public String test1() {
        log.info("执行test1");
        return "t1";
    }

    @Override
    public String test2(String id) {
        log.info("执行test2");
        log.info("参数id="+id);
        return "t2";
    }

    @Override
    public String test3(Student student) {
        log.info("执行test3");
        log.info("参数student="+ JSON.toJSONString(student));
        return "t3";
    }

    @Override
    public String test4(String id, Student student) {
        log.info("执行test4");
        log.info("参数id="+id+"student="+ JSON.toJSONString(student));
        return "t4";
    }
}
