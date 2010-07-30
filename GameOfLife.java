/**
 * This class plays the Game of Life!  IT is an interactive
 * game wherein the user sees a grid and picks which
 * cells in the grid he or she wants to see live or die.
 * After that the user runs the game in either step mode
 * or run mode an watches as the colony he or she created
 * either flourishes or deteriorates slowly until finally reaching
 * its doom.  The cells of the colony live or die on the basis
 * of three rules.  1, if an empty cell is surrounded by exactly
 * 3 neighbors a new cell is born.  2, a cell dies of overcrowding
 * if it is surrounded by 4 or more neighbors.  3, a cell dies of
 * loneliness if it has zero or one neighbors.  4, if a cell has
 * 2 neighbors it remains the same.
 * 
 * More specifically this class creates a frame which holds
 * the game and displays it.
 * 
 * @author Scott  gtg892r
 * I didn't collaborate on this class.
 * @version 1.0
 */

import javax.swing.*;

public class GameOfLife
{
   /** the frame of the game */
   private JFrame frame;
   
   /**
    * This constructor creates a new JFrame and adds
    * a new gamepanel to it.  It also makes it so the
    * frame can't be resized.
    *
    */
   public GameOfLife()
   {
      frame = new JFrame("Game of Life");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setResizable(false);
      frame.getContentPane().add(new GamePanel());
   }//end GameOfLife constructor
   
   /**
    * This method displays the frame on the screen 
    * by packing and showing it.
    */
   public void display()
   {
      frame.pack();
      frame.show();
   }//end display method
   
   /**
    * This method creates a new game of life and then displays it.
    * @param args an Array of Strings that argue
    */
   public static void main(String[] args)
   {
      GameOfLife myGOL = new GameOfLife();
      myGOL.display();
   } //end main method
} //end GameOfLife class
