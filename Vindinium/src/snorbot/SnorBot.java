package snorbot;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.Vector;

import snorbot.PathMap.TileType;
import vindinium.Board.Tile;
import vindinium.Bot;
import vindinium.Direction;
import vindinium.Hero;
import vindinium.State;

public class SnorBot implements Bot
{
	public Direction intToDir( int dir )
	{
		Direction[] directions = { Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST };
		return directions[dir];
	}
	
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
		
		if( this.pathMap == null )
		{
			this.pathMap = new PathMap( state.game.board.tiles, this.position.x, this.position.y );
			
			for( int i = 0; i < this.pathMap.getHeight(); i++ )
			{
				for( int j = 0; j < this.pathMap.getWidth(); j++ )
				{
					System.out.print( this.pathMap.getInfo( i, j ).getShortestPath().size() + "\t" );
				}
				System.out.println();
			}
		}
		
		else
		{
			//this.pathMap.updatePathMap();
		}

		//get all interesting sites
		List<TileInfo> minesInRange = this.pathMap.getSitesInRange( TileType.FREE_MINE, 3 );	//mines in vicinity
		List<TileInfo> pubsInRange = this.pathMap.getSitesInRange( TileType.TAVERN, 100-this.hero.life );		//pubs in vicinity

		//visit those sites
		if( minesInRange.size() != 0 ) 		{ this.direction = minesInRange.get(0).getShortestPath().lastElement(); }
		else if( pubsInRange.size() != 0 ) 	{ this.direction = pubsInRange.get(0).getShortestPath().lastElement(); }
		
		//if not, find rich heroes and kill them
		
		//no rich heroes, move randomly
		else { this.direction = intToDir( (int)(Math.random() * 4) ); }
		
		return this.direction;
	}
}
