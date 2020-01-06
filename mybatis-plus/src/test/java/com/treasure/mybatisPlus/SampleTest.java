package com.treasure.mybatisPlus;

import com.treasure.mybatisPlus.mapper.UserMapper;
import com.treasure.mybatisPlus.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * <p>
 * 简述一下～
 * <p>
 *
 * @author 时前程 2020年01月06日
 * @see
 * @since 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SampleTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testSelect() {
        List<User> userList = userMapper.selectList(null);
//        Assert.assertEquals(5, userList.size());
        userList.forEach(System.out::println);
    }

}
