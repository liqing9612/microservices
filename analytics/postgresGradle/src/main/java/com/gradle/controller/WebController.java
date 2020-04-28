package com.gradle.controller;

import com.gradle.model.Customer;
import com.gradle.repository.CustomerRepository;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Api(value = "/customer", description = "customer restful api")
@RestController
@RequestMapping("/customer")
public class WebController {
    @Autowired
    CustomerRepository repository;

    @GetMapping("save")
    public String process(){
        // save a single Customer
        repository.save(new Customer("Jack", "Smith"));

        // save a list of Customer
        repository.saveAll(Arrays.asList(
                new Customer("Adam", "Johnson"),
                new Customer("Kim", "Smith"),
                new Customer("David", "Williams"),
                new Customer("Peter", "Davis")));

        return "Done";
    }


    @GetMapping("findAllCustomers")
    public List<Customer> findAll(){
        return StreamSupport
                .stream(repository.findAll().spliterator(),false)
                .collect(Collectors.toList());

    }

    @GetMapping("findOne")
    public Customer findOne(){
        Customer cus = StreamSupport
                .stream(repository.findAll().spliterator(),false)
                .collect(Collectors.toList())
                .get(0);

        System.out.println("### Customer: " + cus);


        return cus;
    }

    @GetMapping("findById")
    public String findById(@RequestParam("id") long id){
        String result = "";
        result = repository.findById(id).toString();
        return result;
    }

    @GetMapping("findByLastName")
    public String fetchDataByLastName(@RequestParam("lastname") String lastName){
        String result = "";

        for(Customer cust: repository.findByLastName(lastName)){
            result += cust.toString() + "<br>";
        }

        return result;
    }
}