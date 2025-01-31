package com.argano.websocket.chat;

import lombok.*;
import com.argano.websocket.chat.MessageType;
@Getter
@Setter
@AllArgsConstructor
@Builder
public class ChatMessage {

    private MessageType type;
    private String content;
    private String sender;
    private int unreadMessages;
	public int getUnreadMessages() {
		return unreadMessages;
	}
	public void setUnreadMessages(int unreadMessages) {
		this.unreadMessages = unreadMessages;
	}
	public MessageType getType() {
		return type;
	}
	public void setType(MessageType type) {
		this.type = type;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public ChatMessage(MessageType type, String content, String sender) {
		this.type = type;
		this.content = content;
		this.sender = sender;
	}
	public ChatMessage(){}
}
