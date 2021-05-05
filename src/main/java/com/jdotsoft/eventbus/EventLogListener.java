/*
 * Copyright (C) 2021 JDotSoft. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jdotsoft.eventbus;

import java.lang.reflect.Method;

/**
 * Use this class to log fired and dispatched events in {@link EventBus}.
 * Extend this class to use the application logging system instead of default System.out.println()
 */
public class EventLogListener {

  /**
   * This method is called when this class is registered as listener and event is fired.
   * Override this method in derived class to use the application logging system.
   *
   * @param e event fired
   */
  @SuppressWarnings("static-method")
  public void onEventFired(Event e) {
    System.out.println(makeLogString(e));
  }

  /**
   * This method is called when this class is registered as listener and event is dispatched.
   * Override this method in derived class to use the application logging system.
   *
   * @param e event dispatched
   * @param l listener which processes the event
   * @param m the listener method which processes the event
   */
  @SuppressWarnings("static-method")
  public void onEventDispatched(Event e, EventListener l, Method m) {
    System.out.println(makeLogString(e, l, m));
  }

  protected static String makeLogString(Event e) {
    return String.format("EventBus: fired '%s'", getClassName(e));
  }

  protected static String makeLogString(Event e, EventListener l, Method m) {
    return String.format("EventBus: dispatched '%s' to %s@%d->%s()",
        getClassName(e), getClassName(l), System.identityHashCode(l), m.getName());
  }

  protected static String getClassName(Object obj) {
    Class<?> c = obj.getClass();
    String s = c.getSimpleName(); // could be empty for: eventBus.fireEvent(new Event() {..});
    return s.isEmpty() ? c.getName() : s;
  }
  
}
