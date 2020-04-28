package ey.analytics.data.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import ey.analytics.data.mongodb.entity.Laptop;

public interface LaptopMongoRepository extends MongoRepository<Laptop, Integer> {
}