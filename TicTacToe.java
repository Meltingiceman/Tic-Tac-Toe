import java.util.Random;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class TicTacToe
{
   public static void main(String[] args)
   {
      TTTGame game = new TTTGame();
      while(!game.getGameOver())
      {
         if(game.getReset())
            game = new TTTGame();
      }
   }
}

class TTTGame extends JFrame
{
   private boolean gameOver = false;
   private boolean reset = false;
   private TTTSquare[][] game;
   private JButton[][] squares;
   private JButton resetButton;
   private JPanel board;
   private JPanel bottomPanel;
   private final int WIDTH = 850;
   private final int HEIGHT = 700;
   private int oWins;
   private int xWins;
   private int round;
   private boolean debug = false; //only set this to true if you want to debug
   private boolean xTurn;
   private boolean bot;
   private boolean botIsX;
   private boolean firstTurn = true;
   private static Random rand = new Random();
   
   public TTTGame()
   {
      setSize(WIDTH, HEIGHT);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setLayout(new BorderLayout(20, 7));
      
      setUp();
   }
   
   public void setUp()
   {   
      board = new JPanel();
      board.setDoubleBuffered(false);
      
      resetButton = new JButton("Reset");
      resetButton.addActionListener(new ResetButtonListener());
      
      bottomPanel = new JPanel();
      bottomPanel.setSize(200, 400);
      bottomPanel.setLayout(new FlowLayout());
      /*unfinished reset button. problem switching from X to O when playing against the bot */
//       bottomPanel.add(resetButton); 
      
      if(debug)
      {
         JButton button1 = new JButton();
         JButton button2 = new JButton();
         button1.addActionListener(new DebugListener1());
         button2.addActionListener(new DebugListener2());
         
         bottomPanel.add(button1);
         bottomPanel.add(button2);
      }
      board.setLayout(new GridLayout(3, 3));
      
      game = new TTTSquare[3][3];

      for(int i = 0; i < 3; i++)
      {
         for(int j = 0; j < 3; j++)
         {
            game[i][j] = new TTTSquare(i + 1, j + 1);
            board.add(game[i][j].square);
         }
      }
      
      xTurn = true;
      oWins = xWins = 0;
      round = 1;
      
      setTitle("Tic-Tac-Toe. Round: " + round + " X wins: " + xWins + " O wins: " + oWins);
      add(board, BorderLayout.CENTER);
      add(bottomPanel, BorderLayout.SOUTH);
      
      setVisible(true);
      
      setUpBot();
      
      // int choice = JOptionPane.showConfirmDialog(null, "Do you want to play against the Computer");
//       
//       if(choice == JOptionPane.YES_OPTION)
//       {
//          bot = true;
//          int xChoice = JOptionPane.showConfirmDialog(null, "Do you want to be X");
//          if(xChoice == JOptionPane.YES_OPTION)
//             botIsX = false;
//          else
//             botIsX = true;
// //          botIsX = rand.nextBoolean();
//          String message = "Playing against the Computer good luck!";
//          if(botIsX)
//             message += " The Computer is X";
//          else
//             message += " The Computer is O";
//          JOptionPane.showMessageDialog(null, message);
//          initBot();
//       }
//       else
//       {
//          bot = false;
//          JOptionPane.showMessageDialog(null, "2-Player mode. Have fun!");
//       }
   }
   
   private void setUpBot()
   {
      int choice = JOptionPane.showConfirmDialog(null, "Do you want to play against the Computer");
      
      if(choice == JOptionPane.YES_OPTION)
      {
         bot = true;
         int xChoice = JOptionPane.showConfirmDialog(null, "Do you want to be X");
         if(xChoice == JOptionPane.YES_OPTION)
            botIsX = false;
         else
            botIsX = true;
//          botIsX = rand.nextBoolean();
         String message = "Playing against the Computer good luck!";
         if(botIsX)
            message += " The Computer is X";
         else
            message += " The Computer is O";
         JOptionPane.showMessageDialog(null, message);
         initBot();
      }
      else
      {
         bot = false;
         JOptionPane.showMessageDialog(null, "2-Player mode. Have fun!");
      }
   }
   
   private void initBot()
   {
      if(botIsX)
         botTurn();
   }
   
   public boolean mark(int y, int x, char id)
   {
      boolean success = false;
      if(game[y][x].mark(id))
      {
         squares[y][x] = new JButton(Character.toString(id).toUpperCase());
         success = true;
      }
      
      return success;
   }
   
   public char checkWin()
   {
      char winner = ' ';
      
      winner = checkVert();
      if(winner == ' ')
      {
         winner = checkHorz();
         if(winner == ' ')
         {
            winner = checkDiag();
         }
      }
         
      return winner;
   }
   
   private boolean checkDraw()
   {
      boolean draw = true;
      
      for(int i = 0; i < 3 && draw; i++)
      {
         for(int j = 0; j < 3 && draw; j++)
         {
            if(game[i][j].id == ' ')
               draw = false;
         }
      }
      
      return draw;
   }
   
   private char checkVert()
   {
      char winner = ' ';
      boolean winnerFound = false;
      int xAccumulator = 0;
      int oAccumulator = 0;
      for(int i = 0; i < 3 && !winnerFound; i++)
      {
         for(int j = 0; j < 3; j++)
         {
            if(game[j][i].id == 'X')
            {
               if(oAccumulator > 0)
                  break;
               else
                  xAccumulator++;
               //oAccumulator = 0;
            }
            else if(game[j][i].id == 'O')
            {
               if(xAccumulator > 0)
                  break;
               oAccumulator++;
               //xAccumulator = 0; 
            }
         }
         if(xAccumulator == 3)
         {
            winner = 'X';
            winnerFound = true;
         }
         else if(oAccumulator == 3)
         {
            winner = 'O';
            winnerFound = true;
         }
         xAccumulator = oAccumulator = 0;
      }
      return winner;
   }
   
   private char checkHorz()
   {
      char winner = ' ';
      boolean winnerFound = false;
      int xAccumulator = 0;
      int oAccumulator = 0;
      for(int i = 0; i < 3 && !winnerFound; i++)
      {
         for(int j = 0; j < 3; j++)
         {
            if(game[i][j].id == 'X')
            {
               xAccumulator++;
               oAccumulator = 0;
            }
            else if(game[i][j].id == 'O')
            {
               oAccumulator++;
               xAccumulator = 0;
            }
         }
         if(xAccumulator == 3)
         {
            winner = 'X';
            winnerFound = true;
         }
         else if(oAccumulator == 3)
         {
            winner = 'O';
            winnerFound = true;
         }
         xAccumulator = oAccumulator = 0;
      }
      return winner;
   }
   
   private char checkDiag()
   {
      char winner = ' ';
      int oAccumulator = 0;  int xAccumulator = 0;
      int i = 0;
      
      for(i = 0; i < 3; i++) //starting from top left
      {
         if(game[i][i].id == 'X')
         {
            xAccumulator++;
            oAccumulator = 0;
         }
         else if(game[i][i].id == 'O')
         {
            oAccumulator++;
            xAccumulator = 0;
         }
      }
      
      if(xAccumulator == 3)
         winner = 'X';
      else if(oAccumulator == 3)
         winner = 'O';
      else //starting from top right
      {
         xAccumulator = oAccumulator = 0;
         i = 2;
         for(int j = 0; j < 3; j++) //reuse i in loop since it will already be 2
         {
            if(game[j][i].id == 'X')
            {
               xAccumulator++;
               oAccumulator = 0;
            }
            else if(game[j][i].id == 'O')
            {
               oAccumulator++;
               xAccumulator = 0;
            }
            i--;
         }
         if(xAccumulator == 3)
            winner = 'X';
         else if(oAccumulator == 3)
            winner = 'O';   
      }
      return winner;
   }
   
   //the brains of the bot
   private void botTurn()
   {
      int counter = 1;
      char botId; char humId;
      boolean spotFound;
      int cornerCounter = 0;
      
      if(botIsX)
      {
         botId = 'X';
         humId = 'O';
      }
      else
      {
         botId = 'O';
         humId = 'X';
      }   
      spotFound = botVertHorz(botId);
      
      while(!spotFound && counter <= 8)
      {
         switch(counter)
         {
         case 1:
            spotFound = botDiag(botId);
//             if(!spotFound)
//                spotFound = botSplit(botId);
            break;
         case 2:
               spotFound = botVertHorz(humId);
               if(!spotFound)
               {
//                   spotFound = botSplit(humId);
                  if(!spotFound)
                     spotFound = botDiag(humId);
               }
            break;
         case 3:
            if(botIsX && firstTurn && rand.nextBoolean())
            {
               spotFound = game[1][1].mark(botId);
            }
            else
               spotFound = game[1][1].mark(botId);
            break;
         case 4:
            if(game[0][0].id == humId)
               spotFound = game[2][2].mark(botId);
            
            if(!spotFound && game[2][2].id == humId)
               spotFound = game[0][0].mark(botId);
               
            if(!spotFound && game[0][2].id == humId)
               spotFound = game[2][0].mark(botId);
               
            if(!spotFound && game[2][0].id == humId)
               spotFound = game[0][2].mark(botId);
            break;
         case 5:
            if(botIsX && firstTurn && rand.nextBoolean())
            {
               System.out.print("DEBUG: BREAK HAPPENED ");
               break;
            }
            System.out.println("DOING CASE 5");
            int[] corner = {1, 2, 3, 4};
            int num1, num2, temp;
            for(int i = 0; i < 100; i++)
            {
               num1 = rand.nextInt(corner.length);
               do
               {
                  num2 = rand.nextInt(corner.length);
               }while(num2 == num1);
               temp = corner[num1];
               corner[num1] = corner[num2];
               corner[num2] = temp;
            }
            
            if((game[0][0].id == humId && game[2][2].id == humId) ||
               (game[0][2].id == humId && game[0][2].id == humId))
            {
               if(rand.nextBoolean()) //mark horizontally
               { 
                  if(rand.nextBoolean())
                     spotFound = game[1][2].mark(botId);
                  else
                     spotFound = game[1][0].mark(botId);
               }
               else //mark vertically
               {
                  if(rand.nextBoolean())
                     spotFound = game[0][1].mark(botId);
                  else
                     spotFound = game[2][1].mark(botId);
               }
            }
            
            while(!spotFound && cornerCounter < corner.length)
            {
               switch(corner[cornerCounter])
               {
               case 1:
                  spotFound = game[0][0].mark(botId);
                  break;
               case 2:
                  spotFound = game[0][2].mark(botId);
                  break;
               case 3:
                  spotFound = game[2][0].mark(botId);
                  break;
               case 4:
                  spotFound = game[2][2].mark(botId);
                  break;
               }
               cornerCounter++;
            }
            break;
         case 6:
            int y;
            int x;
            do
            {
               y = rand.nextInt(3);
               x = rand.nextInt(3);
            }while(!game[y][x].mark(botId));
            break;
         default:
//             System.out.println("DEBUG: DELETE LATER");
//             //DELETE THIS LATER
            break;
         }
         counter++;
      }
      firstTurn = false;
      if(checkWin() != ' ')
      {
         JOptionPane.showMessageDialog(null, "The Computer has won. starting next round.");
         if(botIsX)
            xWins++;
         else
            oWins++;
         
         round++;
         newRound(false);
      }
      else if(checkDraw())
      {
         JOptionPane.showMessageDialog(null, "There is a draw no one " +
                                       "wins. Starting next round.");
         round++;
         newRound(false);
      }
   }
   
   //bot looking for a spot to win
   private boolean botVertHorz(char key)
   {
      boolean spotFound = false;
      boolean horizontal = true;
      int Accumulator = 0;
      char botId;
      
      if(botIsX)
         botId = 'X';
      else
         botId = 'O';
      
      int counter = 0;
      do
      {
         for(int i = 0; i < 3 && !spotFound; i++)
         {
            int j = 0;
            for(j = 0; j < 2; j++)
            {
               if(horizontal) //the bot checks horizotally first
               {
//                   System.out.println(game[i][j].id);
//                   System.out.println(botId);
                  if(game[i][j].id == key)
                    Accumulator++;
               }
               else //then the bot checks vertically
               {
                  if(game[j][i].id == key)
                     Accumulator++;
               }
            }
            
            if(Accumulator == 2) //second array index changed from j + 1
            {
               if(horizontal && game[i][j].id == ' ')
               {
                  /*if(*/spotFound = game[i][j].mark(botId);//)
//                      spotFound = true;

               }
               else if(!horizontal && game[j][i].id == ' ')
               {
                  /*if(*/spotFound = game[j][i].mark(botId);//)
//                      spotFound = true;
               }
            }
            Accumulator = 0;
         }
         
         if(!spotFound)
         {
            Accumulator = 0;
            for(int i = 0; i < 3; i++)
            {
               int j = 2;
               for(j = 2; j > 0; j--)
               {
                  if(horizontal)
                  {
                     if(game[i][j].id == key)
                        Accumulator++;
                  }
                  else
                  {
                     if(game[j][i].id == key)
                        Accumulator++;
                  }
//                   System.out.println("DEBUG: " + j);
               }
               
               if(Accumulator == 2)
               {
                  if(horizontal && game[i][j].id == ' ')
                  {
                     /*if(*/spotFound = game[i][j].mark(botId);//)
//                         spotFound = true;
                  }
                  else if(!horizontal && game[j][i].id == ' ')
                  {
                     /*if(*/spotFound = game[j][i].mark(botId);//)
//                         spotFound = true;
                  }
               }
               Accumulator = 0;
            }
         }
         counter++;
         horizontal = false;
//          System.out.println("DEBUG: " + spotFound);
      }while(counter < 2 && !spotFound);
//       System.out.println("DEBUG: EXITING BOT LOOP");
      if(!spotFound)
         spotFound = botSplit(key);
      return spotFound;
   }
   
   private boolean botSplit(char key)
   {
      int accumulator; int counter = 0;
      boolean spotFound = false;
      boolean horizontal = true;
      char botId;
      if(botIsX)
         botId = 'X';
      else
         botId = 'O';
      do
      {
         for(int z = 0; z < 3 && !spotFound; z++)
         {
            if(horizontal && game[z][0].id == key && game[z][2].id == key 
               && game[z][1].id == ' ') {
               game[z][1].mark(botId);
               spotFound = true;
            }
            else if(!horizontal && game[0][z].id == key && game[2][z].id == key
               && game[1][z].id == ' ') {
               game[1][z].mark(botId);
               spotFound = true;   
            }
         }
         counter++;
         horizontal = false;
      }
      while(counter < 2 && !spotFound);
      return spotFound;
   }
   
   private boolean botDiag(char key)
   {
      int accumulator = 0;
      boolean spotFound = false;
      int j = 0;
      int i = 0;
      char botId;
      
      if(botIsX)
         botId = 'X';
      else
         botId = 'O';
      
      for(int z = 1; z <= 6 && !spotFound; z++)
      {
         accumulator = 0;
         switch(z)
         {
         case 1:
           for(i = 0; i < 2; i++, j++) //starting from top left
            {
               if(game[i][j].id == key)
                  accumulator++;
            }
            break;
         case 2:
            for(i = 2; i > 0; i--, j--)
            {
               if(game[i][j].id == key)
                  accumulator++;
            }
            break;
         case 3:
            i = 2;
            for(i = 2; i > 0; i--, j++)
            {
               if(game[i][j].id == key)
                  accumulator++;
            }
            break;
         case 4:
            for(i = 0; i < 2; i++, j--)
            {
               if(game[i][j].id == key)
                  accumulator++;
            }
            break;
         case 5:
            i = j = 1;
            if(game[0][0].id == key && game[2][2].id == key)
               accumulator = 2;
            break;
         case 6:
//             System.out.println(i + " " + j);
            if(game[0][2].id == key && game[2][0].id == key)
               accumulator = 2;
            break;
         }
//          System.out.println(i + " " + j);
         if(accumulator == 2 && game[i][j].id == ' ')
         {
            game[i][j].mark(botId);
            spotFound = true;
         }
         
      }
      return spotFound;
   }
   
   private boolean botBlockArrow()
   {
      return false;
   }
   
   public boolean getReset()
   {
      return reset;
   }
   
   public String toString()
   {
      String s = "";
      for(int i = 0; i < 3; i++)
      {
         for(int j = 0; j < 3; j++)
         {
            s += game[i][j];
         }
         s += "\n";
      }
      return s;
   }
   
   public boolean getGameOver()
   {
      return gameOver;
   }
   
//    private void reset()
//    {
//       setVisible(false);
//       firstTurn = true;
//       
//       setUp();
//    }
   
   protected void update()
   {
      //setVisible(false);
      remove(board);
      board = new JPanel();
      board.setLayout(new GridLayout(3, 3));
      
      for (int i = 0; i < 3; i++)
      {
         for (int j = 0; j < 3; j++)
         {
            board.add(game[i][j].square);
         }
      }
      add(board);
      
      setVisible(true);
   }
   
   protected void newRound(boolean reset)
   {
      //setVisible(false);
      remove(board);
      firstTurn = true;
      board = new JPanel();
      board.setLayout(new GridLayout(3, 3));
      game = new TTTSquare[3][3];
      
      for(int i = 0; i < 3; i++)
      {
         for(int j = 0; j < 3; j++)
         {
            game[i][j] = new TTTSquare(i + 1, j + 1);
            board.add(game[i][j].square);
         }
      }
      xTurn = true;
      if(reset)
      {
         oWins = xWins = 0;
         round = 1;
         setUpBot();
      }
      
      setTitle("Tic-Tac-Toe. Round: " + round + " X wins: " + xWins + " O wins: " + oWins);  
      
      add(board, BorderLayout.CENTER);
      setVisible(true);
      if(bot)
         initBot();
   }
   
   private class DebugListener1 implements ActionListener
   {
      public void actionPerformed(ActionEvent e) 
      {
         System.out.println("XTURN: " + xTurn + "\nBOTISX: " + botIsX);
      }
   }
   //sets up environment to test certain conditions
   private class DebugListener2 implements ActionListener
   {
      public void actionPerformed(ActionEvent e)
      {
         char botId;
         if(botIsX)
            botId = 'X';
         else
            botId = 'O';
         game[0][2].mark(botId);
         game[2][0].mark(botId);
         
      }
   }
   
   private class ResetButtonListener implements ActionListener
   {
      public void actionPerformed(ActionEvent buttonPushed)
      {
         bot = botIsX = xTurn = false;
         newRound(true);        
      }
   }
   
   private class TTTSquare
   {
      public boolean marked;
      public char id;
      public JButton square;
      public int y;
      public int x;
      
      public TTTSquare()
      {
         marked = false;
         square = new JButton();
         square.addActionListener(new MarkerListener());
         id = ' ';
      }
      
      public TTTSquare(int y, int x)
      {
         this.y = y;
         this.x = x;
         marked = false;
         square = new JButton();
         square.addActionListener(new MarkerListener());
         id = ' ';
      }
      
      public boolean mark(char c)
      {
//          System.out.println("DEBUG: MARK CALLED");
         if(marked) return false;
         
         id = c;
         square = new JButton(Character.toString(id).toUpperCase());
         square.setFont(new Font("Arial", Font.PLAIN, 100));
         
         if(xTurn)
         {
            square.setBackground(Color.RED);
            xTurn = false;
         }
         else
         {
            square.setBackground(Color.BLUE);
            xTurn = true;
         }
         update();
         marked = true;
         return true;
      }
      
      public String toString()
      {
          return "[" + id + "]";
      }
      
      private class MarkerListener implements ActionListener
      {
         public void actionPerformed(ActionEvent e)
         {
            boolean newRound = false;
            if(marked) return;
            
            if(xTurn)
               mark('X');
            else
               mark('O');
            // update();
            char winner = checkWin();

            if(winner != ' ')
            {
               JOptionPane.showMessageDialog(null, winner + " wins. Starting next round.");
                              
               if(winner == 'X')
               {
                  xWins++;
               }
               else
               {
                  oWins++;
               }
               round++;
               
               newRound(false);
            }
            else if(checkDraw())
            {
               JOptionPane.showMessageDialog(null, "There is a draw no one " +
                                             "wins. Starting next round.");
               round++;
               newRound(false);
               newRound = true;
            }
            
            if(bot && winner == ' ' && !newRound) //playing against the A.I.
            {
               botTurn();
            }
         }
      }
   }
}