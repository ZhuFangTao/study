package com.lagou.test;

import com.lagou.dao.IUserDao;
import com.lagou.io.Resources;
import com.lagou.pojo.User;
import com.lagou.sqlSession.SqlSession;
import com.lagou.sqlSession.SqlSessionFactory;
import com.lagou.sqlSession.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

public class IPersistenceTest {

    /**
     * 测试更新
     *
     * @throws Exception
     */
    @Test
    public void testUpdate() throws Exception {
        InputStream resourceAsSteam = Resources.getResourceAsSteam("sqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsSteam);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //调用
        User user = new User();
        user.setId(2);
        user.setUsername("hello world");
        user.setPassword("123456");
        user.setBirthday("2010-01-01");
        IUserDao userDao = sqlSession.getMapper(IUserDao.class);
        userDao.updateUserById(user);
    }


    /**
     * 测试查找所有
     *
     * @throws Exception
     */
    @Test
    public void testFindAll() throws Exception {
        InputStream resourceAsSteam = Resources.getResourceAsSteam("sqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsSteam);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //调用
        IUserDao userDao = sqlSession.getMapper(IUserDao.class);
        List<User> userList = userDao.findAll();
        for (User user : userList) {
            System.out.println(user.toString());
        }
    }

    /**
     * 测试删除
     *
     * @throws Exception
     */
    @Test
    public void delete() throws Exception {
        InputStream resourceAsSteam = Resources.getResourceAsSteam("sqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsSteam);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        IUserDao userDao = sqlSession.getMapper(IUserDao.class);
        userDao.deleteById(2);
    }

    /**
     * 测试新增
     *
     * @throws Exception
     */
    @Test
    public void insert() throws Exception {
        InputStream resourceAsSteam = Resources.getResourceAsSteam("sqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsSteam);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        IUserDao userDao = sqlSession.getMapper(IUserDao.class);
        User user = new User();
        user.setId(2);
        user.setUsername("hello world");
        user.setPassword("123");
        user.setBirthday("2020-01-01");
        userDao.insert(user);
    }


}
