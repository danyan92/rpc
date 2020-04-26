package com.ch.rpcserver.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description: TODO
 * @Author chenhao
 * @Date 2020/4/24
 **/
@Data
public class Student implements Serializable {
    private String name;
    private String id;
}
