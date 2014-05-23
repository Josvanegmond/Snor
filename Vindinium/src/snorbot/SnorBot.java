package snorbot;

import vindinium.Bot;
import vindinium.Direction;
import vindinium.State;

public class SnorBot implements Bot
{
	@Override
	public Direction nextMove(State state)
	{
		// *** BATTLEPLAN ***
		
		//wait for enemies to mine gold
		//heal
		//move in on the kill
		//steal all the gold
		//win
		
		return Direction.STAY;
	}
}
