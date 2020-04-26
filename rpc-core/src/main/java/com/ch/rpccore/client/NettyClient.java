package com.ch.rpccore.client;

import com.ch.rpccore.model.RpcRequest;
import com.ch.rpccore.model.RpcResponse;
import com.ch.rpccore.netty.codec.JSONDecoder;
import com.ch.rpccore.netty.codec.JSONEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import org.springframework.stereotype.Component;

/**
 * @Description: TODO
 * @Author chenhao
 * @Date 2020/4/23
 **/

@Component
@Slf4j
public class NettyClient  implements InitializingBean {
    private String host="127.0.0.1";
    private Integer port=8081;

    private Channel channel;

    public Channel getChannel() {
        return channel;
    }

    public void send(RpcRequest request){
        channel.writeAndFlush(request);
    }

    public void start(){
        try {

            EventLoopGroup group = new NioEventLoopGroup();

            Bootstrap b = new Bootstrap();
            // 使用NioSocketChannel来作为连接用的channel类
            b.group(group).channel(NioSocketChannel.class)
                    // 绑定连接初始化器
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel sc) throws Exception {
                            System.out.println("正在连接中...");
                            ChannelPipeline pipeline = sc.pipeline();
                            pipeline.addLast(new JSONEncoder()); //编码request
                            pipeline.addLast(new JSONDecoder()); //解码response
                            pipeline.addLast(new NettyClientHandler()); //客户端处理类

                        }
                    });
            //发起异步连接请求，绑定连接端口和host信息
            final ChannelFuture future = b.connect(host, port).sync();

            this.channel=future.channel();

            future.addListener((ChannelFutureListener) channelFuture -> {
                if (future.isSuccess()) {
                   log.info("连接服务器成功");
                } else {
                    log.error("连接服务器失败");
                    future.cause().printStackTrace();
                    group.shutdownGracefully(); //关闭线程组
                }
            });
        } catch (Exception e) {
            log.error("RPC 客户端启动异常");
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        start();
    }

}
