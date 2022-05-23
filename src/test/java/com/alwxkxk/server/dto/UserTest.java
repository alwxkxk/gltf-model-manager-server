package com.alwxkxk.server.dto;

import com.alwxkxk.server.mapper.UserMapper;
import com.alwxkxk.server.utils.MybatisUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Slf4j
public class UserTest {
    private SqlSession sqlSession;
    private User user;

    @Before
    public void setUp() throws Exception{
        sqlSession = MybatisUtil.getSession();
        user = sqlSession.getMapper(User.class);
    }

    @After
    public void tearDown() throws Exception{
        MybatisUtil.closeSession();
    }


    @Test
    @DisplayName("add user")
    void testAddUser() {
        try{
            sqlSession = MybatisUtils.getSqlSession();
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            mapper.addUser(new User(1, "test", "test"));
            sqlSession.commit(); //提交事务
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
