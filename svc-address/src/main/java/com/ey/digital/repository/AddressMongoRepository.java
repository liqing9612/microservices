package com.ey.digital.repository;

import com.ey.digital.model.Address;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressMongoRepository extends MongoRepository<Address, Long> {
  List<Address> findByZipCode(String zipCode);

  List<Address> findByCity(String city);

  List<Address> findByAddressLine2Null();

  List<Address> findByLastname(String lastname);

  List<Address> findByLastnameAndFirstname(String lastname, String firstname);
}
