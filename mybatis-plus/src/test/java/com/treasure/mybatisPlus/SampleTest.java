package com.treasure.mybatisPlus;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.treasure.mybatisPlus.mapper.UserMapper;
import com.treasure.mybatisPlus.model.GradeEnum;
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
      /*  List<User> userList = userMapper.selectList(null);
//        Assert.assertEquals(5, userList.size());
        userList.forEach(System.out::println);*/
        List<User> user = userMapper.selectList(new QueryWrapper<User>()
                .lambda().eq(User::getGradle, GradeEnum.HIGH));
      user.forEach(a->{
          System.out.println(a.getGradle());
      });
    }
    @Test
    public void testInsert() {

        User user = new User();
        user.setAge(100);
        user.setEmail("13285415306@163.com");
        user.setName("testName");
        user.setGradle(GradeEnum.HIGH);
        int userList = userMapper.insert(user);
//        Assert.assertEquals(5, userList.size());
        System.out.println(user.getId());
    }
    @Test
    public void testUpdate() {

        User user = new User();
        user.setId(1214395770695024641L);
        user.setGradle(GradeEnum.PRIMARY);
        int userList = userMapper.updateById(user);
//        Assert.assertEquals(5, userList.size());
        System.out.println(user.getId());
    }

    @Test
    public void lambdaPagination() {
        Page<User> page = new Page<>(1, 3);
        Page<User> result = userMapper.selectPage(page, Wrappers.<User>lambdaQuery().ge(User::getAge, 1).orderByAsc(User::getAge));

    }

    }
