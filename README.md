# EventBus
Simple Java EventBus.

Follow steps to start using `EventBus`.
These steps are *by example* used in a Swing demo application in this project located in `test -> com.jdotsoft.eventbus.demo.EventBusDemo`
See also http://www.jdotsoft.com/EventBus.php

**Step 1.** Create `BlueEvent` class which extends base class `Event`.
This class might have constructor with arguments, member variables, and any methods.

    public class BlueEvent extends Event {
    }

**Step 2.** Create `BlueEventListener` interface which extends base interface `EventListener` with method `onBlue(BlueEvent e)`.
This interface might have any methods declarations, however, only method which returns `void` with single argument `BlueEvent` will be invoked. 

    public interface BlueEventListener extends EventListener {
       void onBlue(BlueEvent event);
    }

**Step 3.** Implement `BlueEventListener` (and other event listeners if required) in some class which will receive the `BlueEvent` event and register it:
    
    public class BluePanel extends JPanel implements BlueEventListener {
      public BluePanel() {
        EventBus.getInstance().registerListeners(this);
      }
      @Override
      public void onBlue(BlueEvent event) {
        . . .
      }
    }

**Step 4.** Fire event

    eventBus.fire(new BlueEvent());
    - or -
    EventBus.getInstance().fire(new BlueEvent());

**Step 5.** Object `BluePanel` will receive event in listener `BlueEventListener` implementation `onBlue(BlueEvent e)`.


`EventBus` object could be instantiated as `EventBus eventBus = new EventBus();` in some class and passed to listeners.
Use static `EventBus.getInstance();` as an alternative.

    

