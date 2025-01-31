package com.argano.websocket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import com.argano.websocket.chat.ChatMessage;
import com.argano.websocket.chat.MessageType;
import com.google.gson.Gson;

@Component
public class RedisConnectionConfig {

	@SuppressWarnings("deprecation")
	@Bean
	RedisTemplate<String, String> redisTemplate(RedisConnectionFactory connectionFactory) {
		RedisTemplate<String, String> redisTemplate = new RedisTemplate<String, String>();
		redisTemplate.setConnectionFactory(connectionFactory);
		redisTemplate.getConnectionFactory().getConnection().flushAll();
		setRedisTemplateConfigValues(connectionFactory,redisTemplate);
		return redisTemplate;
	}

	@Bean
	Gson gson() {
		return new Gson();
	}

	@SuppressWarnings("unchecked")
	private void setRedisTemplateConfigValues(RedisConnectionFactory redisConnectionFactory,
			@SuppressWarnings("rawtypes") RedisTemplate template) {
		template.setConnectionFactory(redisConnectionFactory);
		template.setKeySerializer(new StringRedisSerializer());
		template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
		template.setHashKeySerializer(new StringRedisSerializer());
		template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
	}

}
