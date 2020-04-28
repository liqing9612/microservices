package com.ey.digital.controller;

import com.ey.digital.exception.EntityNotFoundException;
import com.ey.digital.model.Address;
import com.ey.digital.repository.AddressMongoRepository;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Validated
public class MongoServiceController {
  private static final Logger logger = LoggerFactory.getLogger(MongoServiceController.class);

  @Autowired private AddressMongoRepository addresssRepository;

  @GetMapping("address/city")
  public List<Address> getAddressByCity(@RequestParam(name = "city") @NotEmpty String city) {
    try {
      List<Address> addressList = addresssRepository.findByCity(city);
      if (addressList != null && addressList.size() == 0) {
        throw new EntityNotFoundException("cannot find the address by city name of " + city);
      }
      return addressList;
    } catch (EntityNotFoundException ex) {
      logger.warn(" Warning: " + ex.getMessage());
      throw ex;
    } catch (Exception e) {
      logger.error("Error: ", e);
      throw e;
    }
  }

  @GetMapping("address/all")
  public List<Address> getAddressAll() {
    return addresssRepository.findAll();
  }

  @GetMapping("address/{zipCode}")
  public List<Address> getAddressByZipCode(
      @PathVariable(name = "zipCode")
          @NotEmpty
          @Pattern(
              regexp = "^[0-9]{5}(?:-[0-9]{4})?$",
              message = "Zip code has to be 5 digits or 9 digits with hyphen")
          String zipCode) {
    try {
      List<Address> addressList = addresssRepository.findByZipCode(zipCode);
      if (addressList != null && addressList.size() == 0) {
        throw new EntityNotFoundException("cannot find address by zip code of " + zipCode);
      }
      return addressList;
    } catch (EntityNotFoundException ex) {
      logger.warn(" Warning: " + ex.getMessage());
      throw ex;
    } catch (Exception e) {
      logger.error("Error: ", e);
      throw e;
    }
  }

  @GetMapping("address/lastName")
  public List<Address> getAddressByLastName(
      @RequestParam(name = "lastName") @NotEmpty String lastName) {
    try {
      List<Address> addressList = addresssRepository.findByLastname(lastName);
      if (addressList != null && addressList.size() == 0) {
        throw new EntityNotFoundException("cannot find the address by last name of " + lastName);
      }
      return addressList;
    } catch (EntityNotFoundException ex) {
      logger.warn(" Warning: " + ex.getMessage());
      throw ex;
    } catch (Exception e) {
      logger.error("Error: ", e);
      throw e;
    }
  }

  @GetMapping("address/lastnameAndFirstname")
  public List<Address> getAddressByLastNameAndFirstName(
      @RequestParam(name = "lastName") @NotEmpty String lastName,
      @RequestParam(name = "firstName") @NotEmpty String firstName) {
    try {
      List<Address> addressList =
          addresssRepository.findByLastnameAndFirstname(lastName, firstName);
      if (addressList != null && addressList.size() == 0) {
        throw new EntityNotFoundException(
            "cannot find the address by last name and first name of " + lastName + "," + firstName);
      }
      return addressList;
    } catch (EntityNotFoundException ex) {
      logger.warn(" Warning: " + ex.getMessage());
      throw ex;
    } catch (Exception e) {
      logger.error("Error: ", e);
      throw e;
    }
  }

  @PutMapping("address/lastnameAndFirstname")
  public List<Address> updateAddressByLastNameAndFirstName(
      @RequestParam(name = "lastName") @NotEmpty String lastName,
      @RequestParam(name = "firstName") @NotEmpty String firstName,
      @Valid @RequestBody Address newAddress) {
    try {
      List<Address> addressList =
          addresssRepository.findByLastnameAndFirstname(lastName, firstName);
      if (addressList != null && addressList.size() == 0) {
        throw new EntityNotFoundException(
            "cannot find the updating address by last name and first name of "
                + lastName
                + ","
                + firstName);
      }
      return addressList
          .stream()
          .map(
              address -> {
                updateExistingAddress(newAddress, address);
                addresssRepository.save(address);
                return address;
              })
          .collect(Collectors.toList());
    } catch (EntityNotFoundException ex) {
      logger.warn(" Warning: " + ex.getMessage());
      throw ex;
    } catch (Exception e) {
      logger.error("Error: ", e);
      throw e;
    }
  }

  private void updateExistingAddress(Address newAddress, Address address) {
    address.setFirstname(newAddress.getFirstname());
    address.setLastname(newAddress.getLastname());
    address.setMiddlename(newAddress.getMiddlename());
    address.setAddressLine1(newAddress.getAddressLine1());
    address.setAddressLine2(newAddress.getAddressLine2());
    address.setCity(newAddress.getCity());
    address.setState(newAddress.getState());
    address.setZipCode(newAddress.getZipCode());
  }

  @PostMapping("address")
  public Address insertAddress(@Valid @RequestBody Address newAddress) {
    try {
      return addresssRepository.save(newAddress);
    } catch (Exception e) {
      logger.error("Error: ", e);
      throw e;
    }
  }

  @DeleteMapping("address/lastnameAndFirstname")
  public List<Address> deleteAddressByLastNameAndFirstName(
      @RequestParam(name = "lastName") @NotEmpty String lastName,
      @RequestParam(name = "firstName") @NotEmpty String firstName) {
    try {
      List<Address> addressList =
          addresssRepository.findByLastnameAndFirstname(lastName, firstName);
      if (addressList != null && addressList.size() == 0) {
        throw new EntityNotFoundException(
            "cannot find the deleting address by last name and first name of "
                + lastName
                + ","
                + firstName);
      }
      return addressList
          .stream()
          .map(
              address -> {
                addresssRepository.delete(address);
                return address;
              })
          .collect(Collectors.toList());
    } catch (EntityNotFoundException ex) {
      logger.warn(" Warning: " + ex.getMessage());
      throw ex;
    } catch (Exception e) {
      logger.error("Error: ", e);
      throw e;
    }
  }
}
