package com.alwxkxk.server.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class MybatisUtils {
    private static SqlSessionFactory sqlSessionFactory;
    static {
        //编译后的文件路径，即target/classes下的路径
        String resource = "mybatis-config.xml";
        InputStream inputStream = null;
        try {
            inputStream = Resources.getResourceAsStream(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //获取sqlSessionFactory对象
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
    }
    //根据sqlSessionFactory获取SqlSession对象
    public static SqlSession getSqlSession() {
        //带参的openSession方法可以显式开启和关闭自动提交功能。
        //MyBatis默认是关闭自动提交功能的，因此DML语句需要手动提交。
        return sqlSessionFactory.openSession();
    }
}
