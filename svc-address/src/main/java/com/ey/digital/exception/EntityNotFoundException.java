package com.ey.digital.exception;

public class EntityNotFoundException extends RuntimeException {
  private static final long serialVersionUID = 3421282698628842256L;

  private String message;

  public EntityNotFoundException(String errMsg) {
    super();
    this.message = errMsg;
  }

  public EntityNotFoundException(Exception ex) {
    super();
    this.message = ex.getMessage();
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
