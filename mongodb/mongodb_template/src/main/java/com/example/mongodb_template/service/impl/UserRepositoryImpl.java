package com.example.mongodb_template.service.impl;

import com.example.mongodb_template.model.User;
import com.example.mongodb_template.service.UserRepository;
import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

/**
 * @author tom
 * @version V1.0
 * @date 2020/10/2 14:51
 */
@Service
public class UserRepositoryImpl implements UserRepository {

    @Autowired
    private MongoTemplate mongodb;

    /**
     * 创建对象
     * @param user
     */
    @Override
    public void saveUser(User user) {
        mongodb.save(user);

    }

    /**
     * 查找对象
     * @param userNane
     * @return
     */
    @Override
    public User findUserByUserName(String userNane) {
        Query query = new Query(Criteria.where("userName").is(userNane));
        User user = mongodb.findOne(query,User.class);
        return user;
    }

    /**
     * 跟新对象
     * @param user
     * @return
     */
    @Override
    public long updateUser(User user) {
        Query query=new Query(Criteria.where("id").is(user.getId()));
        Update update = new Update().set("userName",user.getUserName()).set("password",user.getPassWord());
        //更新查询返回结果集的第一条
        UpdateResult result =mongodb.updateFirst(query,update,User.class);
        //更新查询返回结果集的所有
        // mongoTemplate.updateMulti(query,update,UserEntity.class);
        if(result!=null)
            return result.getMatchedCount();
        else
            return 0;
    }

    /**
     * 删除对象
     * @param id
     */
    @Override
    public void deleteUserById(Long id) {
        Query query=new Query(Criteria.where("id").is(id));
        mongodb.remove(query,User.class);
    }
}
