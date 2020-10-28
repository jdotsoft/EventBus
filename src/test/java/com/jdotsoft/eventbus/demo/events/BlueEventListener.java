package com.jdotsoft.eventbus.demo.events;

import com.jdotsoft.eventbus.EventListener;

public interface BlueEventListener extends EventListener {

  void onBlue(BlueEvent event);

}
