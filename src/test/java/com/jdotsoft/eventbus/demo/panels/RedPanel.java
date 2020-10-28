package com.jdotsoft.eventbus.demo.panels;

import com.jdotsoft.eventbus.EventBus;
import com.jdotsoft.eventbus.demo.events.RedEvent;
import com.jdotsoft.eventbus.demo.events.RedEventListener;

public class RedPanel extends AbstractPanel implements RedEventListener, Cloneable {

  public RedPanel(EventBus eventBus) {
    super(eventBus, "RED");
    eventBus.registerListeners(this);
  }

  @Override
  public void onRed(RedEvent event) {
    setBackground(event.getColor());
  }

}
