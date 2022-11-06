package com.example;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

@SpringBootTest
class MyRedisApplicationTests {

	@Resource
	private RedisTemplate redisTemplate;
	@Test
	void contextLoads() {

		redisTemplate.opsForValue().set("myKey", "myValue");
		System.out.println(redisTemplate.opsForValue().get("myKey"));
	}


}
