package com.example.springboot_elasticsearch;

import com.example.springboot_elasticsearch.dao.CustomerRepository;
import com.example.springboot_elasticsearch.model.Customer;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootElasticsearchApplicationTests {

    @Autowired
    private CustomerRepository repository;

    @Test
    public void saveCustomers() {
        repository.save(new Customer("Alice", "北北京",13));
        repository.save(new Customer("Bob", "北北京",23));
        repository.save(new Customer("neo", "⻄西安",30));
        repository.save(new Customer("summer", "烟台",22));
    }

    @Test
    public void fetchAllCustomers() {
        System.out.println("Customers found with findAll():");
        System.out.println("-------------------------------");
        for (Customer customer : repository.findAll()) {
            System.out.println(customer);
        }
    }

    @Test
    public void updateCustomers() {
        List<Customer> customers =  repository.findByUserName("summer");
        System.out.println(customers);
        Customer customer = customers.get(0);
        customer.setAddress("北北京市海海淀区⻄西直⻔门");
        repository.save(customer);
        List<Customer> xcustomer = repository.findByUserName("summer");
        System.out.println(xcustomer);
    }

    @Test
    public void fetchIndividualCustomers() {
        for (Customer customer : repository.findByAddress("北北京")) {
            System.out.println(customer);
        }
    }

    @Test
    public void fetchPageCustomers() {
        Sort.Order order=new Sort.Order(Sort.Direction.DESC, "address.keyword");
        Pageable pageable = PageRequest.of(0,10,Sort.by(order));
//        Sort sort = new Sort(Sort.Direction.DESC, "address.keyword");
//        Pageable pageable = PageRequest.of(0, 10, sort);
        Page<Customer> customers=repository.findByAddress("北北京", pageable);
        System.out.println("Page customers "+customers.getContent().toString());
    }

    @Test
    public void fetchPage2Customers() {
        QueryBuilder customerQuery = QueryBuilders.boolQuery()
                .must(QueryBuilders.matchQuery("address", "北北京"));
        Page<Customer> page = repository.search(customerQuery, PageRequest.of(0, 10));
        System.out.println("Page customers "+page.getContent().toString());
    }

}
