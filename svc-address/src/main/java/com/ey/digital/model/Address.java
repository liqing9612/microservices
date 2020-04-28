package com.ey.digital.model;

import java.io.Serializable;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Address")
public class Address implements Serializable {
  @Id private String id;

  @NotEmpty(message = "First name cannot be empty")
  private String firstname;

  private String middlename;

  @NotEmpty(message = "Last name cannot be empty")
  private String lastname;

  @NotEmpty(message = "Address Line 1 cannot be empty")
  private String addressLine1;

  private String addressLine2;

  @NotEmpty(message = "City name cannot be empty")
  private String city;

  @NotEmpty(message = "State abbreviation name cannot be empty")
  @Pattern(
      regexp =
          "^(?-i:A[LKSZRAEP]|C[AOT]|D[EC]|F[LM]|G[AU]|HI|I[ADLN]|K[SY]|LA|M[ADEHINOPST]|N[CDEHJMVY]|O[HKR]|P[ARW]|RI|S[CD]|T[NX]|UT|V[AIT]|W[AIVY])$",
      message = "U.S state abbreviation name has to be valid in 2 capital letters")
  private String state;

  @NotEmpty(message = "Zip code cannot be empty")
  @Pattern(
      regexp = "^[0-9]{5}(?:-[0-9]{4})?$",
      message = "Zip code has to be 5 digits or 9 digits with hyphen")
  @Indexed
  private String zipCode;

  public Address() {}

  public String getAddressLine1() {
    return addressLine1;
  }

  public void setAddressLine1(String addressLine1) {
    this.addressLine1 = addressLine1;
  }

  public String getAddressLine2() {
    return addressLine2;
  }

  public void setAddressLine2(String addressLine2) {
    this.addressLine2 = addressLine2;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getZipCode() {
    return zipCode;
  }

  public void setZipCode(String zipCode) {
    this.zipCode = zipCode;
  }

  public String getFirstname() {
    return firstname;
  }

  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  public String getMiddlename() {
    return middlename;
  }

  public void setMiddlename(String middlename) {
    this.middlename = middlename;
  }

  public String getLastname() {
    return lastname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
  }

  public String getId() {
    return id;
  }

  @Override
  public String toString() {
    return "Address{"
        + "firstname='"
        + firstname
        + '\''
        + ", middlename='"
        + middlename
        + '\''
        + ", lastname='"
        + lastname
        + '\''
        + ", addressLine1='"
        + addressLine1
        + '\''
        + ", addressLine2='"
        + addressLine2
        + '\''
        + ", city='"
        + city
        + '\''
        + ", state='"
        + state
        + '\''
        + ", zipCode='"
        + zipCode
        + '\''
        + '}';
  }
}
