package com.huanghe.ws.spring;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.socket.WebSocketSession;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SessionBean {

    private WebSocketSession webSocketSession;

    private Integer clientId;
}
