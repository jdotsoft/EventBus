package com.jdotsoft.eventbus.demo.panels;

import com.jdotsoft.eventbus.EventBus;
import com.jdotsoft.eventbus.demo.EventBusDemo;
import com.jdotsoft.eventbus.demo.events.BlueEvent;
import com.jdotsoft.eventbus.demo.events.BlueEventListener;

public class BluePanel extends AbstractPanel implements BlueEventListener {

  public BluePanel(EventBus eventBus) {
    super(eventBus, "BLUE");
    eventBus.registerListeners(this);
  }

  @Override
  public void onBlue(BlueEvent event) {
    setBackground(EventBusDemo.STD_BLUE);
  }

}
