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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * EventBus class.
 */
public class EventBus {

  // Optional static instance. Used by EventBus.getInstance()
  private static EventBus eventBus;

  private Map<Class<? extends Event>, EventInfo> map = new HashMap<>();
  private EventLogListener logListener; // no logging if null

  /**
   * Call this method to create a singleton static instance.
   * Alternative is to create an instance in the calling class and pass it to different application components.
   * 
   * @return instance of the class.
   */
  public static synchronized EventBus getInstance() {
    if (eventBus == null) {
      eventBus = new EventBus();
    }
    return eventBus;
  }

  /**
   * Use this method to enable logging. The default value is null, which disables events logging.
   *
   * @param logListener instance of the {@link EventListener} class.
   * @return this object for method chaining.
   */
  public EventBus setEventLogListener(EventLogListener logListener) {
    this.logListener = logListener;
    return this;
  }

  /**
   * Call this method to register event listeners.
   *
   * @param listenerImpl object implementing one or more interfaces which extend {@link EventListener}.
   */
  public void registerListeners(EventListener listenerImpl) {
    // Process interfaces which extend interface EventListener
    for (Class<? extends EventListener> cEventListener : getEventListenerInterfaces(listenerImpl.getClass())) {
      EventInfo info = findOrCreateEventInfo(cEventListener);
      info.hsListenerImpl.add(listenerImpl); // register EventListener listenerImpl
    }
  }

  /**
   * Call this method to unregister event listeners.
   * @param listenerImpl object implementing one or more interfaces extending {@link EventListener}.
   */
  public void unregisterListeners(EventListener listenerImpl) {
    for (EventInfo i : map.values()) {
      if (i.hsListenerImpl.contains(listenerImpl)) {
        i.hsListenerImpl.remove(listenerImpl);
      }
    }
  }

  /**
   * Fire event.
   * @param event event to fire.
   */
  public void fireEvent(Event event) {
    if (logListener != null) {
      logListener.onEventFired(event);
    }
    EventInfo info = map.get(event.getClass());
    if (info == null) {
      throw new EventBusException(String.format(
          "Event %s does not have registered listener", event.getClass().getName()));
    }
    for (EventListener listener : info.hsListenerImpl) {
      if (logListener != null) {
        logListener.onEventDispatched(event, listener, info.method);
      }
      try {
        info.method.invoke(listener, event);
      } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
        throw new EventBusException(
            String.format("Failed to dispatch event %s@%d to listener %s@%d",
                event.getClass().getName(), System.identityHashCode(listener),
                listener.getClass().getName(), System.identityHashCode(listener)), e);
      }
    }
  }

  private static Set<Class<?>> getInterfacesRecursive(Class<?> cArg) {
    Set<Class<?>> hs = new HashSet<>();
    for (Class<?> c :  cArg.getInterfaces()) {
      hs.add(c);
      hs.addAll(getInterfacesRecursive(c));
      Class<?> cSuper = cArg.getSuperclass();
      if (cSuper != null) {
        hs.addAll(getInterfacesRecursive(cSuper));
      }
    }
    return hs;
  }

  @SuppressWarnings("unchecked")
  private static Set<Class<? extends EventListener>> getEventListenerInterfaces(Class<? extends EventListener> cListener) {
    Set<Class<? extends EventListener>> hs = new HashSet<>();
    for (Class<?> cInterface :  getInterfacesRecursive(cListener)) {
      Class<?>[] allSuper = cInterface.getInterfaces();
      if (allSuper.length == 0) {
        continue; // does not have super interfaces, will process if has EventListener super interface
      }
      for (Class<?> cSuper : allSuper) {
        if (cSuper != EventListener.class) {
          continue; // this super interfaces is not EventListener super interface
        }
      }
      // Collect this cInterface because it has super (or super.super...) interface EventListener:
      hs.add((Class<? extends EventListener>)cInterface); // cast is required because cInterface is Class<?>
    }
    return hs;
  }

  /**
   * Finds {@link EventInfo} by {@link EventListener} in the internal collection or creates new one
   * and adds it to internal collection.
   *
   * @param cEventListener class {@link EventListener}.
   * @return {@link EventInfo}.
   */
  private EventInfo findOrCreateEventInfo(Class<? extends EventListener> cEventListener) {
    EventInfo info = createEventInfo(cEventListener);
    for (EventInfo i : map.values()) {
      if (i.cEvent.equals(info.cEvent)) {
        if (i.cEventListener.equals(info.cEventListener) && i.method.equals(info.method)) {
          return i;
        }
        throw new EventBusException(String.format(
            "Event %s has not unique listener: %s and %s",
            i.cEvent.getName(), i.cEventListener.getName(), info.cEventListener.getName()));
      }
    }
    map.put(info.cEvent, info);
    return info;
  }

  @SuppressWarnings("unchecked")
  private static EventInfo createEventInfo(Class<? extends EventListener> cEventListener) {
    EventInfo info = new EventInfo();
    info.cEventListener = cEventListener;
    // Extract EventListener method
    Method[] mAll = cEventListener.getMethods();
    if (mAll.length != 1) {
      throw new EventBusException(String.format(
          "Event listener %s must have a single method", cEventListener));
    }
    info.method = mAll[0];
    if (info.method.getReturnType() != Void.TYPE) {
      throw new EventBusException(String.format(
          "Event listener %s must return void", cEventListener));
    }
    // Extract Event class
    Class<?>[] cParamAll = info.method.getParameterTypes();
    if (cParamAll.length != 1) {
      throw new EventBusException(String.format(
          "Event listener %s.%s() method should have a single argument 'event'",
          cEventListener.getName(), info.method.getName()));
    }
    Class<?> cParam = cParamAll[0]; // class com.jdotsoft.eventbus.demo.events.RedEvent
    for (Class<?> cInterface :  getInterfacesRecursive(cParam)) {
      if (cInterface == Event.class) {
        info.cEvent = (Class<? extends Event>)cParam; // cast is required because cParam is Class<?>
        break;
      }
    }    
    if (info.cEvent == null) {
      throw new EventBusException(String.format(
          "Event listener %s.%s() method should have a single argument of type %s",
          cEventListener.getName(), info.method.getName(), Event.class.getName()));
    }
    return info;
  }

  /**
   * Use this method to debug registered listeners.
   * @return string to log.
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("EventBus: " + getClass().getName() +  "@" + System.identityHashCode(this) + "\n");
    for (EventInfo i : map.values()) {
      sb.append("  EVENT: " + i.cEvent.getName() + "\n");
      for (EventListener listener : i.hsListenerImpl) {
        sb.append("    LISTENER: " + listener.getClass().getName() + "@" + System.identityHashCode(listener) + "\n");
      }
    }
    return sb.toString();
  }

  private static class EventInfo {
    Class<? extends Event> cEvent;
    Class<? extends EventListener> cEventListener;
    Method method;
    Set<EventListener> hsListenerImpl = new HashSet<>();
  }

}
