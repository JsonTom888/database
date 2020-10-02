package com.example.mongodb_repository.dao;

import com.example.mongodb_repository.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author tom
 * @version V1.0
 * @date 2020/10/2 16:30
 */
public interface UserRepository extends MongoRepository<User,Long> {

    User findByUserName(String userName);

    /**
     * 分页
     * @param var1
     * @return
     */
    Page<User> findAll(Pageable page);


}
