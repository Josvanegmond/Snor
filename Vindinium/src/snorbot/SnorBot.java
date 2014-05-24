package snorbot;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import vindinium.Board.Tile;
import vindinium.Bot;
import vindinium.Direction;
import vindinium.Hero;
import vindinium.State;

public class SnorBot implements Bot
{
	private List<Hero> heroList;
	private Tile[][] tileMatrix;
	private Direction direction;
	
	private Hero hero;
	private Point position = new Point();
	
	private static Direction intToDir( int i )
	{
		Direction[] directions = { Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST };
		return directions[i];
	}
	
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
		this.position.x = this.hero.position.right;
		this.position.y = this.hero.position.left;
		
		this.heroList = state.game.heroes;
		this.tileMatrix = state.game.board.tiles;

		//see if there are free mines
		if( attemptMine() == false )
		{
			//if not, find rich heroes
			if( findRichHero( 10 ) == false )
			{
				//no rich heroes, move randomly
				moveRandom();
			}
			
			//rich hero found!
			else
			{
				//kill the hero
				killHero( this.hero );
			}
		}
	
		System.out.println( " - pos[" + this.position.x + "," + this.position.y + "]" );
		return this.direction;
	}
	
	private boolean findRichHero( int gold )
	{
		for( Hero hero : heroList  )
		{
			if( hero != this.hero && hero.gold > gold )
			{
				return true; //eeew
			}
		}
		
		return false;
	}
	
	private void killHero( Hero hero )
	{
		//if we can't mine this turn, move to hero
		if( attemptMine() == false )
		{
			moveToHero( hero );
		}
	}
	
	private void moveToHero( Hero hero )
	{
		//shortest path to the hero!
		//auto kills when next to player
			
		direction = intToDir( (int)(Math.random() * 4) );
		System.out.print( "Moving to rich bastard" );
	}
	
	private void moveRandom()
	{
		direction = intToDir( (int)(Math.random() * 4) );
		System.out.print( "Randomly wander around" );
	}
	
	private boolean attemptMine()
	{
		boolean canMine = false;
		Tile[] neighbourTiles = getNeighbourTiles( this.position.x, this.position.y );

		for( int i = 0; i < 4 && canMine == false; i++ )
		{
			Tile tile = neighbourTiles[i];
			if( tile == Tile.FREE_MINE )
			{
				canMine = true;
				System.out.print( "Can mine! Mine is in the " + intToDir(i).toString() );
				direction = intToDir( i );
			}
		}
		
		return canMine;
	}
	
	private Tile[] getNeighbourTiles( int x, int y )
	{ 
		int xMax = tileMatrix[0].length;
		int yMax = tileMatrix.length;
		
		Tile[] tiles = new Tile[4];
		
		tiles[0] = ( y - 1 >= 0 ) ? this.tileMatrix[ y - 1 ][ x ] : Tile.WALL;		//NORTH
		tiles[1] = ( x + 1 < xMax ) ? this.tileMatrix[ y ][ x + 1 ] : Tile.WALL;	//EAST
		tiles[2] = ( y + 1 < yMax ) ? this.tileMatrix[ y + 1 ][ x ] : Tile.WALL;	//SOUTH
		tiles[3] = ( x - 1 >= 0 ) ? this.tileMatrix[ y ][ x - 1 ] : Tile.WALL;		//WEST
		
		return tiles;
	};
}
