package com.jdotsoft.eventbus.demo.events;

import java.awt.Color;

import com.jdotsoft.eventbus.Event;

public class RedEvent implements Event {

  private Color color;

  public RedEvent(Color color) {
    this.color = color;
  }

  public Color getColor() {
    return color;
  }

}
