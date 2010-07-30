/**
 * This class represents a cell.  It can either be alive
 * or dead.  And like most humans, it knows how
 * old it is.  Based on this knowledge a cell can draw
 * itself in a certain shade of gray, the older the darker.
 * 
 * @author Scott  gtg892r
 * I didn't collaborate on this class
 * @version 1.0
 */

import java.awt.*;
import java.io.*;

public class Square implements Serializable
{
   /** true if the cell is alive */
   private boolean alive;
   
   /** the age of the cell */
   private int age;
   
   /**
    * This constructor cretes a new, dead, cell.
    *
    */
   public Square()
   {
      alive = false;
      age = 0;
   }//end Square constructor
   
   /**
    * This constructor creates a new cell that already
    * knows whether or not it is alive.
    * @param alive a boolean representing the status of the new cell (alive or dead)
    */
   public Square(boolean alive)
   {
      this.alive = alive;
   }//end Square constructor
   
   /**
    * This constructor creates a  new cell that already
    * knows its age and adds one to it.  
    * @param age an integer representing the age of the cell
    */
   public Square(int age)
   {
      this.age = age + 1;
      alive = true;
   }//end Square constructor
   
   /**
    * This method gets the status of the cell.
    * @return a boolean that is true if the cell is alive
    */
   public boolean isAlive()
   {
      return alive;
   }//end isAlive method
   
   /**
    * This method kills the cell.
    */
   public void die()
   {
      alive = false;
   }//end die method
   
   /**
    * This method resurrects the cell from the dead
    * and it lives.
    */
   public void live()
   {
      alive = true;
   }//end live method
   
   
   /**
    * This method makes the status opposite of what
    * it used to be.  So if it was dead before now it is
    * alive, and vice versa.
    */
   public void flip()
   {
      alive = !alive;
   }//end flip method
   
   /**
    * This method gets the age of the cell.
    * @return an integer representing the age of the cell
    */
   public int getAge()
   {
      return age;
   }//end getAge method
   
   /**
    * This method draws a living cell on the screen based on its age.
    * The older a cell is the darker the shade of gray it is drawn to be.
    * @param g a Graphics objbect representing the page to be drawn on
    * @param row an integer representing the row on which to draw the cell
    * @param column an integer representing the column on which to draw the cell
    */
   public void show(Graphics g, int row, int column)
   {
      int x1 = (int)(column*CellModel.gridSeparation);
      int y1 = (int)(row*CellModel.gridSeparation);
      int colorNum = 255 - 35*age;
      
      if (colorNum < 45)
         colorNum = 45;
      
      g.setColor(new Color(colorNum, colorNum, colorNum));
      g.fillRect(x1, y1, (int)CellModel.gridSeparation, (int)CellModel.gridSeparation);
   }//end show method
}//end Square class
