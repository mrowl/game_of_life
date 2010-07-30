/**
 * This class is the panel in which everything is done.  It holds three
 * panels, one which houses the buttons, another which has a slide
 * bar, and a third to draw the board in.  
 * 
 * @author Scott  gtg892r
 * I didn't collaborate on this class.
 * @version 1.0
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.io.*;

public class GamePanel extends JPanel
{
   /** width of the grid */
   public static final int WIDTH = 600;
   
   /** height of the grid */
   public static final int HEIGHT = 600;
   
   /** the width of the sidebar containg buttons */
   public static final int SIDE_WIDTH = 120;
   
   /** the height of the bottom bar containing the slider */
   public static final int BOTTOM_HEIGHT = 80;
   
   /** the width of the border */
   public static final int BORD = 2;
   
   /** the current generation number */
   private int stepNumber = 0;
   
   /** the label showing the size of the grid currently */
   private JLabel sizeCaption;
   
   /** the label showing the current step number */
   private JLabel curStep = new JLabel("Current Step:  0");
   
   /** the text field that sets the delay in ms */
   private JTextField delayEntry = new JTextField("100", 5);
   
   /** the clear button */
   private JButton clear = new JButton("Clear");
   
   /** the step button (activates step mode) */
   private JButton step = new JButton("Step");
   
   /** the run button (activates continuous run mode) */
   private JButton run = new JButton("Run");
   
   /** the stop button (deactivates run mode) */
   private JButton stop = new JButton("Stop");
   
   /** the save button (saves current model) */
   private JButton save = new JButton("Save Model");
   
   /** the load button (loads a saved model) */
   private JButton load = new JButton("Load Model");
   
   /** the slider that changes the grid size from 1 - 100 */
   private JSlider sizeSlider;
   
   /** the timer that holds the delay for run mode */
   private Timer timer;
   
   /** the cell model that is worked on and displayed */
   private CellModel curModel;
   
   /** true if the program is in step mode */
   private boolean stepMode= false;
   
   /** true if the program is in run mode */
   private boolean runMode = false;
   
   /**
    * This constructor creates a new panel with the
    * BorderLayout layout.  It adds three panels to
    * it in the center, west, and south positions.  
    * It also adds the button listeners and initializes
    * a few variables as well as setting the size and
    * background of the panel.
    *
    */
   public GamePanel()
   {
      curModel = new CellModel(20, 20);
      timer  = new Timer(100, new RunListener());
      sizeCaption = new JLabel("");
      
      setLayout(new BorderLayout());
      add(new BoardPanel(), BorderLayout.CENTER);
      add(new SidePanel(), BorderLayout.WEST);
      add(new BottomPanel(), BorderLayout.SOUTH);
      
      clear.addActionListener(new ButtonListener());
      step.addActionListener(new ButtonListener());
      run.addActionListener(new ButtonListener());
      stop.addActionListener(new ButtonListener());
      save.addActionListener(new ButtonListener());
      load.addActionListener(new ButtonListener());
      delayEntry.addActionListener(new ButtonListener());
      
      setBackground(Color.black);
      setPreferredSize(new Dimension(WIDTH + SIDE_WIDTH, HEIGHT + BOTTOM_HEIGHT));
   }//end GamePanel constructor
   
   /**
    * This method loads a model object from
    * a given file.  It sets the slider value properly
    * and also reads in the static variable gridSeparation.
    * @param fName a String representing the name of the file to be loaded
    */
   public void loadModel(String fName)
   {
      try
      {
         FileInputStream fis = new FileInputStream(fName);
         ObjectInputStream in = new ObjectInputStream(fis);
         CellModel temp = (CellModel)in.readObject();
         sizeSlider.setValue(temp.size);
         curModel = temp;
         CellModel.gridSeparation = in.readDouble();
         in.close();
      }//end try
      catch (IOException exception)
      {
         JOptionPane.showMessageDialog(null, "Unforeseen IO error, aborting");
      }//end catch
      catch (ClassNotFoundException exception)
      {
         JOptionPane.showMessageDialog(null, "Invalid Object File");
      }//end catch
   }//end loadModel method
   
   
   /**
    * This inner class is the SidePanel.  It holds all of the buttons
    * as well as the delay entry text field and the label for
    * generation number.
    */
   private class SidePanel extends JPanel
   {
      /** the width of the buttons in the side panel */
      private static final int BUTT_WIDTH = 90;
      
      /**
       * This constructor creates the sidepanel and adds all of the components
       * to it.  It sets the sizes of all the buttons and also sets the margins of a
       * couple of them.  It then sets the background, the border, and the size.
       *
       */
      public SidePanel()
      {
         JLabel delayLabel = new JLabel("Delay Time (ms): ");
         curStep.setForeground(Color.white);
         delayLabel.setForeground(Color.white);
         
         add(clear);
         add(step);
         add(run);
         add(stop);
         add(curStep);
         add(Box.createRigidArea(new Dimension(0, 30)));
         add(delayLabel);
         add(delayEntry);
         add(save);
         add(load);
         
         clear.setPreferredSize(new Dimension(BUTT_WIDTH, 26));
         step.setPreferredSize(new Dimension(BUTT_WIDTH, 26));
         run.setPreferredSize(new Dimension(BUTT_WIDTH, 26));
         stop.setPreferredSize(new Dimension(BUTT_WIDTH, 26));
         save.setPreferredSize(new Dimension(BUTT_WIDTH, 26));
         save.setMargin(new Insets(0, 0, 0, 0));
         load.setPreferredSize(new Dimension(BUTT_WIDTH, 26));
         load.setMargin(new Insets(0, 0, 0, 0));
         
         this.setBorder(BorderFactory.createLineBorder(Color.lightGray, 2));
         setBackground(Color.black);
         setPreferredSize(new Dimension(SIDE_WIDTH, HEIGHT));
      }//end SidePanel constructor
   }//end SidePanel class
   
   
   /**
    * This innrer class draws the board for the cell model.
    * It has a mouselistener to check for clicks, and houses
    * the paintComponent method to draw with.
    * 
    */
   private class BoardPanel extends JPanel
   {
      /**
       * This constructor sets the background color and adds
       * the mouseListener to the panel.
       *
       */
      public BoardPanel()
      {
         setBackground(Color.black);
         addMouseListener(new ClickListener());
         addMouseMotionListener(new DragListener());
      }//end BoardPanel constructor
      
      /**
       * This method overrides the super paintcomponent method
       * and allows the panel to be drawn on.  It sets the size and
       * step number captions every time it is called and then 
       * proceeds to draw the model.
       */
      public void paintComponent (Graphics page)
      {
         super.paintComponent(page);
         
         sizeCaption.setText("Board Size (" + sizeSlider.getValue() + " x " + sizeSlider.getValue() + "): ");
         page.setColor(Color.white);
         curStep.setText("Current Step: " + stepNumber);
         curModel.display(page);
      }//end paintComponent method
   }//end BoardPanel class
   
   
   /**
    * This class is the bottom panel of the application.  It holds
    * the slider and a corresponding label.
    * 
    */
   private class BottomPanel extends JPanel
   {
      /**
       * This constructor creates the panel with flow layout
       * and then adds two components, the slider and its
       * caption.  It sets up the slider properly and adds
       * a change listener to it.
       *
       */
      public BottomPanel()
      {
         sizeSlider = new JSlider(1, 100, 20);
         
         sizeCaption.setForeground(Color.white);
         setBackground(Color.black);
         setPreferredSize(new Dimension(WIDTH+SIDE_WIDTH, BOTTOM_HEIGHT));
         this.setBorder(BorderFactory.createLineBorder(Color.lightGray, 2));
         
         add(sizeCaption);
         add(sizeSlider);
         sizeSlider.setPaintLabels(true);
         sizeSlider.setPaintTicks(true);
         sizeSlider.setMajorTickSpacing(10);
         sizeSlider.addChangeListener(new SliderListener());
      }//end BottomPanel constructor
   }//end BottomPanel class
   
   
   /**
    * This inner class listens for a change in the slider's position.
    */
   private class SliderListener implements ChangeListener
   {
      /**
       * This method creates a new cell model with the
       * new user inputted size.
       */
      public void stateChanged(ChangeEvent e)
      {
         int size = sizeSlider.getValue();
         curModel = new CellModel (size, size);
         repaint();
      }//end stateChanged method
   }//end SliderListener class
   
   
   /**
    * This inner class listens for clicks on all of the buttons
    * in the side panel.
    */
   private class ButtonListener implements ActionListener
   {
      /**
       * This method tests to see which button got clicked and
       * then handles the resulting event.  For clear that means
       * creating a new, empty, cell model.  For step that means
       * entering step mode and doing one turn.  For run that
       * means starting a timer which will continue until stop
       * is pressed.  For the delay entry text box that means
       * setting the timer's delay to the new value.  For save
       * that means opening a file chooser to save the object.
       * For load that means opening a file chooser to load the
       * object.  Only clearing (and sometimes stopping can be 
       * done while either of the modes are active.
       */
      public void actionPerformed(ActionEvent event)
      {
         Object source = event.getSource();
         
         if (source == clear && !timer.isRunning())
         {
            curModel = new CellModel(sizeSlider.getValue(), sizeSlider.getValue());
            stepNumber = 0;
            stepMode = false;
            runMode = false;
            sizeSlider.setEnabled(true);
            delayEntry.setEnabled(true);
         }//end if
         else if (source == step && !runMode)
         {
            curModel.evolve();
            stepMode = true;
            stepNumber++;
            sizeSlider.setEnabled(false);
            delayEntry.setEnabled(false);
         }//end else if
         else if (source == run && !stepMode)
         {
            timer.start();
            runMode = true;
            sizeSlider.setEnabled(false);
            delayEntry.setEnabled(false);
         }//end else if
         else if (source == stop)
         {
            timer.stop();
            delayEntry.setEnabled(true);
         }
         else if (source == delayEntry)
         {
            try
            {
            timer.setDelay(Integer.parseInt(delayEntry.getText()));
            }//end try
            catch (NumberFormatException exception)
            {
               JOptionPane.showMessageDialog(null, "Please enter a valid delay time");
               delayEntry.setText(Integer.toString(timer.getDelay()));
            }//end catch
         }// end else if
         else if (source == save && !timer.isRunning())
         {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogType(JFileChooser.SAVE_DIALOG);
            chooser.setDialogTitle("Type in the name of the file to save to");
            int status = chooser.showOpenDialog(null);
            
            if (status == JFileChooser.APPROVE_OPTION)
               curModel.save(chooser.getSelectedFile().getPath());
         }//end else if
         else if (source == load && !(stepMode || runMode))
         {
            JFileChooser chooser = new JFileChooser();
            int status = chooser.showOpenDialog(null);
            
            if (status == JFileChooser.APPROVE_OPTION)
               loadModel(chooser.getSelectedFile().getPath());
         }//end else if
               
         repaint();
      }//end actionPerformed method
   }// end ButtonListener class
   
   
   /**
    * This inner class listens for the timer's delay to be up.
    * 
    */
   private class RunListener implements ActionListener
   {
      /**
       * This method tells the model to run one turn 
       * and then updates the number of steps.
       */
      public void actionPerformed(ActionEvent event)
      {
         curModel.evolve();
         stepNumber++;
         repaint();
      }//end actionPerformed method
   }//end RunListener class
   
   
   /**
    * This inner class listens for clicks on the drawing panel.
    */
   private class ClickListener extends MouseAdapter
   {
      /**
       * This method traps a mouse pressed event and
       * either creates a new cell or destroys an old one
       * where the mouse was pressed,
       * so long as neither of the modes are going.
       */
      public void mousePressed(MouseEvent e)
      {
         if (!runMode && !stepMode)
         {
            int row = (int)(e.getY() / CellModel.gridSeparation);
            int column = (int)(e.getX() / CellModel.gridSeparation);
            
            curModel.flip(row, column);
            repaint();
         }//end if
      }//end mousePressed method
   }//end ClickListener class
   
   private class DragListener extends MouseMotionAdapter
   {
      private int curRow = -1;
      
      private int curCol = -1;
      
      public void mouseDragged(MouseEvent e)
      {
         if (!runMode && !stepMode)
         {
            int row = (int)(e.getY() / CellModel.gridSeparation);
            int column = (int)(e.getX() / CellModel.gridSeparation);
            
            if ((row != curRow || column !=curCol));
            {
               curModel.flip(row, column);
               curRow = row;
               curCol = column;
               repaint();
            }
         }//end if
      }
   }
}//end GamePanel class
