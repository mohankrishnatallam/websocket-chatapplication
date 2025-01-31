package com.argano.websocket.chat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;

import com.google.gson.Gson;

@Controller
public class ChatController {

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Autowired
	Gson gson;

	@Autowired
	private SimpMessageSendingOperations messagingTemplate;

	@MessageMapping("/chat.sendMessage")
	public void sendMessage(@Payload SendMessage chatMessage) {
		System.out.println(chatMessage);
		messagingTemplate.convertAndSendToUser(chatMessage.getTo(), "/queue/chat",
				gson.toJson(chatMessage));
		//return chatMessage;
	}

	@SuppressWarnings("deprecation")
	@MessageMapping("/chat.addUser")
	public void addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
		Set<byte[]> keys = redisTemplate.getConnectionFactory().getConnection().keys("*".getBytes());
		Iterator<byte[]> it = keys.iterator();
		ArrayList<String> arrayList = new ArrayList<String>();
		while(it.hasNext()){
		       byte[] data = (byte[])it.next();
		       arrayList.add(new String(data, 0, data.length));
		       String string = redisTemplate.opsForValue().get(new String(data, 0, data.length));
		}
		System.out.println(headerAccessor.getSessionId());
		List<String> multiGet = redisTemplate.opsForValue().multiGet(arrayList);
		List<ChatMessage> list = new ArrayList();
		for(String str:multiGet) {
			ChatMessage fromJson = gson.fromJson(str, ChatMessage.class);
			list.add(fromJson);
		}
		redisTemplate.opsForValue().set(chatMessage.getSender(), gson.toJson(chatMessage));
		headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
		String json = gson.toJson(multiGet);
		
		  messagingTemplate.convertAndSendToUser(chatMessage.getSender(),
		  "/queue/notification", gson.toJson(list));
		 
		messagingTemplate.convertAndSend("/topic/public",
				gson.toJson(chatMessage));
	}
}
