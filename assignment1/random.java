
'''
     private StringBuilder getCol(int row, int col)
     {
          StringBuilder colPrefix = new StringBuilder();
          for (int i = row; i<board.length && board[i][col] != '-'; i++) //stays in col but moves down rows
          {
               colPrefix.append(board[i][col]);
          }
          return colPrefix; 
     }

     private StringBuilder getRow(int row, int col)
     {
          StringBuilder rowPrefix = new StringBuilder();
          for (int i = col; i<board.length && board[row][i] != '-'; i++) //stays in the row but moves left to right thru the columns
          {
               rowPrefix.append(board[row][i]);
          }
          return rowPrefix;
     }
'''

private StringBuilder getCol(int row, int col, char[][] board)
     {
          for (int i = 0; i<colPref.length; i++) //stays in col but moves down rows
          {
               colPref[col].append(board[i][col]);
          }
          return colPrefix; 
     }

     private StringBuilder getRow(int row, int col, char[][] board)
     {
          for (int i = 0; i<board.length && board[row][i] != '-'; i++) //stays in the row but moves left to right thru the columns
          {
               rowPref[row].append(board[row][i]);
          }
          return rowPrefix;
     }



     public final class Crossword implements WordPuzzleInterface {
     private StringBuilder rowPref[];
     private StringBuilder colPref[];
     private DictInterface dictionary;
	/*
     * fills out a word puzzle defined by an empty board. 
     * The characters in the empty board can be:
     *    '+': any letter can go here
     *    '-': no letter is allowed to go here
     *     a letter: this letter has to remain in the filled puzzle
     *  @param board is a 2-d array representing the empty board to be filled
     *  @param dictionary is the dictinary to be used for filling out the puzzle
     *  @return a 2-d array representing the filled out puzzle
     */
	public char[][] fillPuzzle(char[][] board, DictInterface dictionary){
          this.dictionary = dictionary;
          rowPref = new StringBuilder[board.length];
          colPref = new StringBuilder[board.length];
          for (int i = 0; i<board.length; i++)
          {
               rowPref[i] = new StringBuilder();
               colPref[i] = new StringBuilder();
          }
          return placeChar(0,0, board);
	}

     private char[][] placeChar(int row, int col, char[][] board)
     {
          if (row == board.length) //have reached the row beyond the board meaning board has been completed
          {
               return board;
          }
          if (board[row][col] == '+')
          {
               for (char letter = 'a'; letter <= 'z'; letter++) 
               {
                    if (rowPref[row].startsWith)
               }
          }
          if (board[row][col] == '-')
          {
               rowPref[row].append(board[row][col]);
               colPref[col].append(board[row][col]);
               if (col >= board[row].length)
               {
                    return placeChar(row+1,0,board);
               }
               else{
                    return placeChar(row, col+1,board);
               }
               //rowPref[row].deleteCharAt(rowPref[row].length()-1);
               //colPref[col].deleteCharAt(colPref[col].length()-1);

          }
          else if (board[row][col] == '+')
          {
               for (char letter = 'a'; letter <= 'z'; letter++) 
               {
                    board[row][col] = letter;
                    rowPref[row].append(letter);
                    colPref[col].append(letter);
                    if (isValid(row,col,board))
                    {
                         if (col==board.length-1)
                         {
                              return placeChar(row+1, col,board);
                         }
                         else
                         {
                              return placeChar(row, col+1,board);
                         }
                    }
                    rowPref[row].deleteCharAt(rowPref[row].length()-1);
                    colPref[col].deleteCharAt(colPref[col].length()-1);
               }
          }
          else
          {
               rowPref[row].append(board[row][col]);
               colPref[col].append(board[row][col]);
               if (isValid(row,col,board))
               {
                    if (col==board.length-1)
                    {
                         return placeChar(row+1, col,board);
                    }
                    else
                    {
                         return placeChar(row, col+1,board);
                    }
               }
               rowPref[row].deleteCharAt(rowPref[row].length()-1);
               colPref[col].deleteCharAt(colPref[col].length()-1);
          }
          return null;
     }

     private boolean isValid(int row, int col, char[][] board) {
          StringBuilder rowPrefix = rowPref[row];
          StringBuilder colPrefix = colPref[col];

          int searchRow = dictionary.searchPrefix(rowPrefix);
          int searchCol = dictionary.searchPrefix(colPrefix);

          if (searchRow == 0 || searchRow == 2 || searchCol == 0 || searchCol == 2)
          {
               System.out.println(rowPref[row]);
               return false;
          }

          if (row < board.length - 1 && board[row+1][col] != '-')
          {
               StringBuilder down = rowPrefix;
               down.append(board[row+1][col]);
               if (dictionary.searchPrefix(down) == 0 || dictionary.searchPrefix(down) == 2)
               {
                    return false;
               }
          }

          if (col < board.length - 1 && board[row][col+1] != '-')
          {
               StringBuilder side = colPrefix;
               side.append(board[row][col+1]);
               if (dictionary.searchPrefix(side) == 0 || dictionary.searchPrefix(side) == 2)
               {
                    return false;
               }
          }
          
          return true;
     }

    /*
     * checks if filledBoard is a correct fill for emptyBoard
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
		return false;
	}
}

private boolean isValid(int row, int col, char[][] board) {
          StringBuilder rowPrefix = rowPref[row];
          StringBuilder colPrefix = colPref[col];

          int searchRow = dictionary.searchPrefix(rowPrefix);
          int searchCol = dictionary.searchPrefix(colPrefix);

          if (searchRow == 0 || searchCol == 0)
          {
               return false;
          }
          else
          {
               return true;
          }
     }


else
          {
               rowPref[row].append(board[row][col]);
               colPref[col].append(board[row][col]);
               if (isValid(row,col,board))
               {
                    if (col == board.length - 1)
                    {
                         return placeChar(row+1, 0, board);
                    }
                    else
                    {
                         return placeChar(row, col+1, board);
                    }
               }

          }
          return
          
          if (col==board.length-1)
               {
                    return placeChar(row+1, 0,board);
               }
               else{
                    return placeChar(row, col+1,board);
               }


               public boolean checkPuzzle(char[][] emptyBoard, char[][] filledBoard, DictInterface dictionary)
	{
		for (int row = 0; row < filledBoard.length; row++)
          {
               StringBuilder rowWord = new StringBuilder();
               for (int col = 0; col<filledBoard[0].length; col++)
               {
                    rowWord.append(filledBoard[row][col]);
               }
               if (dictionary.searchPrefix(rowWord) == 0 || dictionary.searchPrefix(rowWord) == 1)
               {
                    return false;
               }
          }

          for (int col = 0; col < filledBoard[0].length; col++)
          {
               StringBuilder colWord = new StringBuilder();
               for (int row = 0; row<filledBoard.length; row++)
               {
                    colWord.append(filledBoard[row][col]);
               }
               if (dictionary.searchPrefix(colWord) == 0 || dictionary.searchPrefix(colWord) == 1)
               {
                    return false;
               }
          }
          return true;
	}




          else if (row<board.length - 1 && col<board.length - 1) //not on any edge but both row and col are prefixes at least or words
          {
               
               if ((searchRow == 1 || searchRow == 3) && (searchCol == 1 || searchCol == 3))
               {
                    return true;
               }
          }
          else if (row == board.length - 1 && col == board.length - 1) //last cell in board, both row and col MUST be words (and/or prefixes)
          {
               if ((searchRow == 2 || searchRow == 3) && (searchCol == 2 || searchCol == 3))
               {
                    return true;
               }
          }
          else if (row == board.length - 1 && col < board.length - 1) //also last row but not last col, 
          {
               if ((searchRow == 1 || searchRow == 3) && (searchCol == 2 || searchCol == 3)) //everything in the current col should amount to a word (and/or prefix) but the row just needs to be a prefix at the least (in progress)
               {
                    return true;
               }
          }
          else if (row < board.length - 1 && col == board.length - 1) //last col but not last row, the current row should be a word (and/or prefix) and the col must a prefix in progress at the very least
          {
               if ((searchRow == 2 || searchRow == 3) && (searchCol == 1 || searchCol == 3)) //col is a prefix
               {
                    return true;
               }
          }
          return false; //not valid

          //check rows (prefixes)
          if (searchRow == 1) //row is prefix not word
          {
               if (col == colPref.length - 1) //last column... row must be a word
               {
                    return false;
               }
               if (board[row][col+1] == '-') //next column is a minus and the row so far is only a prefix but not a word... invalid
               {
                    return false;
               }
          }
          else if (searchRow == 2) //row is word not prefix
          {
               if (col < colPref.length - 1) //col is not last but there's  a '-' so the row so far must be a word
               {
                    if (board[row][col+1] != '-')
                    {
                         return false;
                    }
                    
               }
               else
               {
                    //need to consider if row is a word
               }
          }
          //now check cols (prefixes)
          if (searchCol == 1) //col is prefix but not a word
          {
               if (row == rowPref.length - 1) //last row but the col is not a word... not valid
               {
                    return false;
               }
               if (board[row+1][col] == '-') //next cell in the col (1 row down) is a minus and the word so far is not a word... not valid 
               {
                    return false;
               }
          }
          else if (searchCol == 2) //col is word but not prefix
          {
               if (row != rowPref.length - 1) //not at the last row
               {
                    if (board[row+1][col] != '-') //next cell in col (1 row down) is not a minus (must be another char) yet somehow the unfinished word is not a prefix... there are more cells in the coll but none with the current column prefix... invalid
                    {
                         return false;
                    }
               }
          }
          return true; //all cases have passed so the character is valid
     }

    /*
     * checks if filledBoard is a correct fill for emptyBoard
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
		for (int row = 0; row < filledBoard.length; row++)
          {
               StringBuilder rowWord = new StringBuilder();
               for (int col = 0; col<filledBoard[0].length; col++)
               {
                    rowWord.append(filledBoard[row][col]);
               }
               if (dictionary.searchPrefix(rowWord) == 0 || dictionary.searchPrefix(rowWord) == 1)
               {
                    return false;
               }
          }

          for (int col = 0; col < filledBoard[0].length; col++)
          {
               StringBuilder colWord = new StringBuilder();
               for (int row = 0; row<filledBoard.length; row++)
               {
                    colWord.append(filledBoard[row][col]);
               }
               if (dictionary.searchPrefix(colWord) == 0 || dictionary.searchPrefix(colWord) == 1)
               {
                    return false;
               }
          }
          return true;
	}