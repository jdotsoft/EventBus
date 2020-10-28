package com.jdotsoft.eventbus;

public class EventBusException extends RuntimeException {

  public EventBusException(String message) {
    super(message);
  }

  public EventBusException(String message, Throwable cause) {
    super(message, cause);
  }

}
