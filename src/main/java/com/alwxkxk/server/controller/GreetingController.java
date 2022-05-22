package com.alwxkxk.server.controller;

import java.util.concurrent.atomic.AtomicLong;

import com.alwxkxk.server.dto.User;
import com.alwxkxk.server.entity.Greeting;

import com.alwxkxk.server.mapper.UserMapper;
import com.alwxkxk.server.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *  http 路由测试代码 greeting
 */
@RestController
public class GreetingController {
    private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();

	@GetMapping("/greeting")
	public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
		return new Greeting(counter.incrementAndGet(), String.format(template, name));
	}

	@PostMapping("/addUser")
	public String addUser(
			@RequestParam(value = "name") String name,
			@RequestParam(value = "pwd") String pwd
	){
		SqlSession sqlSession = null;
		try {
			sqlSession = MybatisUtils.getSqlSession();
			UserMapper mapper = sqlSession.getMapper(UserMapper.class);
			mapper.addUser(new User(1, name, pwd));
			sqlSession.commit(); //提交事务
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(sqlSession != null){
				sqlSession.close();
			}
		}
		return "test";
	}
}
