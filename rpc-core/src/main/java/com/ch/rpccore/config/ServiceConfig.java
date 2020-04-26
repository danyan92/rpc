package com.ch.rpccore.config;

import lombok.Data;

/**
 * @Description: 服务配置
 * @Author chenhao
 * @Date 2020/4/21
 **/
@Data
public class ServiceConfig {
    /** 接口 */
    private String interfaceClass;

    /** 引用 */
    private String ref;

    /** 版本 */
    private String version;

}
