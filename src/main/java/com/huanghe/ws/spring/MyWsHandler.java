package com.huanghe.ws.spring;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;
import org.springframework.web.socket.server.support.AbstractHandshakeHandler;

import javax.websocket.Session;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * websocket处理程序
 */
@Component
@Slf4j
public class MyWsHandler extends AbstractWebSocketHandler {

    private static Map<String, SessionBean> sessionBeanMap;

    private static AtomicInteger clientIdMarker;

    static {
        sessionBeanMap = new ConcurrentHashMap<>();
        clientIdMarker = new AtomicInteger(0);
    }

    /**
     * 连接建立完成之后
     *
     * @param session WebSocketSession
     * @throws Exception Exception
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        SessionBean sessionBean = new SessionBean(session, clientIdMarker.getAndIncrement());
        sessionBeanMap.put(session.getId(), sessionBean);
        log.info(sessionBeanMap.get(session.getId()).getClientId() + "：" + "建立了连接");

    }

    /**
     * 接受到消息
     *
     * @param session WebSocketSession
     * @param message 客户端消息
     * @throws Exception Exception
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info(sessionBeanMap.get(session.getId()).getClientId() + "：" + message);
        super.handleTextMessage(session, message);
        Object parse = JSON.parse(message.getPayload());
        session.sendMessage(new TextMessage(JSON.toJSONString(parse)));
    }


    /**
     * 连接发生异常
     *
     * @param session   WebSocketSession
     * @param exception exception
     * @throws Exception
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        super.handleTransportError(session, exception);
        if (session.isOpen()) {
            session.close();
        }
        sessionBeanMap.remove(session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        log.info(sessionBeanMap.get(session.getId()).getClientId() + "：" + "关闭了连接");
        sessionBeanMap.remove(session.getId());

    }

    // /**
    //  * 服务端给客户端发送消息,每隔2秒发送心态
    //  */
    // @Scheduled(fixedRate = 2000)
    // public void sendMessage() throws IOException {
    //     Set<String> ids = sessionBeanMap.keySet();
    //     for (String id : ids) {
    //         WebSocketSession session = sessionBeanMap.get(id).getWebSocketSession();
    //         session.sendMessage(new TextMessage("心跳"));
    //     }
    // }
}
