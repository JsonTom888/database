package com.example.mongodb_repository.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author tom
 * @version V1.0
 * @date 2020/10/2 16:29
 */
@Data
public class User implements Serializable {

    private static final long serialVersionUID = -3446226524091463456L;

    private Long id;
    private String userName;
    private String passWord;

}