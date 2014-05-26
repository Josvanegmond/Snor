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

		//find the richest hero
		Hero richestHero = state.game.heroes.get(0);
		if( richestHero == this.hero ) { richestHero = state.game.heroes.get(1); } //just grab another hero if we are #0
		
		for( Hero hero : state.game.heroes )
		{
			if( hero != this.hero && hero.gold > richestHero.gold )
			{
				richestHero = hero;
			}
		}
		
		//get all interesting sites
		List<TileInfo> minesInRange = this.pathMap.getSitesInRange( TileType.FREE_MINE, 20 );	//mines in vicinity
		List<TileInfo> takenMinesInRange = this.pathMap.getSitesInRange( TileType.TAKEN_MINE, (100-richestHero.life)/2 );	//mines in vicinity
		List<TileInfo> pubsInRange = this.pathMap.getSitesInRange( TileType.TAVERN, (int)(Math.pow(100-this.hero.life,2)*state.game.board.size/5000) );		//pubs in vicinity
		
		//first visit pubs (apparently we need it)
		if( pubsInRange.size() != 0 )
		{
			this.direction = this.pathMap.getDirectionTo( pubsInRange.get(0) );
		}
		
		//then visit the mines
		else if( minesInRange.size() != 0 )
		{
			this.direction = this.pathMap.getDirectionTo( minesInRange.get(0) );
		}
		
		//then visit taken mines
		else if( takenMinesInRange.size() != 0 )
		{
			this.direction = this.pathMap.getDirectionTo( takenMinesInRange.get(0) );
		}
		
		//then hunt for players
		else
		{
			//kill the richest hero! (but first full health)
			if( this.hero.life > 85 )
			{
				this.direction = this.pathMap.getDirectionTo( richestHero.position.right, richestHero.position.left );
			}
			else
			{
				//run away!!!
				this.direction = intToDir( (int)(Math.random()*4) );
			}
		}
		
		//VisualPathMap.getInstance( pathMap );
		
		return this.direction;
	}
}
