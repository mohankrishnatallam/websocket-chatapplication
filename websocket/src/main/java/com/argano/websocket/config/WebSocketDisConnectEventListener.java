package com.argano.websocket.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.argano.websocket.chat.ChatMessage;
import com.argano.websocket.chat.MessageType;
import com.google.gson.Gson;

@Component
@Slf4j
@RequiredArgsConstructor
public class WebSocketDisConnectEventListener {

	@Autowired
    private SimpMessageSendingOperations messagingTemplate;
    
    @Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	@Autowired
	Gson gson;

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        if (username != null) {
        	ChatMessage chatMessage = new ChatMessage();
        	chatMessage.setType(MessageType.LEAVE);
        	chatMessage.setSender(username);
        	redisTemplate.delete(username);
            messagingTemplate.convertAndSend("/topic/public", gson.toJson(chatMessage));
        }
    }

}
