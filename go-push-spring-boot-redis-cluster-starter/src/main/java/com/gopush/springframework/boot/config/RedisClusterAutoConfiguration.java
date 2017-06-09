package com.gopush.springframework.boot.config;

import com.gopush.redis.RedisClusterFactory;
import com.gopush.springframework.boot.properties.RedisClusterProperties;
import com.gopush.springframework.boot.RedisClusterTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static java.lang.Integer.*;

/**
 * go-push
 *
 * @类功能说明：Redis-Cluster自动配置类
 * @作者：chenxiangqi
 * @创建时间：2017/6/9 下午9:55
 * @VERSION：
 */

@Configuration
@EnableConfigurationProperties(value = {RedisClusterProperties.class})
@ConditionalOnClass({JedisCluster.class, JedisPoolConfig.class, RedisClusterFactory.class, RedisClusterTemplate.class})
@ConditionalOnProperty(prefix = "redis.cluster", value = "enable", matchIfMissing = true)
@Slf4j
public class RedisClusterAutoConfiguration {

    @Autowired
    private RedisClusterProperties redisClusterProperties;

    @Bean
    @ConditionalOnMissingBean(RedisClusterTemplate.class)
    public RedisClusterTemplate redisClusterTemplate(){

        String[] dockedServers = redisClusterProperties.getDockedServers();
        String[] servers = redisClusterProperties.getServers();
        if (ArrayUtils.isEmpty(servers)){
            throw new RuntimeException(" redis cluster servers is empty");
        }
        RedisClusterFactory factory = new RedisClusterFactory();

        Set<HostAndPort> serverNodes = new HashSet<>();
        for (String v: servers) {
            String[] t = v.split(":");
            serverNodes.add(new HostAndPort(t[0],Integer.parseInt(t[1])));
        }
        factory.setJedisClusterNodes(serverNodes);
        if (ArrayUtils.isNotEmpty(dockedServers)){
            Set<HostAndPort> dockedServerNodes = new HashSet<>();
            for (String v: dockedServers) {
                String[] t = v.split(":");
                dockedServerNodes.add(new HostAndPort(t[0],Integer.parseInt(t[1])));
            }
            factory.setDockedJedisClusterNodes(dockedServerNodes);
        }
        factory.setClientPoolSize(redisClusterProperties.getPoolSize());
        factory.setClusterTimeout(redisClusterProperties.getTimeout());
        log.info("RedisCluster setting success!");
        return new RedisClusterTemplate(factory);
    }

}