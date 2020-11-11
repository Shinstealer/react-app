package com.shinstealer.spring.webflux.react_app.event;

import org.springframework.context.ApplicationEvent;

public class ProfileCreatedEvent extends ApplicationEvent {


  private static final long serialVersionUID = -48756745161L;

  public ProfileCreatedEvent(Object source) {
    super(source);

  }

}
