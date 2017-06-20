package com.gopush.nodeserver.devices.infos;

import lombok.Builder;
import lombok.ToString;

/**
 * go-push
 *
 * @类功能说明：每个批处理机内部处理器 信息
 * @作者：喝咖啡的囊地鼠
 * @创建时间：2017/6/20 下午8:37
 * @VERSION：
 */
@Builder
@ToString
public class ProcessorInfo {

    private String batchName;

    private int index;

    private int loader;

}