package com.argano.websocket.chat;

public class SendMessage {

	private String content;
    private String from;
    private String to;
    
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	@Override
	public String toString() {
		return "SendMessage [content=" + content + ", from=" + from + ", to=" + to + "]";
	}
}
