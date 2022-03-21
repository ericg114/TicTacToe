// class representing a Player;
// each player has a unique symbol
public class Player
{
  public static final String[] playerSymbols = {"X", "O", "$", "#"}; 

  // instance variable marked as FINAL
  // once it's set in the constructor, it stays!
	private final String symbol;

	public Player (String symbol)
	{
		this.symbol = symbol;
	}
	
	public String getSymbol()
  {
    return symbol;
  }
}