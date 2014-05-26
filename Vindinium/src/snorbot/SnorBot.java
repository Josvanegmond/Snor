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
		
		//if a hero has plenty money, kill him
		//otherwise, if we have plenty health, mine all mines close by
		//otherwise, find a pub and get drunk
		
		this.direction = Direction.STAY;
		this.hero = state.hero();
		this.position = new Point( this.hero.position.right, this.hero.position.left ); //x and y are reversed, really
		
		this.pathMap = new PathMap( state, this.position.x, this.position.y );

		//get all interesting sites
		List<TileInfo> minesInRange = this.pathMap.getSitesInRange( TileType.FREE_MINE, 20 );	//mines in vicinity
		List<TileInfo> pubsInRange = this.pathMap.getSitesInRange( TileType.TAVERN, (int)((100-this.hero.life)/10) );		//pubs in vicinity

		//first visit pubs (apparently we need it)
		if( pubsInRange.size() != 0 )
		{
			System.out.print( "Searching closest pubs... " + pubsInRange.get(0).getX() + "," + pubsInRange.get(0).getY() );
			this.direction = this.pathMap.getDirectionTo( pubsInRange.get(0) );
		}
		
		//then visit the mines
		else if( minesInRange.size() != 0 )
		{
			System.out.print( "Searching closest mine... " + minesInRange.get(0).getX() + "," + minesInRange.get(0).getY() );
			this.direction = this.pathMap.getDirectionTo( minesInRange.get(0) );
		}
		
		//else if( pubsInRange.size() != 0 ) 	{ this.direction = pubsInRange.get(0).getShortestPath().lastElement(); }
		
		//if not, find rich heroes and kill them
		
		//no rich heroes, move randomly
		//else { this.direction = intToDir( (int)(Math.random() * 4) ); }
		
		VisualPathMap.getInstance( pathMap );
		
		System.out.println("Going " + this.direction.name() );
		return this.direction;
	}
}
