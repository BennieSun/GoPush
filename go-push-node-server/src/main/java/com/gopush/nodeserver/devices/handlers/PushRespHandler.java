package com.gopush.nodeserver.devices.handlers;

import com.gopush.common.Constants;
import com.gopush.devices.handlers.IDeviceMessageHandler;
import com.gopush.nodeserver.devices.BatchProcesser;
import com.gopush.protocol.device.DeviceMessage;
import com.gopush.protocol.device.PushResp;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * go-push
 *
 * @类功能说明：推送消息后的响应批处理器
 * @作者：喝咖啡的囊地鼠
 * @创建时间：2017/6/12 下午10:08
 * @VERSION：
 */

@Slf4j
public class PushRespHandler extends BatchProcesser<PushResp> implements IDeviceMessageHandler<PushResp> {
    @Override
    public boolean support(PushResp message) {
        return message instanceof PushResp;
    }

    @Override
    public void call(ChannelHandlerContext context, PushResp message) {
        Channel channel = context.channel();
        if (!channel.hasAttr(Constants.CHANNEL_ATTR_HANDSHAKE)){
            log.warn("Channel has not handshake, channel {} will be closed",channel);
            context.close();
            return;
        }
        //接收成功后,将推送的消息置换成已读或删除等操作
        if (PushResp.Result.S.equals(message.getResult()) || PushResp.Result.D.equals(message.getResult())){
            putMsg(message);
            log.debug("Receive pushResp , device :{}, msg id :{}, reason :{}!", message.getDevice(),message.getMsgId(),message.getResult());
        }
        else {
            log.warn("Receive pushResp , message not be received, device :{}, msg id :{}, reason :{}",message.getDevice(),message.getMsgId(),message.getResult());
        }

    }


    @Override
    protected String getBatchExecutorName() {
        return "Resp-Push-BatchExecutor";
    }

    @Override
    protected boolean retryFailure() {
        return true;
    }

    @Override
    protected void batchHandler(List<PushResp> batchReq) throws Exception {
        // TODO: 2017/6/18 处理推送的消息的结果
        // 将消息从待发送移除
        // 更新消息已经被投递的数量
        // 将该消息置成已发送
    }


}
