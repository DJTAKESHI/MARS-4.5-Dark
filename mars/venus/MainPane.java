package mars.venus;

import mars.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.undo.*;
import java.text.*;
import java.util.*;
import java.io.*;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

/*
Copyright (c) 2003-2006,  Pete Sanderson and Kenneth Vollmar

Developed by Pete Sanderson (psanderson@otterbein.edu)
and Kenneth Vollmar (kenvollmar@missouristate.edu)

Permission is hereby granted, free of charge, to any person obtaining 
a copy of this software and associated documentation files (the 
"Software"), to deal in the Software without restriction, including 
without limitation the rights to use, copy, modify, merge, publish, 
distribute, sublicense, and/or sell copies of the Software, and to 
permit persons to whom the Software is furnished to do so, subject 
to the following conditions:

The above copyright notice and this permission notice shall be 
included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, 
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF 
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. 
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR 
ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF 
CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION 
WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

(MIT license, http://www.opensource.org/licenses/mit-license.html)
*/

/**
 * Creates the tabbed areas in the UI and also created the internal windows that
 * exist in them.
 * 
 * @author Sanderson and Bumgarner
 **/

public class MainPane extends JTabbedPane {
   EditPane editTab;
   ExecutePane executeTab;
   EditTabbedPane editTabbedPane;

   private VenusUI mainUI;

   /**
    * Constructor for the MainPane class.
    **/

   public MainPane(VenusUI appFrame, Editor editor, RegistersWindow regs,
         Coprocessor1Window cop1Regs, Coprocessor0Window cop0Regs) {
      super();

      System.out.println("Look and Feel: "
            + UIManager.getLookAndFeel().getClass().getName());

      System.out.println("MainPane UI: "
            + this.getUI().getClass().getName());

      // Dark theme for Edit / Execute tabs
      javax.swing.UIManager.put(
            "TabbedPane.selected",
            new Color(0x37373D));
      javax.swing.UIManager.put(
            "TabbedPane.foreground",
            new Color(0xD4D4D4));

      this.setBackground(new Color(0x1E1E1E));
      this.setForeground(new Color(0xD4D4D4));
      this.setOpaque(true);

      // Customize Metal tab colors
      UIManager.put("TabbedPane.contentAreaColor", new Color(0x1E1E1E));
      UIManager.put("TabbedPane.selected", new Color(0x37373D));
      UIManager.put("TabbedPane.unselectedBackground", new Color(0x252526));
      UIManager.put("TabbedPane.selectHighlight", new Color(0x37373D));
      UIManager.put("TabbedPane.darkShadow", new Color(0x3C3C3C));
      UIManager.put("TabbedPane.shadow", new Color(0x3C3C3C));

      // Recreate UI delegate using updated colors
      // this.setUI(new javax.swing.plaf.metal.MetalTabbedPaneUI() {
      // @Override
      // protected void paintContentBorder(
      // java.awt.Graphics g,
      // int tabPlacement,
      // int selectedIndex) {
      // // Remove Metal's default content border
      // }
      // });

      this.mainUI = appFrame;
      this.setTabPlacement(JTabbedPane.TOP); // LEFT);
      if (this.getUI() instanceof BasicTabbedPaneUI) {
         BasicTabbedPaneUI ui = (BasicTabbedPaneUI) this.getUI();
      }
      editTabbedPane = new EditTabbedPane(appFrame, editor, this);
      // editTabbedPane.setVisible(false);
      executeTab = new ExecutePane(appFrame, regs, cop1Regs, cop0Regs);
      String editTabTitle = "Edit"; // "<html><center>&nbsp;<br>E<br>d<br>i<br>t<br>&nbsp;</center></html>";
      String executeTabTitle = "Execute"; // "<html><center>&nbsp;<br>E<br>x<br>e<br>c<br>u<br>t<br>e<br>&nbsp;</center></html>";
      Icon editTabIcon = null;// new
                              // ImageIcon(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource(Globals.imagesPath+"Edit_tab.jpg")));
      Icon executeTabIcon = null;// new
                                 // ImageIcon(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource(Globals.imagesPath+"Execute_tab.jpg")));

      this.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
      this.addTab(editTabTitle, editTabIcon, editTabbedPane);

      System.out.println("MainPane background: " + this.getBackground());
      System.out.println("EditTabbedPane background: " + editTabbedPane.getBackground());
      System.out.println("MainPane opaque: " + this.isOpaque());
      System.out.println("EditTabbedPane opaque: " + editTabbedPane.isOpaque());

      System.out.println("TabbedPane.background: "
            + UIManager.getColor("TabbedPane.background"));
      System.out.println("TabbedPane.contentAreaColor: "
            + UIManager.getColor("TabbedPane.contentAreaColor"));

      javax.swing.SwingUtilities.invokeLater(() -> {
         System.out.println("=== MainPane children ===");

         for (Component c : this.getComponents()) {
            System.out.println(
                  c.getClass().getName()
                        + " | background=" + c.getBackground()
                        + " | visible=" + c.isVisible()
                        + " | bounds=" + c.getBounds());
         }

         System.out.println("=== EditTabbedPane children ===");

         for (Component c : editTabbedPane.getComponents()) {
            System.out.println(
                  c.getClass().getName()
                        + " | background=" + c.getBackground()
                        + " | visible=" + c.isVisible()
                        + " | bounds=" + c.getBounds());
         }
      });

      // this.addTab("<html><center>&nbsp;<br>P<br>r<br>o<br>j<br>&nbsp;<br>1<br&nbsp;</center></html>",
      // null, new JTabbedPane());
      // this.addTab("<html><center>&nbsp;<br>P<br>r<br>o<br>j<br>&nbsp;<br>2<br&nbsp;</center></html>",
      // null, new JTabbedPane());
      // this.addTab("<html><center>&nbsp;<br>P<br>r<br>o<br>j<br>&nbsp;<br>3<br&nbsp;</center></html>",
      // null, new JTabbedPane());
      // this.addTab("<html><center>&nbsp;<br>P<br>r<br>o<br>j<br>&nbsp;<br>4<br&nbsp;</center></html>",
      // null, new JTabbedPane());

      this.addTab(executeTabTitle, executeTabIcon, executeTab);

      this.setToolTipTextAt(0, "Text editor for composing MIPS programs.");
      this.setToolTipTextAt(1,
            "View and control assembly language program execution.  Enabled upon successful assemble.");

      /*
       * Listener has one specific purpose: when Execute tab is selected for the
       * first time, set the bounds of its internal frames by invoking the
       * setWindowsBounds() method. Once this occurs, listener removes itself!
       * We do NOT want to reset bounds each time Execute tab is selected.
       * See ExecutePane.setWindowsBounds documentation for more details.
       */
      this.addChangeListener(
            new ChangeListener() {
               public void stateChanged(ChangeEvent ce) {
                  JTabbedPane tabbedPane = (JTabbedPane) ce.getSource();
                  int index = tabbedPane.getSelectedIndex();
                  Component c = tabbedPane.getComponentAt(index);
                  ExecutePane executePane = Globals.getGui().getMainPane().getExecutePane();
                  if (c == executePane) {
                     executePane.setWindowBounds();
                     Globals.getGui().getMainPane().removeChangeListener(this);
                  }
               }
            });
   }

   /**
    * Returns current edit pane. Implementation changed for MARS 4.0 support
    * for multiple panes, but specification is same.
    *
    * @return the editor pane
    */
   public EditPane getEditPane() {
      return editTabbedPane.getCurrentEditTab();
   }

   /**
    * Returns component containing editor display
    *
    * @return the editor tabbed pane
    */
   public JComponent getEditTabbedPane() {
      return editTabbedPane;
   }

   /**
    * returns component containing execution-time display
    *
    * @return the execute pane
    */
   public ExecutePane getExecutePane() {
      return executeTab;
   }

   /**
    * returns component containing execution-time display.
    * Same as getExecutePane().
    *
    * @return the execute pane
    */
   public ExecutePane getExecuteTab() {
      return executeTab;
   }

}