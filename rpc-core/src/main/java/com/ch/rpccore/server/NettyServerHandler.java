package com.ch.rpccore.server;


import com.alibaba.fastjson.JSON;
import com.ch.rpccore.model.RpcRequest;
import com.ch.rpccore.model.RpcResponse;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Server的消息处理类Handler
 * @author chenhao
 */
@ChannelHandler.Sharable
@Slf4j
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    private final Map<String, Object> serviceMap;

    public NettyServerHandler(Map<String, Object> serviceMap) {
        this.serviceMap = serviceMap;
    }

    /**
     * 客户端去和服务端连接成功时触发
     * @param ctx
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx)   {
        log.info("客户端连接成功!"+ctx.channel().remoteAddress());
    }

    /**
     * 通知处理器最后的channelRead()是当前批处理中的最后一条消息时调用
     * @param ctx
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx)   {
        log.info("客户端断开连接!{}",ctx.channel().remoteAddress());
        ctx.channel().close();
    }

    /**
     * 接受client发送的消息
     * @param ctx
     * @param msg
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg){
        log.info("客户端消息---"+msg);
        RpcRequest request= JSON.parseObject(msg.toString(),RpcRequest.class);
        //do response
        log.info("RPC客户端请求接口:"+request.getClassName()+"   方法名:"+request.getMethodName());
        RpcResponse response = new RpcResponse();
        response.setId(request.getId());
        try {
            Object result = this.handler(request);
            response.setId(request.getId()).setData(result).success();
        } catch (Throwable e) {
            e.printStackTrace();
            response.setCode(202).fail();
            log.error("RPC Server handle request error",e);
        }
        ctx.writeAndFlush(response);
    }


    /**
     * 通过反射，执行本地方法
     * @param request
     * @return
     * @throws Throwable
     */
    private Object handler(RpcRequest request) throws Throwable{
        String className = request.getClassName();
        Object serviceBean = serviceMap.get(className);

        if (serviceBean!=null){
            Class<?> serviceClass = serviceBean.getClass();
            String methodName = request.getMethodName();
            Class<?>[] parameterTypes=null;
            if(request.getParameterTypes()!=null&&request.getParameterTypes().length>0){
                List<Class<?>> parameterTypeList=new ArrayList<>();
                for(String name:request.getParameterTypes()) {
                    parameterTypeList.add(Class.forName(name).newInstance().getClass());
                }
                parameterTypes=parameterTypeList.toArray(new Class<?>[parameterTypeList.size()]);
            }
            Object[] parameters = request.getParameters();
            Method method = serviceClass.getMethod(methodName,parameterTypes);
            method.setAccessible(true);
            return method.invoke(serviceBean, getParameters(parameterTypes,parameters));
        }else{
            throw new Exception("未找到服务接口,请检查配置!:"+className+"#"+request.getMethodName());
        }
    }

    /**
     * 获取参数列表
     * @param parameterTypes
     * @param parameters
     * @return
     */
    private Object[] getParameters(Class<?>[] parameterTypes,Object[] parameters){
        if (parameters==null || parameters.length==0){
            return parameters;
        }else{
            Object[] new_parameters = new Object[parameters.length];
            for(int i=0;i<parameters.length;i++){
                new_parameters[i] = JSON.parseObject(parameters[i].toString(),parameterTypes[i]);
            }
            return new_parameters;
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt)throws Exception {
        if (evt instanceof IdleStateEvent){
            IdleStateEvent event = (IdleStateEvent)evt;
            if (event.state()== IdleState.ALL_IDLE){
                log.info("客户端已超过60秒未读写数据,关闭连接.{}",ctx.channel().remoteAddress());
                ctx.channel().close();
            }
        }else{
            super.userEventTriggered(ctx,evt);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)   {
        log.info(cause.getMessage());
        ctx.close();
    }
}
