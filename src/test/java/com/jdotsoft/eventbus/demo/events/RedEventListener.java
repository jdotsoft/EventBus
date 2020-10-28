package com.jdotsoft.eventbus.demo.events;

import com.jdotsoft.eventbus.EventListener;

public interface RedEventListener extends EventListener {

  void onRed(RedEvent event);

}
