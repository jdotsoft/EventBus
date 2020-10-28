package com.jdotsoft.eventbus.demo.panels;

import com.jdotsoft.eventbus.EventBus;
import com.jdotsoft.eventbus.demo.EventBusDemo;
import com.jdotsoft.eventbus.demo.events.BlueEvent;
import com.jdotsoft.eventbus.demo.events.BlueEventListener;
import com.jdotsoft.eventbus.demo.events.RedEvent;
import com.jdotsoft.eventbus.demo.events.RedEventListener;

public class BlueRedPanel extends AbstractPanel implements RedEventListener, BlueEventListener {

  public BlueRedPanel(EventBus eventBus) {
    super(eventBus, "RED & BLUE");
    eventBus.registerListeners(this);
  }

  @Override
  public void onRed(RedEvent event) {
    setBackground(event.getColor());
  }

  @Override
  public void onBlue(BlueEvent event) {
    setBackground(EventBusDemo.STD_BLUE);
  }

}
