package com.huanghe.ws.java;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 监听websocket请求地址 /myWs
 */
@ServerEndpoint("/myWs")
@Component
@Slf4j
public class WsServerEndpoint {

    static Map<String,Session> sessionMap = new ConcurrentHashMap<>();

    /**
     * 链接建立的时候执行的操作
     * @param session session
     */
    @OnOpen
    public void onOpen(Session session){
        // 保留和客户端的websocket链接
        sessionMap.put(session.getId(),session);
        log.info("websocket is open");
    }

    /**
     * 接受到消息的时候操作
     * @param receive 客户端的消息
     * @return 服务端的消息
     */
    @OnMessage
    public String onMessage(String receive){
        log.info("收到客户端一条消息："+receive);
        return "服务端已经接受到了你的消息";
    }

    /**
     * 当用户把浏览器关闭（代表着websocket关闭了），会触发关闭请求
     */
    @OnClose
    public void onClose(Session session){
        sessionMap.remove(session.getId());
    }

    /**
     * 服务端给客户端发送消息,每隔2秒发送心态
     */
    @Scheduled(fixedRate = 2000)
    public void sendMessage() throws IOException {
        Set<String> ids = sessionMap.keySet();
        for (String id : ids) {
            Session session = sessionMap.get(id);
            session.getBasicRemote().sendText("心跳");
        }
    }
}
