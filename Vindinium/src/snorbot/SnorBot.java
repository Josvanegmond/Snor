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
		
		//wait for enemies to mine gold
		//heal
		//move in on the kill
		//steal all the gold
		//win
		
		this.direction = Direction.STAY;
		this.hero = state.hero();
		this.position.x = this.hero.position.left;
		this.position.y = this.hero.position.right;
		
		heroList = state.game.heroes;
		tileMatrix = state.game.board.tiles;

		if( findRichHero( 10 ) == false )
		{
			if( attemptMine() == false )
			{
				moveRandom();
			}
		}
		
		else
		{
			killHero( hero );
		}
		
		return direction;
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
		System.out.println( "Moving to rich bastard" );
	}
	
	private void moveRandom()
	{
		direction = intToDir( (int)(Math.random() * 4) );
		System.out.println( "Randomly wander around" );
	}
	
	private boolean attemptMine()
	{
		boolean canMine = false;
		List<Tile> neighbourTiles = getNeighbourTiles( this.position.x, this.position.y );

		for( int i = 0; i < 4 && canMine == false; i++ )
		{
			Tile tile = neighbourTiles.get( i );
			if( tile == Tile.FREE_MINE )
			{
				canMine = true;
				System.out.println( "Can mine! Mine is in the " + intToDir(i).toString() );
				direction = intToDir( i );
			}
		}
		
		return canMine;
	}
	
	private List<Tile> getNeighbourTiles( int x, int y )
	{ 
		int yMax = tileMatrix[0].length;
		int xMax = tileMatrix.length;
		
		List<Tile> tiles = new ArrayList<Tile>();
		
		tiles.add( ( this.position.y - 1 >= 0 ) ? this.tileMatrix[ this.position.y - 1 ][ this.position.x ] : Tile.WALL );
		tiles.add( ( this.position.x + 1 <= xMax ) ? this.tileMatrix[ this.position.y ][ this.position.x + 1 ] : Tile.WALL );
		tiles.add( ( this.position.y + 1 < yMax ) ? this.tileMatrix[ this.position.y + 1 ][ this.position.x ] : Tile.WALL );
		tiles.add( ( this.position.x - 1 >= 0 ) ? this.tileMatrix[ this.position.y ][ this.position.x - 1 ] : Tile.WALL );
		
		return tiles;
	};
}
