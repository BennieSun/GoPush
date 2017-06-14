package com.gopush.protocol.device;

import com.gopush.protocol.exceptions.DeviceProtocolException;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * go-push
 *
 * @类功能说明：设备消息基类
 * @作者：喝咖啡的囊地鼠
 * @创建时间：2017/6/9
 * @VERSION：
 */

@Slf4j
public abstract class DeviceMessage {

    //消息 Type Key
    protected static final String DEVICE_TYPE_KEY = "T";

    /**
     * 消息类型
     * 1)心跳请求
     * 2)心跳响应
     * 3)握手请求
     * 4)握手响应
     * 5)消息推送
     * 6)推送反馈
     */
    protected enum Type {
        PI,    // PING
        PO,   // PONG
        HS,    // HANDSHAKE_REQ
        HSR,  // HANDSHAKE_RESP
        P,    // PUSH_REQ
        PR    // PUSH_RESP
    }

    /**
     * 获取设备消息类型
     * @return
     */
    protected abstract Type type();



    /**
     * 设备消息转换 JSONObject
     * @return
     * @throws JSONException
     */
    protected abstract JSONObject toEncode() throws JSONException;


    /**
     * 设备消息编码
     * @return
     */
    public String encode() throws DeviceProtocolException{
        try {
            JSONObject jsonObject = toEncode();
            if(jsonObject == null){
                //直接抛出空指针异常，确保 消息是必须要解码的
                //throw new NullPointerException("Message is empty");
                //前端不传 直接新建一个消息json空
                jsonObject = new JSONObject();
            }
            jsonObject.putOnce(DEVICE_TYPE_KEY,type());
            return jsonObject.toString();
        } catch (JSONException e) {
            throw new DeviceProtocolException(e);
        }
    }

    /**
     * 设备消息转换
     * @param json
     * @throws JSONException
     */
    protected abstract void toDecode(JSONObject json) throws JSONException;


    /**
     * 设备消息解码
     * @param json
     * @return
     * @throws DeviceProtocolException
     */
    public DeviceMessage decode(String json) throws DeviceProtocolException {
        try {
            JSONObject jsonObject = new JSONObject(json);
            DeviceMessage message;
            switch (Type.valueOf(jsonObject.getString(DEVICE_TYPE_KEY))){
                case PI:
                    message = Ping.builder().build();
                    break;
                case PO:
                    message = Pong.builder().build();
                    break;
                case HS:
                    message = HandShakeReq.builder().build();
                    break;
                case HSR:
                    message = HandShakeResp.builder().build();
                    break;
                case P:
                    message = PushReq.builder().build();
                    break;
                case PR:
                    message = PushResp.builder().build();
                    break;
                default:
                    throw new DeviceProtocolException("Unknown Device type " + jsonObject.getString(DEVICE_TYPE_KEY));
            }
            message.toDecode(jsonObject);
            return message;
        } catch (JSONException e) {
            throw new DeviceProtocolException("Exception occur,Message is " + json, e);
        }
    }

}
