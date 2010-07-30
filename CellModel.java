/**
 * This class is a cell model.  It holds a matrix
 * of squares which represents the model.  It
 * can draw itself, evolve into a new generation,
 * and save itself.
 * 
 * @author Scott  gtg892r
 * I didn't collaborate on this class.
 * @version 1.0
 */

import java.awt.*;
import java.io.*;
import javax.swing.*;

public class CellModel implements Serializable
{
   /** the matrix of cells representing the habitat */
   private Square[][] board;
   
   /** the size of the board (number of rows) */
   public int size;
   
   /** the length of each cell on the board (or the distance between adjacent gridlines) */
   public static double gridSeparation;
   
   /**
    * This constructor initializes the board matrix to the
    * correct size and then instantiates all the squares.
    * It also intializes the gridSeparation and rows variables.
    * @param rows an integer representing the number of rows in the model
    * @param columns an integer representing the number of columns in the model
    */
   public CellModel(int rows, int columns)
   {
      board = new Square[rows][columns];
      size = rows;
      gridSeparation = (double)GamePanel.WIDTH / board.length;
      for (int i = 0; i < board.length; i++)
         for (int j = 0; j < board[0].length; j++)
            board[i][j] = new Square();
   }//end CellModel constructor
   
   /**
    * This method runs one turn on the model.  It creates a
    * new, temporary matrix and then adds the cells based on the cell
    * positions in the actual board.  It then makes the actual board
    * the same as the temporary one.
    *
    */
   public void evolve()
   {
      Square[][] temp = new Square[board.length][board[0].length];
      int status = 0;
      
      for (int i = 0; i < board.length; i++)
         for (int j = 0; j < board[0].length; j++)
         {
            status = testDeath(i, j);
            if (status == 0)
               temp[i][j] = new Square(false);
            else if (status == 1 && !board[i][j].isAlive())
               temp[i][j] = new Square(true);
            else if (board[i][j].isAlive())
               temp[i][j] = new Square(board[i][j].getAge());
            else
               temp[i][j] = new Square(false);
         }//end for
      
      board = temp;
   }//end evolve method
   
   /**
    * This method tests to see if the cell at the given location
    * is going to die or live, or stay the same.
    * @param row an integer representing the row of the cell to be tested
    * @param col an integer representing the column of the cell to be tested
    * @return an integer that is 1 if the cell is going to live, 0 if it is to die, and -1 otherwise
    */
   public int testDeath(int row, int col)
   {
      int result = -1, total = 0, startI = row - 1, startJ = col - 1;
      
      if (row == 0)
         startI = row;
      if (col == 0)
         startJ = col;
      if (board[row][col].isAlive())
         total--;
      
      for (int i = startI; i <= row + 1 && i < board.length; i++)
         for (int j = startJ; j <= col + 1 && j < board[0].length; j++)
            if (board[i][j].isAlive())
               total++;
            
      if (total >= 4 || total <= 1)
         result = 0;
      else if (total == 3)
         result = 1;
      
      return result;
   }//end testDeath method
   
   /**
    * This method draws the model to the panel.
    * @param g a Graphics object representing the page to be drawn to
    */
   public void display(Graphics g)
   {
      g.setColor(Color.white);
      for (int i = 0; i < board.length; i++)
         for (int j = 0; j < board[0].length; j++)
            if (board[i][j].isAlive())
               board[i][j].show(g, i, j);
      
      g.setColor(Color.darkGray);
      drawGrid(g);
   }//end display method
   
   /**
    * This method draws the gridlines on the panel.
    * @param g a Graphics object representing the page to be drawn to
    */
   public void drawGrid(Graphics g)
   {
      for (int i = 1; i < board.length; i++)
      {
         g.drawLine((int)(i*gridSeparation), 0, (int)(i*gridSeparation), GamePanel.HEIGHT);
         g.drawLine(0, (int)(i*gridSeparation), GamePanel.WIDTH,  (int)(i*gridSeparation));
      }//end for
   }//end drawGrid method
   
   /**
    * This method flips the status of the square at the given
    * location.
    * @param row an integer representing the row of the square to be flipped
    * @param column an integer representing the column of the square to be flipped
    */
   public void flip(int row, int column)
   {
      board[row][column].flip();
   }//end flip method
   
   /**
    * This method saves the object using the serializable interface.
    * It also saves the gridSeparation variable.
    * @param fName a String representing the file the object is to be saved to
    */
   public void save(String fName)
   {
      try
      {
         FileOutputStream fos = new FileOutputStream(fName);
         ObjectOutputStream out = new ObjectOutputStream(fos);
         out.writeObject(this);
         out.writeDouble(gridSeparation);
         out.close();
      }//end try
      catch (IOException exception)
      {
         JOptionPane.showMessageDialog(null, "Error writing to file, try again");
      }//end catch
   }//end save method
}//end CellModel class
