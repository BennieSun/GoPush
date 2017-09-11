package com.gopush.nodeserver.infos.watchdog.listener;

import com.gopush.nodeserver.infos.watchdog.listener.event.NodeServerInfoEvent;
import com.gopush.nodeserver.registers.NodeServerRegisterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author chenxiangqi
 * @date 2017/9/12 上午12:39
 */
@Slf4j
@Component
public class PostNodeServerInfoDataListener {

    @Autowired
    private NodeServerRegisterService nodeServerRegisterService;

    @EventListener(condition = "#event.nodeServerInfo != null")
    public void postDataToZk(NodeServerInfoEvent event) {
        nodeServerRegisterService.postNewData(event.getNodeServerInfo());
    }
}
