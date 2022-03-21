// class for the tic tac toe board (grid); the methods in this class
// draw the grid, updates it with eeach move, and check for a winner
public class Board
{
  // spaces on the Board (2D array of Space objects)
  private Space[][] spaces;
  
  // size of Board (either 3, 4, 5, or 6)
  private int boardSize;
    
  /**
   * Constructs Space objects and adds them to the spaces array.
   * Also sets up the winning conditions for tic-tac-toe.
   */
	public Board(int size)
	{
    boardSize = size;
    
    // initialize the spaces 2D array
    spaces = new Space[size][size];
    
    // set each space in the spaces 2d array to a new Space object
    // which has a "blank" as the default symbol
    for (int row = 0; row < spaces.length; row++)
    {
      for (int col = 0; col < spaces[0].length; col++)
      {
        spaces[row][col] = new Space();
      }
    }
	}
  
  // getter method which allows the client to specify
  // which specific Space to return
  public Space getSpace(int row, int col)
  {
    // check to make sure the requested Space is in bounds!
    if (row >= 0 && row < spaces.length && col >= 0 && col < spaces[0].length)
    {
      return spaces[row][col];
    }  
    return null;
  }
    
  /**
   * Draws the tic-tac-toe board so that the user can see what is happening.
   * The empty board should look like this:
   *
   *   1|2|3
   *   -----
   *   4|5|6
   *   -----
   *   7|8|9
   *
   * As the players choose space numbers, those spaces will be filled in with Xs and Os.
   *
   *   O|2|3
   *   -----
   *   4|X|6
   *   -----
   *   7|8|9
   *
   * This method goes through each space on the board and checks for BLANKS.
   * If the space is BLANK, it prints the appropriate number.
   * Otherwise, it prints the appropriate symbol.
   */
	public void drawBoard()
	{
    System.out.println();
    
    int spaceNum = 0;

    // draw the grid row by row, column by column
		for (int row = 0; row < spaces.length; row++)
		{
      for (int col = 0; col < spaces[0].length; col++)
      {
        spaceNum++;
        
        Space space = spaces[row][col];
        
        // number all blank spaces 1 thru 9, 16, 25, or 36
        if (space.getSymbol() == Space.BLANK)
        { 
          // add a SPACE in front of single-digit numbers
  			  if (spaceNum <= 9)
          {
            System.out.print(" ");
          }
          System.out.print(spaceNum);
        }
  			else
        {
          System.out.print(" " + space.getSymbol());
        }
        
        // print a vertical divider between each column
			  System.out.print("|");
      }
      
      // print a horizontal divider after each row
      System.out.println();
      
      int numDashes = 3 * boardSize; //determined based on visually seeing what looked best
      for (int j = 0; j < numDashes; j++)
      {
	 	    System.out.print("-");
      }
      System.out.println();
		}
    
		System.out.println();	
	}	

  /**
   * Updates a space based on a player's move by changing it from a blank space to either
   * a an X or O (whichever symbol is associated with player)
   * The method uses an integer (spaceIdx) to determine which space is going to be updated.
   * Method returns TRUE if the space was successfully "occupied", which occurs via the occupySpace
   * method on the space object; if the space was NOT successfully occupied (either because the selected
   * space was outside therange, OR the space was ALREADY occupied by the opposite player), then
   * return false to indicate the space was NOT occupied.
   *
   * @param spaceNum  the selected space of the space to be occupied by player.
   * @param player  the Player taking the turn and attempting to "occupy" the space.
   * @return  true if the move was successful and the space occupied; return false otherwise.
   */
	public boolean recordMove(int spaceNum, Player player)
	{
    // if the user selected a space below 1 or greater than the last
    // number the board, immediately return false
    if (spaceNum < 1 || spaceNum > boardSize * boardSize)
    {
      return false;
    }
  
    int count = 0;
    int rowNum = 0;
    int colNum = 0;
    
    // count through and get the correct index associated with spaceNum
    for (int row = 0; row < spaces.length; row++)
    {
      for (int col = 0; col < spaces[0].length; col++)
      {
        count++;
        
        if (count == spaceNum)
        {
          rowNum = row;
          colNum = col;
          break;
        }
      }
    }
    // try to occupy the space with the Player's symbol, which updates
    // the symbol if the space is currently a numbered "blank" space (and returns true);
    // if the space is already occupied, return false
    String symbol = player.getSymbol();
    boolean spaceSuccessfullyOccupied = spaces[rowNum][colNum].occupySpace(symbol);
    
    return spaceSuccessfullyOccupied;
	}
  
  /**
   *Determines whether or not the board is full (has no BLANK spaces).
   *
   *@return True if there are no BLANKs in any spaces.
   */	
	public boolean isFull()
	{
		for (Space[] row : spaces)
    {
      for (Space space : row)
      {
        if (space.getSymbol() == Space.BLANK)
        {
          return false;
        }
      }
    }
		return true;	
	}

  /**
   * Determine whether or not there is a winner and if so, which symbol won;
   * this method returns the symbol of the winning Player OR returns BLANK if no winner 
   *
   * @return  if there IS a winning condition on the board, appropriately returns
              the winning Player's symbol; if there is NO winning condition and no
              current winner, this method returns BLANK
   */
	public String checkWinner()
	{
    // check for a winner by iterating through the 2D array and checking if all
    // symbols in a particular row,  column, or diagonal are the same
    
    // CHECK ROWS FIRST:
    String rowWinner = checkRows();
    
    if (!rowWinner.equals(Space.BLANK))
    {
      return rowWinner;
    }
    
    // THEN LET'S CHECK COLUMNS:
    String colWinner = checkColumns();
    
    if (!colWinner.equals(Space.BLANK))
    {
      return colWinner;
    }
    
    // FINALLY LET'S CHECK DIAGONALS:
    String diagWinner = checkDiagonals();
    
    if (!diagWinner.equals(Space.BLANK))
    {
      return diagWinner;
    }
    
    // no winner so far
    return Space.BLANK;
	}
   
  /**
   * Determines whether or not any ROW in the grid has all matching symbols
   *
   * @return  if there IS a winning condition in any of the ROWS, appropriately returns
              the winning Player's symbol; if no rows have all the same symbol (i.e. no
              winner across the rows), return a BLANK
   */
	private String checkRows()
	{   
    // check each of the ROWS by traversing row-major order and comparing each symbol with the one
    // that comes after it in the row; early return the moment a winning row is determined
    for (int row = 0; row < spaces.length; row++)
    {
      // get the first symbol in the row
      String firstSymbolInRow = spaces[row][0].getSymbol();
      
      // boolean for tracking if all symbols in the row match (i.e. a win);
      // start it as true, then switch to false when we get a non-matching symbol
      boolean winningRow = true;
      
      // we can start at col = 1 since we are using col = 0 as first symbol
      for (int col = 1; col < spaces[0].length; col++)
      {
        // get the next symbolin the row
        String nextSymbol = spaces[row][col].getSymbol();
                  
        // if the two symbols are NOT the same, set winningRow to false
        if (!firstSymbolInRow.equals(nextSymbol)) 
        {
          winningRow = false;
        }
      }
      
      // if this particular row had all symbols the same, then we have a winner!
      // immediately return early with the String of the winning symbol
      if (winningRow)
      {
        return firstSymbolInRow;
      }
    }
    
    // if we get here, then that means there were no winning rows, so return a BLANK
   	return Space.BLANK;	
	}

  /**
   * Determines whether or not any COLUMN in the grid has all matching symbols
   *
   * @return  if there IS a winning condition in any of the COLUMNS, appropriately returns
              the winning Player's symbol; if no columns have all the same symbol (i.e. no
              winner across the columns), return a BLANK
   */
	private String checkColumns()
	{   
    // check each of the COLUMNS by traversing column-major order and comparing each symbol with the one
    // that comes after it in the column; early return the moment a winning column is determined
    for (int col = 0; col < spaces[0].length; col++)
    {
      // get the first symbol in the column
      String firstSymbolInColumn = spaces[0][col].getSymbol();
      
      // boolean for tracking if all symbols in the column match (i.e. a win);
      // start it as true, then switch to false when we get a non-matching symbol
      boolean winningColumn = true;
      
      // we can start at row = 1 since we are using row = 0 as first symbol
      for (int row = 1; row < spaces.length; row++)
      {
        // get the next symbol in the column
        String nextSymbol = spaces[row][col].getSymbol();
                  
        // if the two symbols are NOT the same, set winningRow to false
        if (!firstSymbolInColumn.equals(nextSymbol)) 
        {
          winningColumn = false;
        }
      }
      
      // if this particular column had all symbols the same, then we have a winner!
      // immediately return early with the String of the winning symbol
      if (winningColumn)
      {
        return firstSymbolInColumn;
      }
    }
    
    // if we get here, then that means there were no winning columns, so return a BLANK
   	return Space.BLANK;	
	}

  /**
   * Determines whether or not either DIAGONAL in the grid has all matching symbols
   *
   * @return  if there IS a winning condition in either of the diagonals, appropriately returns
              the winning Player's symbol; if neither diagonol has all the same symbol (i.e. no
              winner across the diagonals), return a BLANK
   */
	private String checkDiagonals()
	{ 
    /* CHECK TOP LEFT to BOTTOM RIGHT DIAGONAL FIRST: */
    
    // get the first symbol on the diagonal (top-left index)
    String firstSymbol = spaces[0][0].getSymbol();
    
    // boolean for tracking if all symbols in the diagonal match (i.e. a win);
    // start it as true, then switch to false when we get a non-matching symbol
    boolean winningDiag = true;
    
    // set a single for loop to iterate; start at 1 since we manually took out [0][0] above
    for (int count = 1; count < boardSize; count++)
    {
      //get the next symbol on the diagonal, e.g. [1][1], then [2][2], then [3][3], etc.
      String nextSymbol = spaces[count][count].getSymbol();
                  
      // if the two symbols are NOT the same, set winningDiag to false
      if (!firstSymbol.equals(nextSymbol)) 
      {
        winningDiag = false;
      }
    }
    // if this diagonal had all symbols the same, then we have a winner!
    // immediately return early with the String of the winning symbol
    if (winningDiag)
    {
      return firstSymbol;
    }
    
    /* CHECK TOP RIGHT to BOTTOM LEFT DIAGONAL SECOND: */
    
    // get the first symbol on the diagonal (top-right index)
    firstSymbol = spaces[0][boardSize - 1].getSymbol();
    
    winningDiag = true;
    
    // set a single for loop to iterate; start at 1 since we manually took out first symbol above
    for (int count = 1; count < boardSize; count++)
    {
      //get the next symbol on the diagonal
      // for example, for 6x6 grid, the symbol at [0][5] is manually retrieved above,
      // then this loop checks it agains [1][4], then [2][3], then [3][2], etc.
      String nextSymbol = spaces[count][boardSize - count - 1].getSymbol();
                  
      // if the two symbols are NOT the same, set winningDiag to false
      if (!firstSymbol.equals(nextSymbol)) 
      {
        winningDiag = false;
      }
    }
    // if this diagonal had all symbols the same, then we have a winner!
    // immediately return early with the String of the winning symbol
    if (winningDiag)
    {
      return firstSymbol;
    }
    
    /* if we get here, then that means there were no winning diagonal, so return a BLANK */
   	return Space.BLANK;	
	}
}