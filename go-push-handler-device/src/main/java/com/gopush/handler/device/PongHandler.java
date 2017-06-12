package com.gopush.handler.device;

import com.gopush.protocol.device.DeviceMessage;
import com.gopush.protocol.device.Pong;
import lombok.Builder;
import lombok.Data;

/**
 * go-push
 *
 * @类功能说明：
 * @作者：喝咖啡的囊地鼠
 * @创建时间：2017/6/12 下午10:03
 * @VERSION：
 */

@Builder
@Data
public class PongHandler  extends  AbstractBatchProcessHandler<Pong> implements DeviceMessagehandler{
    @Override
    public boolean support(DeviceMessage message) {
        return message instanceof Pong;
    }

    @Override
    public void call(DeviceMessage message) {

    }
}
