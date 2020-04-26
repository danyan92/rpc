package com.ch.rpcserver.service;

import com.ch.rpcserver.model.Student;

/**
 * @Description: TODO
 * @Author chenhao
 * @Date 2020/4/22
 **/
public interface Service1 {
    String test1();

    String test2(String id);

    String test3(Student student);

    String test4(String id,Student student);
}
