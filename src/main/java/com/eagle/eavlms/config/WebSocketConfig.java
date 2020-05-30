package com.eagle.eavlms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;
//用于配置websocket
@Configuration  //定义配置类
public class WebSocketConfig {
    //注入ServerEndpointExporter 这个bean会自动注册使用了@ServerEndpoint注解声明的Websocket endpoint 使用springboot内置容器时
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

}
