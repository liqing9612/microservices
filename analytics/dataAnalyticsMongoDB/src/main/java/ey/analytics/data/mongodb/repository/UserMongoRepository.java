package ey.analytics.data.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import ey.analytics.data.mongodb.entity.User;

public interface UserMongoRepository extends MongoRepository<User, Long> {
}