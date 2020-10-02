package com.example.springboot_elasticsearch.dao;

import com.example.springboot_elasticsearch.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * @author tom
 * @version V1.0
 * @date 2020/10/2 18:00
 */
public interface CustomerRepository extends ElasticsearchRepository<Customer,String> {

    List<Customer> findByAddress(String address);
    List<Customer> findByUserName(String userName);
    int deleteByUserName(String userName);
    Page<Customer> findByAddress(String address, Pageable pageable);

}
