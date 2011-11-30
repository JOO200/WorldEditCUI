package wecui.event;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import wecui.fevents.Listener;
import wecui.WorldEditCUI;
import wecui.event.cui.CUIBaseEvent;
import wecui.event.cui.CUIEventType;

/**
 * Listener class for CUIEvent
 * 
 * @author lahwran
 * @author yetanotherx
 * 
 */
public class CUIListener implements Listener<CUIEvent> {

    protected WorldEditCUI controller;

    public CUIListener(WorldEditCUI controller) {
        this.controller = controller;
    }

    public void onEvent(CUIEvent event) {
        try {
            CUIEventType eventType = CUIEventType.getTypeFromKey(event.type);
            if( eventType == null ) {
                event.markInvalid("Unknown CUIEvent identifier.");
            }
            
            Constructor[] constructors = eventType.getEventClass().getDeclaredConstructors();
            if (constructors == null || constructors.length == 0) {
                return;
            }
            CUIBaseEvent newEvent = (CUIBaseEvent) constructors[0].newInstance(this.controller, event.params);

            String result = newEvent.doRun();
            if (result != null) {
                event.markInvalid(result);
            } else {
                event.setHandled(true);
            }

        } catch (InstantiationException ex) {
        } catch (IllegalAccessException ex) {
        } catch (IllegalArgumentException ex) {
        } catch (InvocationTargetException ex) {
        }
    }
}