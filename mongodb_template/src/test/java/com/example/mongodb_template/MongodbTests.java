package com.example.mongodb_template;

import com.example.mongodb_template.model.User;
import com.example.mongodb_template.service.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MongodbTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testSaveUser(){
        User user=new User();
        user.setId(26L);
        user.setUserName("JsonTom888");
        user.setPassWord("123456");
        userRepository.saveUser(user);
        System.out.println("插入数据： "+user);
    }

    @Test
    public void findUserByUserName(){
        User user= userRepository.findUserByUserName("JsonTom888");
        System.out.println("user is "+user);
    }

    @Test
    public void updateUser(){
        User user=new User();
        user.setId(26L);
        user.setUserName("畅想未来");
        user.setPassWord("88888888");
        userRepository.updateUser(user);
    }

    @Test
    public void deleteUserById(){
        userRepository.deleteUserById(26L);
    }

}
