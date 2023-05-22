/*
Project: Assignment 1
Class: CS 1501 w/ Professor Sherif Khattah
Author: Rand Hasan (rsh44)
Date: 2/10/2023
 */

public final class Crossword implements WordPuzzleInterface {
     private StringBuilder rowPref[]; //StringBuilder for the prefix (in progress) of the current row
     private StringBuilder colPref[]; //StringBuilder for the prefix (in progress) of the current col
     private DictInterface dictionary; //instance variable for the dictionary created from the dict8.txt file

	/*
     * Fills out a word puzzle defined by an empty board. 
     * The characters in the empty board can be:
     *    '+': any letter can go here
     *    '-': no letter is allowed to go here
     *     a letter: this letter has to remain in the filled puzzle
     *  @param board is a 2-d array representing the empty board to be filled
     *  @param dictionary is the dictionary to be used for filling out the puzzle
     *  @return a 2-d array representing the filled out puzzle
     */
	public char[][] fillPuzzle(char[][] board, DictInterface dictionary){
          this.dictionary = dictionary;
          rowPref = new StringBuilder[board.length];
          colPref = new StringBuilder[board.length];
          for (int i = 0; i<board.length; i++) //initializes the prefixes for each row and col to be built
          {
               rowPref[i] = new StringBuilder();
               colPref[i] = new StringBuilder();
          }
          return placeChar(0,0, board);
	}

     /*
     *  Iterates through the board and places chars based on what is already there
     *  @param row is the row of the current cell 
     *  @param col is the column that the current cell is in
     *  @param board is the 2d array that the cell is in
     *  @return a 2-d array representing the filled out puzzle
     */             
     private char[][] placeChar(int row, int col, char[][] board)
     {
          if (row == board.length) //have reached the row beyond the board meaning board has been completed and can be returned
          {
               return board;
          }
          if (board[row][col] == '+') //if the current cell contains a '+'
          {
               for (char letter = 'a'; letter <= 'z'; letter++) //iterates through every letter in the alphabet to potentially fill the cell
               {
                    board[row][col] = letter;
                    rowPref[row].append(letter); //adds letter to the row prefix StringBuilder and checks if it passes the isValid() test
                    colPref[col].append(letter); //adds letter to the col prefix StringBuilder and checks if it passes the isValid() test
                    if (isValid(row,col,board)) //checks if current row and col prefixes are valid
                    {
                         if (col==board.length-1) //cell is in the last column
                         {
                              if (placeChar(row+1, 0,board)!=null) //recursively proceed to col 0 of the next row
                              {
                                   return board;
                              }
                         }
                         else //not in the last col
                         {
                              if (placeChar(row, col+1,board)!=null) //recursively proceed to the next col
                              {
                                   return board;
                              }
                         }
                    }
                    board[row][col] = '+'; //if the letter is not valid, backtrack by deleted this added char to the row and col prefixes
                    rowPref[row].deleteCharAt(rowPref[row].length()-1);
                    colPref[col].deleteCharAt(colPref[col].length()-1);
               }
               return null;
          }
          else if (board[row][col] == '-') //or if cell currently has a '-'
          {
               rowPref[row].append('-'); //append '-' to current cell row and col prefixes
               colPref[col].append('-');
               if (col == board.length - 1) //if cell is in the last col
               {
                    return placeChar(row+1, 0, board); //recursively proceed to col 0 of the next row
               }
               else //if cell is not in the last col
               {
                    return placeChar(row, col+1, board); //recursively proceed to the next col
               }
          }
          else{ //or if cell has a preset letter
               rowPref[row].append(board[row][col]); //append preset letter to the current cell row and col prefixes
               colPref[col].append(board[row][col]);
               if (isValid(row,col,board)) //check if the current row and col prefixes are valid with this preset letter
               {
                    if (col==board.length-1) //if cell is in the last col
                    {
                         if (placeChar(row+1, 0,board)!=null) //recursively proceed to col 0 in the next row
                         {
                              return board;
                         }
                    }
                    else //if cell is not in the last col
                    {
                         if (placeChar(row, col+1,board)!=null) //recursively proceed to the next col
                         {
                              return board;
                         }
                    }
               }
               if (rowPref[row].length()>0) //backtrack the previous char in the row or col if either of the current row or col prefixes are invalid
               {
                    rowPref[row].deleteCharAt(rowPref[row].length()-1);
               }
               if (colPref[col].length()>0)
               {
                    colPref[col].deleteCharAt(colPref[col].length()-1);
               }
               return null;
          }
          
     }

     /*
     *  Checks if current row and col StringBuilder's could create prefixes or words in the dictionary to determine if letters should or should not be place
     *  @param row is the row of the current cell  
     *  @param col is the column that the current cell is in
     *  @param board is the 2d array that the cell is in
     *  @return a boolean value whether or not placing a char which pass tests that state both the row and col so far either create a word or have the potential to do so (prefix)
     */  
     private boolean isValid(int row, int col, char[][] board) 
     {
          StringBuilder rowPrefix = rowPref[row]; //StringBuilder for the current row
          StringBuilder colPrefix = colPref[col]; //StringBuilder for the current col
  
          int searchRow = dictionary.searchPrefix(rowPrefix, rowPref[row].lastIndexOf("-")+1, rowPref[row].length()-1); //search prefix of the row starting at the last minus and extending to the end of the prefix so far (note if no '-' found then we start at 0)
          int searchCol = dictionary.searchPrefix(colPrefix, colPref[col].lastIndexOf("-")+1, colPref[col].length()-1); //search prefix of the col starting at the last minus and extending to the end of the prefix so far (note if no '-' found then we start at 0)

          if (searchRow == 0 || searchCol == 0) //saves time.  invalid because row or col so far doesn't create a word OR a prefix.
          {
               return false;
          }

          if (row == board.length - 1 && col == board.length - 1) //if in the last cell
          {
               if (searchRow == 1 || searchCol == 1) //and row or col is a valid prefix but not a word (can't add anything else on)
               {
                    return false; //we must return false
               }
          }
          
          if(row < board.length - 1 && col == board.length - 1) //or if cell is in the last col but not the last row
          {
               if (board[row+1][col] == '-' && (searchCol == 0 || searchCol == 1)) //if the cell underneath is a '-' and the col so far is a prefix but not a word (cannot add anything else other)
               {
                    return false; //we must return false
               }
               if (searchRow == 1 || searchCol == 2) //if the row so far is not a word but a prefix (cannot add anything else onto the row bc we are in the last col) or if the col is a word but not a prefix (we still have more cols we can add onto)
               {
                    return false; //we must return false
               }
          }

          if(row == board.length - 1 && col < board.length - 1) //or  if cell is in the last row but not the last col
          {
               if (board[row][col+1] == '-' && (searchRow == 0 || searchRow == 1)) //if the cell to the right is a '-' and the row so far is a prefix but not a word (cannot add anything else other)
               {
                    return false; //we must return false
               }
               if (searchRow == 2 || searchCol == 1) //if the col so far is not a word but a prefix (cannot add anything else onto the row bc we are in the last col) or if the row is a word but not a prefix (we still have more cols we can add onto)
               {
                    return false; //we must return false
               }
          }

          if (row < board.length - 1 && col < board.length - 1) //or if cell is in neither the last row or last col
          {
               if (board[row][col+1] == '-' && (searchRow == 0 || searchRow == 1)) //if '-' in next col but the row so far is not a word
               {
                    return false; //we must return false
               }
               if (board[row+1][col] == '-' && (searchCol == 0 || searchCol == 1)) //if '-' in next row but the col so far is not a word 
               {
                    return false; //we must return false
               }
               if ((searchRow == 2 || searchCol == 2) && (board[row][col+1] != '-' && board[row+1][col] != '-')) //if the next cell in the row and col is not a '-' but the row or col so far is a word and NOT a prefix (yet we still have space to add chars to the row or col words)
               {
                    return false; //we must return false
               }
          }
          return true; //all cases have passed so the character is valid
     }

    /*
     * Checks if filledBoard is a correct fill for emptyBoard
     * @param emptyBoard is a 2-d array representing an empty board
     * @param filledBoard is a 2-d array representing a filled out board
     * @param dictionary is the dictinary to be used for checking the puzzle
     * @return true if rules defined in fillPuzzle has been followed and 
     *  that every row and column is a valid word in the dictionary. If a row
     *  a column has one or more '-' in it, then each segment separated by 
     * the '-' should be a valid word in the dictionary 
     */
	public boolean checkPuzzle(char[][] emptyBoard, char[][] filledBoard, DictInterface dictionary)
	{
          for (int i = 0; i<emptyBoard.length; i++) //iterates thru the empty board
          {
               for (int j = 0; j<emptyBoard.length; j++)
               {
                    if (emptyBoard[i][j]!='+') //if the cell in the empty board either contains a preset letter or '-'
                    {
                         if (filledBoard[i][j]!=emptyBoard[i][j]) //it must also be in the filledBoard
                         {
                              return false; //otherwise return false
                         }
                    }
                    if (filledBoard[i][j] == '-') //if the filled board has a '-'
                    {
                         if (emptyBoard[i][j] != '-') //it should also be in that position 
                         {
                              return false; //otherwise return false
                         }
                    }
               }
          }
          
          for (int row = 0; row<filledBoard.length; row++) //iterates thru each row in the filled board
          {
               StringBuilder r = new StringBuilder(); //creates a new StringBuilder object for each row
               for (int col = 0; col<filledBoard.length; col++) //proceeds down the cols in the row
               {
                    if (filledBoard[row][col] == '-') //if there's a '-'
                    {
                         if (dictionary.searchPrefix(r) < 2) //check if the StringBuilder object r in the row leading up to it produces a word
                         {
                              return false; //if not return false
                         }
                         else{
                              r = new StringBuilder(); //if the StringBuilder object r in the row so far does produce a word
                              continue; //proceed ot the next column
                         }
                    }
                    else { //if the cell is a letter
                         r.append(filledBoard[row][col]); //append to the row prefix
                         if (col == filledBoard.length - 1) //if cell is in the last col
                         {
                              if (dictionary.searchPrefix(r) == 0 || dictionary.searchPrefix(r) == 1) //if the row does not produce a word (no more cols tho in the row to build row prefix further)
                              {
                                   return false; //we must return false
                              }
                         }
                         else{ //cell is not in the last col
                              if (dictionary.searchPrefix(r) == 2 || dictionary.searchPrefix(r) == 0) //if the row so far is not a prefix (more cols tho so row prefix must grow)
                              {
                                   return false; //we must return false
                              }
                         }
                    }
               }
          }

          for (int col = 0; col<filledBoard.length; col++) //iterates thru each col in the filled board
          {
               StringBuilder c = new StringBuilder(); //creates a new StringBuilder object for each col
               for (int row = 0; row<filledBoard.length; row++) //proceeds down the rows in the col
               {
                    if (filledBoard[row][col] == '-') //if there's a '-'
                    {
                         if (dictionary.searchPrefix(c) == 0 || dictionary.searchPrefix(c) == 1) //check if the StringBuilder object c in the col leading up to it produces a word
                         {
                              return false; //otherwise return false
                         }
                         else{
                              c = new StringBuilder(); //if the StringBuilder object c in the col so far does produce a word
                              continue; //proceed ot the next column
                         }
                    }
                    else { //if the cell is a letter
                         c.append(filledBoard[row][col]); //append to the col prefix
                         if (row == filledBoard.length-1) //if cell is in the last row
                         {
                              if (dictionary.searchPrefix(c) == 0 || dictionary.searchPrefix(c) == 1) //if the col does not produce a word (no more rows tho in the col to build col prefix further)
                              {
                                   return false; //we must return false
                              }
                         }
                         else{ //cell is not in the last col
                              if (dictionary.searchPrefix(c) == 2 || dictionary.searchPrefix(c) == 0) //if the col so far is not a prefix (more rows tho so col prefix must grow)
                              {
                                   return false; //we must return false
                              }
                         }
                    }
               }
          }
          return true; //otherwise return true bc we've passed all tests
	}
}
