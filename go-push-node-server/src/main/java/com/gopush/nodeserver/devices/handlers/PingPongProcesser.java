package com.gopush.nodeserver.devices.handlers;

import com.gopush.common.Constants;
import com.gopush.nodeserver.devices.BatchProcesser;
import com.gopush.redis.RedisClusterDefaultVisitor;
import com.gopush.springframework.boot.RedisClusterTemplate;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * go-push
 *
 * @类功能说明：
 * @作者：喝咖啡的囊地鼠
 * @创建时间：2017/6/22 上午1:28
 * @VERSION：
 */
@Slf4j
public  abstract class PingPongProcesser<T> extends BatchProcesser<T>{


    @Autowired
    private RedisClusterTemplate redisClusterTemplate;

    /**
     * 检测是否已经握手
     * @param channel
     * @return
     */
    protected boolean checkHandShake(Channel channel){
        if (!channel.hasAttr(Constants.CHANNEL_ATTR_HANDSHAKE)){
            log.warn("channel not handshake, channel:{}",channel);
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }


    protected void liveHandShake(List<Object[]> batchReq){
        if (CollectionUtils.isNotEmpty(batchReq)){

            RedisClusterDefaultVisitor visitor = redisClusterTemplate.defaultVisitor();

            batchReq.stream().forEach((ele) -> {
                String device = (String) ele[0];
                int[] idles = (int[]) ele[1];
                visitor.expire(Constants.DEVICE_KEY+device,idles[0]);
            });

        }
    }

}