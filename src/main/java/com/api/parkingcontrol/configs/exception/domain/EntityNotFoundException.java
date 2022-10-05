package com.api.parkingcontrol.configs.exception.domain;

public class EntityNotFoundException extends DomainException {
  public EntityNotFoundException(String message) {
    super(message);
  }
}
