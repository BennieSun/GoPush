package com.gopush.infos.datacenter.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author chenxiangqi
 * @date 2017/9/15 上午11:39
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NodeClientLoaderInfo {
    private String name;
    private int receiveCounter;
    private int sendCounter;
}
