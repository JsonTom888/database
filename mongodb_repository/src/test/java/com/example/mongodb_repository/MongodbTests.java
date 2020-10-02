package com.example.mongodb_repository;

import com.example.mongodb_repository.dao.UserRepository;
import com.example.mongodb_repository.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
        userRepository.save(user);
        System.out.println("插入数据： "+user);
    }

    @Test
    public void findUserByUserName(){
        User user= userRepository.findByUserName("JsonTom888");
        System.out.println("user is "+user);
    }

    @Test
    public void updateUser(){
        User user=new User();
        user.setId(26L);
        user.setUserName("畅想未来");
        user.setPassWord("88888888");
        userRepository.save(user);
    }

    @Test
    public void deleteUserById(){
        User user=new User();
        user.setId(26L);
        user.setUserName("畅想未来");
        user.setPassWord("88888888");
//        userRepository.delete(user);
        userRepository.deleteById(26L);
    }

    @Test
    public void testSave100User() throws Exception {
        for (long i=0;i<100;i++) {
            User user=new User();
            user.setId(i);
            user.setUserName("Tom_"+i);
            user.setPassWord("gre456j6hgdfg");
            userRepository.save(user);
        }
    }

    /**
     * 分页
     */
    @Test
    public void testPage(){
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(2, 10, sort);
        Page page = userRepository.findAll(pageable);
        System.out.println("users: "+page.getContent().toString());
    }

}
