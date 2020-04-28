package com.ey.digital;

import com.ey.digital.model.Address;
import com.ey.digital.repository.AddressMongoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MongoRestApplication implements CommandLineRunner {
  private static final Logger logger = LoggerFactory.getLogger(MongoRestApplication.class);

  @Autowired private AddressMongoRepository addresssRepository;

  public static void main(String[] args) {
    SpringApplication.run(MongoRestApplication.class, args);
  }

  public void run(String... args) {
    addresssRepository.deleteAll();

    Address address = new Address();
    address.setLastname("Jacksons");
    address.setFirstname("Tom");
    address.setAddressLine1("121 River street");
    address.setCity("Hoboken");
    address.setState("NJ");
    address.setZipCode("00730");
    addresssRepository.save(address);

    Address address2 = new Address();
    address2.setLastname("Littles");
    address2.setFirstname("Nancy");
    address2.setAddressLine1("100 Hart Street");
    address2.setAddressLine2("APT. 401");
    address2.setCity("Brooklyn");
    address2.setState("NY");
    address2.setZipCode("11206");
    addresssRepository.save(address2);

    logger.info("-------------------------------");

    logger.info("1. Address: found with findAll():");
    addresssRepository.findAll().forEach(addr -> logger.info(addr.toString()));

    logger.info("-------------------------------");

    logger.info("2. Address: found with findByZipCode():");
    addresssRepository.findByZipCode("00730").forEach(addr -> logger.info(addr.toString()));

    logger.info("-------------------------------");

    logger.info("3. Address: found with findByAddressLine_2Null():");
    addresssRepository.findByAddressLine2Null().forEach(addr -> logger.info(addr.toString()));

    logger.info("-------------------------------");

    logger.info("4. Address: found with findByCity():");
    addresssRepository.findByCity("Hoboken").forEach(addr -> logger.info(addr.toString()));

    logger.info("-------------------------------");

    logger.info("5. Address: found with findByLastname():");
    addresssRepository.findByLastname("Littles").forEach(addr -> logger.info(addr.toString()));

    logger.info("-------------------------------");

    logger.info("6. Address: found with findByLastnameAndFirstname():");
    addresssRepository
        .findByLastnameAndFirstname("Littles", "Nancy")
        .forEach(addr -> logger.info(addr.toString()));
  }
}
