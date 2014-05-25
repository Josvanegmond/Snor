package snorbot;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.Vector;

import vindinium.Board.Tile;
import vindinium.Bot;
import vindinium.Direction;
import vindinium.Hero;
import vindinium.State;

public class SnorBot implements Bot
{
	private PathMap pathMap;
	
	private List<Hero> heroList;
	private Direction direction;
	private Tile[] neighbourTiles;
	
	private Hero hero;
	private Point position;
	
	@Override
	public Direction nextMove(State state)
	{
		// *** BATTLEPLAN ***
		
		//wait for enemies to mine gold (meanwhile, look for free mines)
		//heal (find the beer bar closest to enemy on the route)
		//move in on the kill
		//steal all the gold
		//win
		
		this.direction = Direction.STAY;
		this.hero = state.hero();
		this.position = new Point( this.hero.position.right, this.hero.position.left ); //x and y are reversed, really
		
		this.pathMap = new PathMap( state.game.board.tiles, this.position.x, this.position.y );

		//see if there are free mines in the direct vicinity
		//if not, see if we need a drink
		//if not, find rich heroes
		//no rich heroes, move randomly
		//rich hero found!
		//kill the hero
		
		return this.direction;
	}
}
