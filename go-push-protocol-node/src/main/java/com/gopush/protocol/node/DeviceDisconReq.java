package com.gopush.protocol.node;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * go-push
 *
 * @类功能说明：设备下线上报请求
 * @作者：喝咖啡的囊地鼠
 * @创建时间：2017/6/9
 * @VERSION：
 */


@Builder
@Slf4j
public class DeviceDisconReq extends NodeMessageReq{

    @Override
    protected Type type() {
        return Type.DI;
    }

    @Override
    protected JSONObject toEncode() throws JSONException {
        return null;
    }

    @Override
    protected void toDecode(JSONObject jsonObject) throws JSONException {
    }
}
