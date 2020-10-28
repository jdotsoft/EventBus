package com.jdotsoft.eventbus.demo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import com.jdotsoft.eventbus.EventBus;
import com.jdotsoft.eventbus.EventLogListener;
import com.jdotsoft.eventbus.demo.events.BlueEvent;
import com.jdotsoft.eventbus.demo.events.RedEvent;
import com.jdotsoft.eventbus.demo.events.ResetEvent;
import com.jdotsoft.eventbus.demo.panels.BluePanel;
import com.jdotsoft.eventbus.demo.panels.BlueRedPanel;
import com.jdotsoft.eventbus.demo.panels.RedPanel;

public class EventBusDemo extends JFrame {
  private final static int DEFAILT_WIDTH = 500;
  private final static int DEFAILT_HEIGHT = 300;
  private final static Color STD_RED  = new Color(255, 175, 175);
  public  final static Color STD_BLUE = new Color(175, 175, 255);

  public EventBusDemo() {
    super("EventBusDemo");
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception e) {
      // Ignore: ClassNotFoundException, InstantiationException
      // IllegalAccessException, UnsupportedLookAndFeelException
    }
    setBounds(100, 100, 300, 300);
    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    });

    int nWidth = DEFAILT_WIDTH;
    int nHeight = DEFAILT_HEIGHT;
    Dimension dmScreen = Toolkit.getDefaultToolkit().getScreenSize();
    int nW = Math.min(nWidth, dmScreen.width);
    int nH = Math.min(nHeight, dmScreen.height - 20); // 20 for taskbar
    int nX = (dmScreen.width - nW) / 2;
    int nY = (dmScreen.height - nH) / 2;
    setBounds(nX, nY, nW, nH);
    initGUI();
  }

  private void initGUI() {
    EventBus eventBus = EventBus.getInstance()
                                .setEventLogListener(new EventLogListener());
    JPanel panelMain = new JPanel();
    panelMain.setLayout(new BoxLayout(panelMain, BoxLayout.Y_AXIS));
    panelMain.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10)); // T,L,B,R

    JPanel panelBtn = new JPanel();
    JButton btnRed = new JButton("Red");
    btnRed.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        eventBus.fireEvent(new RedEvent(STD_RED));
      }
    });
    panelBtn.add(btnRed);

    JButton btnBlue = new JButton("Blue");
    btnBlue.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        eventBus.fireEvent(new BlueEvent());
      }
    });
    panelBtn.add(btnBlue);

    JButton btnReset = new JButton("Reset");
    btnReset.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        eventBus.fireEvent(new ResetEvent());
      }
    });
    panelBtn.add(btnReset);

    panelMain.add(panelBtn);
    panelMain.add(new RedPanel(eventBus));
    panelMain.add(new BluePanel(eventBus));
    panelMain.add(new BlueRedPanel(eventBus));
    panelMain.add(Box.createVerticalStrut(1000));

    // Set content
    Container cont = this.getContentPane();
    cont.setLayout(new BorderLayout());
    cont.add(panelMain, BorderLayout.CENTER);

    System.out.println(eventBus);
  }

  public static void main(String[] args) {
    EventBusDemo app = new EventBusDemo();
    app.setVisible(true);
  }

}
