package com.ch.rpccore.server;

import com.ch.rpccore.annotation.RpcService;
import com.ch.rpccore.netty.codec.JSONDecoder;
import com.ch.rpccore.netty.codec.JSONEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @Description: TODO
 * @Author chenhao
 * @Date 2020/4/22
 **/
@Component
@Slf4j
public class NettyServer implements ApplicationContextAware,InitializingBean {

    private Map<String, Object> serviceMap = new HashMap<>();

    //@Value("${rpc.server.address}")
    private String host="127.0.0.1";
    private Integer port=8081;
    /**
     * bossGroup就是parentGroup，是负责处理TCP/IP连接的
     */
    private static final EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    /**
     * workerGroup就是childGroup,是负责处理Channel(通道)的I/O事件
     */
    private static final EventLoopGroup workerGroup = new NioEventLoopGroup(4);

    private static Executor singleExecutor= new ThreadPoolExecutor(1,1,0,TimeUnit.SECONDS,new LinkedBlockingQueue(1000));

    @Override
    public void afterPropertiesSet() {

        final NettyServerHandler handler = new NettyServerHandler(serviceMap);

        singleExecutor.execute(() -> {
            try {
                ServerBootstrap bootstrap = new ServerBootstrap();
                bootstrap.group(bossGroup,workerGroup).
                        channel(NioServerSocketChannel.class).
                        option(ChannelOption.SO_BACKLOG,1024).
                        childOption(ChannelOption.SO_KEEPALIVE,true).
                        childOption(ChannelOption.TCP_NODELAY,true).
                        childHandler(new ChannelInitializer<SocketChannel>() {
                            //创建NIOSocketChannel成功后，在进行初始化时，将它的ChannelHandler设置到ChannelPipeline中，用于处理网络IO事件
                            @Override
                            protected void initChannel(SocketChannel channel) {
                                ChannelPipeline pipeline = channel.pipeline();
                                pipeline.addLast(new IdleStateHandler(0, 0, 60));
                                pipeline.addLast(new JSONEncoder());
                                pipeline.addLast(new JSONDecoder());
                                pipeline.addLast(handler);
                            }
                        });
                ChannelFuture future = bootstrap.bind(host,port).sync();
                future.addListener((ChannelFutureListener) channelFuture -> {
                    if (future.isSuccess()) {
                        log.info("RPC 服务器启动.监听端口:"+port);

                    } else {
                        log.info("连接服务器失败");
                        future.cause().printStackTrace();
                        //关闭线程组
                        workerGroup.shutdownGracefully();
                    }
                });
                //registry.register(serverAddress);
                //等待服务端监听端口关闭add
                future.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                log.error("RPC 服务器启动异常");
            }
        });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        log.info("开始加载rpc服务类");
        Map<String, Object> beans= applicationContext.getBeansWithAnnotation(RpcService.class);
        for(Object serviceBean:beans.values()){
            Class<?> clazz = serviceBean.getClass();
            Class<?>[] interfaces = clazz.getInterfaces();
            for (Class<?> inter : interfaces){
                String interfaceName = inter.getName();
                log.info("加载服务类: {}", interfaceName);
                serviceMap.put(interfaceName, serviceBean);
            }
        }
    }
}
