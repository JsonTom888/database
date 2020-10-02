package com.example.mongodb_template.service;

import com.example.mongodb_template.model.User;

/**
 * @author tom
 * @version V1.0
 * @date 2020/10/2 14:52
 */
public interface UserRepository {

    public void saveUser(User user);

    public User findUserByUserName(String userNane);

    public long updateUser(User user);

    public void deleteUserById(Long id);

}
