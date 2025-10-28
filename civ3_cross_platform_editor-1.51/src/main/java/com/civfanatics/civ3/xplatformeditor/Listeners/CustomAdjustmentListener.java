
package com.civfanatics.civ3.xplatformeditor.Listeners;

import com.civfanatics.civ3.xplatformeditor.MapPanel;
import java.awt.Adjustable;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

/**
 * Implements a custom scroll listener for use with the MapPanel.  It calls
 * methods in the map panel when the scroll bar is adjusted, so that the
 * map panel redraws the screen to show the new area.  This is necessary as
 * the map panel actively renders the screen, and does not do so when not
 * necessary so as not to burn CPU cycles.
 * 
 * @author Andrew
 */
public class CustomAdjustmentListener implements AdjustmentListener {

    boolean already = false;
    MapPanel map;
    public CustomAdjustmentListener(MapPanel map)
    {
        this.map = map;
    }
    
    /**
     * This method is called whenever the value of a scrollbar is changed,
     * either by the user or programmatically.
     */
    public void adjustmentValueChanged(AdjustmentEvent evt) {
        int oldPos = map.getVertPosition();
        Adjustable source = evt.getAdjustable();

        // getValueIsAdjusting() returns true if the user is currently
        // dragging the scrollbar's knob and has not picked a final value
        if (evt.getValueIsAdjusting()) {
            // The user is dragging the knob
            //return;
        }

        // Determine which scrollbar fired the event
        int orient = source.getOrientation();
        boolean horiz = true;
        if (orient == Adjustable.HORIZONTAL) {
            horiz = true;
        } else {
            horiz = false;
        }

        // Determine the type of event
        int type = evt.getAdjustmentType();
        switch (type) {
          case AdjustmentEvent.UNIT_INCREMENT:
              // Scrollbar was increased by one unit
              break;
          case AdjustmentEvent.UNIT_DECREMENT:
              // Scrollbar was decreased by one unit
              break;
          case AdjustmentEvent.BLOCK_INCREMENT:
              // Scrollbar was increased by one block
              break;
          case AdjustmentEvent.BLOCK_DECREMENT:
              // Scrollbar was decreased by one block
              break;
          case AdjustmentEvent.TRACK:
              // The knob on the scrollbar was dragged
              break;
        }
        if (horiz)
            map.receiveHorizPosition(evt.getValue());
        else
            map.receiveVertPosition(evt.getValue());

        int value = evt.getValue() - oldPos;
        
        // Get current value
        if (!already)
        {
            //JOptionPane.showMessageDialog(null,"Value: " + value);
            //already = true;
        }
    }
}
