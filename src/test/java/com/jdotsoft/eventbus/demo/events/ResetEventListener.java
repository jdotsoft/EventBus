package com.jdotsoft.eventbus.demo.events;

import com.jdotsoft.eventbus.EventListener;

public interface ResetEventListener extends EventListener {

  void onReset(ResetEvent event);

}
