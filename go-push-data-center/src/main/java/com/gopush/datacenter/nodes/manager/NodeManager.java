package com.gopush.datacenter.nodes.manager;

import com.alibaba.fastjson.JSON;
import com.gopush.datacenter.nodes.manager.listener.event.ReloadNodesEvent;
import com.gopush.nodes.handlers.INodeMessageHandler;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * go-push
 *
 * @类功能说明：
 * @作者：喝咖啡的囊地鼠
 * @创建时间：2017/6/24 下午3:33
 * @VERSION：
 */

@Slf4j
@Component
public class NodeManager {

    private EventLoopGroup group = new NioEventLoopGroup();

    private Map<String, Node> nodeChannelPool = new ConcurrentHashMap<>();

    @Autowired
    private List<INodeMessageHandler> nodeMessageHandlers;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public void reload(){
        nodeChannelPool.forEach((k,node)-> node.destroy());
        nodeChannelPool.clear();
        applicationEventPublisher.publishEvent(ReloadNodesEvent.builder().eventName("reload").build());

    }

    public void remove(String nodeName){
        if (nodeChannelPool.containsKey(nodeName)){
            nodeChannelPool.get(nodeName).destroy();
            nodeChannelPool.remove(nodeName);
        }
    }

    public void put(String nodeName,
                    String intranetIp, int nodePort,
                    String internetIp, int devicePort){
        remove(nodeName);
        Node node = new Node(intranetIp,nodePort,internetIp,devicePort,group,nodeMessageHandlers);
        node.init();
        nodeChannelPool.put(nodeName,node);
        log.info("{}", JSON.toJSONString(nodeChannelPool));
    }

}
