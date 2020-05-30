package com.eagle.eavlms.service;
import com.eagle.eavlms.entity.NoticeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//对websocket连接的具体处理逻辑
//请求地址，id是用户id
@ServerEndpoint("/notice-ws/{id}")  //服务位于哪个访问点 即访问入口
@Service
@Slf4j
public class WebSocketEndpoint {

    //保存连接的用户信息，主动推送
    //ConcurrentHashMap是map，表示线程安全的
    private static final Map<String,Session> WEB_SOCKET_SESSION_MAP =new ConcurrentHashMap<>();

    //用于序列化对象
    private static final ObjectMapper OBJECT_MAPPER=new ObjectMapper();
    //当用户连接时调用
    @OnOpen
    public void onOpen(Session session,@PathParam(value = "id") String id) {
        log.info("======>id为{}用户WebSocket连接成功",id);
        WEB_SOCKET_SESSION_MAP.put(id,session);

    }

    //当用户关闭时调用
    @OnClose
    public void onClose(@PathParam(value = "id") String id) {
        log.info("======>id为{}用户WebSocket连接删除",id);
        WEB_SOCKET_SESSION_MAP.remove(id);//从map中删除
    }

    //用户发来消息 前端发来数据时后端接受处理逻辑
    @OnMessage
        public void onMessage(String message,@PathParam(value = "id") String id)  {
        WEB_SOCKET_SESSION_MAP.get(message).getAsyncRemote().sendText(message);
        log.info("======>id为{}用户发送给{}一条消息：{}",id,message,message);

    }

    /**
     * 服务器主动推送消息到客户端
     * @param noticeInfo
     * @throws JsonProcessingException
     */
    public void sendMessage(NoticeInfo noticeInfo) throws JsonProcessingException {
        try {
            //WEB_SOCKET_SESSION_MAP保存了用户的连接session
            WEB_SOCKET_SESSION_MAP.get(noticeInfo.getToUser().getId().toString())
                    //OBJECT_MAPPER.writeValueAsString序列化消息内容为json格式 getAsyncRemote非阻塞：在不能立刻得到结果之前，该调用不会阻塞当前线程。
                    .getAsyncRemote().sendText(OBJECT_MAPPER.writeValueAsString(noticeInfo));
            log.info("======>id为{}用户发送给{}一条消息：{}",noticeInfo.getFromUser().getId(),
                    noticeInfo.getToUser().getId(),noticeInfo.getMessage());
        }catch (NullPointerException e){
            log.info("======>id为{}的用户不在线！",noticeInfo.getToUser().getId());
        }
    }
}
