package com.jdotsoft.eventbus.demo.panels;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.jdotsoft.eventbus.EventBus;
import com.jdotsoft.eventbus.demo.events.ResetEvent;
import com.jdotsoft.eventbus.demo.events.ResetEventListener;

public class AbstractPanel extends JPanel implements ResetEventListener {

  protected EventBus eventBus;
  private Color defaultBackground;

  public AbstractPanel(EventBus eventBus, String label) {
    this.eventBus = eventBus;
    this.defaultBackground = getBackground();
    add(new JLabel(label + " event listener"));
    setBorder(BorderFactory.createEtchedBorder());
    setMinimumSize(new Dimension(10, 75));
  }

  @Override
  public void onReset(ResetEvent event) {
    setBackground(defaultBackground);
  }

}
