package com.example.springboot_elasticsearch.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * @Document 注解会对实体中的所有属性建立索引
 * indexName = "customer" 表示创建一个名称为 "customer" 的索引
 * type = "customer" 表示在索引中创建一个名为 "customer" 的 type
 * shards = 1 表示只使用一个分片
 * replicas = 0 表示不使用复制
 * refreshInterval = "-1" 表示禁用索引刷新
 *
 * @author tom
 * @version V1.0
 * @date 2020/10/2 17:56
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "customer", type = "customer", shards = 1, replicas = 0, refreshInterval = "-1")
public class Customer {
    @Id
    private String id;
    private String userName;
    private String address;
    private int age;

    public Customer(String userName, String address, int age) {
        this.userName = userName;
        this.address = address;
        this.age = age;
    }
}
