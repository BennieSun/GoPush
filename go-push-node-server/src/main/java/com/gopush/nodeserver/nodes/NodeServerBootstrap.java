package com.gopush.nodeserver.nodes;

import com.gopush.nodeserver.nodes.inbound.NodeChannelInBoundHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * go-push
 *
 * @类功能说明：
 * @作者：喝咖啡的囊地鼠
 * @创建时间：2017/6/20 下午9:08
 * @VERSION：
 */

@Slf4j
public class NodeServerBootstrap {

    private EventLoopGroup bossGroup = new NioEventLoopGroup();
    private EventLoopGroup workGroup =  new NioEventLoopGroup();

    @Setter
    private int port;

    @Autowired
    private NodeChannelInBoundHandler nodeChannelInBoundHandler;

    @PostConstruct
    public void start() throws Exception {

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup,workGroup)
                .channelFactory(NioServerSocketChannel::new)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {

                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast("frameDecoder",new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,4,0,4));
                        pipeline.addLast("stringDecoder",new StringDecoder(CharsetUtil.UTF_8));
                        pipeline.addLast("frameEncoder",new LengthFieldPrepender(4));
                        pipeline.addLast("stringEncoder",new StringEncoder(CharsetUtil.UTF_8));
                        pipeline.addLast("idleStateHandler", new IdleStateHandler(300,0,0));
                        pipeline.addLast("handler",nodeChannelInBoundHandler);
                    }
                })
                .option(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_REUSEADDR,true)
                .option(ChannelOption.SO_SNDBUF,2048)
                .option(ChannelOption.SO_RCVBUF,1024);
        bootstrap.bind(port).sync();
        log.info("Node server start successful!  listening port: {}",port);
    }



    @PreDestroy
    public void destory(){
        log.debug("Node Server will be stoped!");
        bossGroup.shutdownGracefully();
        workGroup.shutdownGracefully();
    }
}