package snorbot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import vindinium.Board.Tile;
import vindinium.Direction;
import vindinium.Hero;
import vindinium.State;

public class PathMap
{
	public enum TileType
	{
		AIR,
		FREE_MINE,
		TAVERN,
		WALL
	};
	
	private State state;
	private Tile[][] tileMap;
	private TileInfo[][] pathMap;
	private int centerX, centerY, width, height;
	private CollisionHashMap< TileType, TileInfo > siteList;

	private static Direction intToDir( int i )
	{
		Direction[] directions = { Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST };
		return directions[i];
	}
	
	public PathMap( PathMap original )
	{
		this.centerX = original.centerX;
		this.centerY = original.centerY;
		this.width = original.width;
		this.height = original.height;
		
		for( int y = 0; y < this.height; y++ )
		{
			for( int x = 0; x < this.width; x++ )
			{
				this.tileMap[y][x] = original.tileMap[y][x];
				this.pathMap[y][x] = original.pathMap[y][x].clone();
			}
		}
		
		this.siteList = original.siteList;
		
		updatePathMap();
	}
	
	public PathMap( State state, int x, int y )
	{
		this.centerX = x;
		this.centerY = y;
		this.tileMap = state.game.board.tiles;
		this.width = tileMap[0].length;
		this.height = tileMap.length;
		this.pathMap = new TileInfo[ height ][ width ];
		this.siteList = new CollisionHashMap< TileType, TileInfo >();
		
		updatePathMap();
	}
	
	public TileInfo getInfo( int x, int y )
	{
		return this.pathMap[y][x];
	}
	
	public CollisionHashMap<TileType, TileInfo> getSiteList()
	{
		return this.siteList;
	}
	
	public Direction getDirectionTo( int x, int y )
	{
		return Direction.STAY;
	}
	
	public void updatePathMap()
	{
		this.siteList.clear();
		
		for( int y = 0; y < this.height; y++ )
		{
			for( int x = 0; x < this.width; x++ )
			{
				this.pathMap[y][x] = new TileInfo( this.tileMap[y][x], -1, x, y, new Stack<Direction>() );
			}
		}
		
		updatePathMap( this.centerX, this.centerY, 0, new Stack<Direction>() );

		for( int y = 0; y < this.height; y++ )
		{
			for( int x = 0; x < this.width; x++ )
			{
				Tile tile = this.tileMap[ y ][ x ];
				if( tile == Tile.FREE_MINE ) { this.siteList.put( TileType.FREE_MINE, this.pathMap[ y ][ x ] ); }
				if( tile == Tile.TAVERN ) { this.siteList.put( TileType.TAVERN, this.pathMap[ y ][ x ] ); }
			}
		}
	}
	
	//updates the pathmap values based on the center position
	private void updatePathMap( int centerX, int centerY, int distance, Stack<Direction> cameFromDir )
	{
		//check if the distance we travelled is shorter than the distance registered for this position, if so, we found a shorter path!
		int checkDistance = this.pathMap[ centerY ][ centerX ].getDistance();
		if( checkDistance > distance || checkDistance == -1 )
		{
			//we register the minimum amount of steps to reach this position
			this.pathMap[ centerY ][ centerX ].setShortestPath( cameFromDir );
			this.pathMap[ centerY ][ centerX ].setDistance( distance );

			//if this position is a mine, wall or a pub, we can't pass through, but otherwise, we continue pathfinding
			Tile currentTile = this.pathMap[ centerY ][ centerX ].getTile();
			if( currentTile != Tile.TAVERN && currentTile != Tile.FREE_MINE  && currentTile != Tile.WALL  )
			{
				//recursive call for all neighbouring tiles, increase distance and clone navigation stack per direction
				Tile[] tiles = getNeighbourTiles( centerX, centerY );
				
				if( tiles[0] != Tile.WALL )
				{ 
					Stack<Direction> cloneCameFromDir = (Stack<Direction>)cameFromDir.clone();
					cloneCameFromDir.push( Direction.SOUTH );
					updatePathMap( centerX, centerY-1, distance+1, cloneCameFromDir );
				}
				
				if( tiles[1] != Tile.WALL )
				{
					Stack<Direction> cloneCameFromDir = (Stack<Direction>)cameFromDir.clone();
					cloneCameFromDir.push( Direction.WEST );
					updatePathMap( centerX+1, centerY, distance+1, cloneCameFromDir );
				}
				
				if( tiles[2] != Tile.WALL )
				{
					Stack<Direction> cloneCameFromDir = (Stack<Direction>)cameFromDir.clone();
					cloneCameFromDir.push( Direction.NORTH );
					updatePathMap( centerX, centerY+1, distance+1, cloneCameFromDir );
				}
				
				if( tiles[3] != Tile.WALL )
				{
					Stack<Direction> cloneCameFromDir = (Stack<Direction>)cameFromDir.clone();
					cloneCameFromDir.push( Direction.EAST );
					updatePathMap( centerX-1, centerY, distance+1, cloneCameFromDir );
				}
			}
		}
	}
	
	//retrieves neighbouring tiles, returns WALL when out of array bounds
	private Tile[] getNeighbourTiles( int x, int y )
	{ 
		int xMax = this.width;
		int yMax = this.height;
		
		Tile[] tiles = new Tile[4];
		
		tiles[0] = ( y - 1 >= 0 ) ? this.tileMap[ y - 1 ][ x ] : Tile.WALL;		//NORTH
		tiles[1] = ( x + 1 < xMax ) ? this.tileMap[ y ][ x + 1 ] : Tile.WALL;	//EAST
		tiles[2] = ( y + 1 < yMax ) ? this.tileMap[ y + 1 ][ x ] : Tile.WALL;	//SOUTH
		tiles[3] = ( x - 1 >= 0 ) ? this.tileMap[ y ][ x - 1 ] : Tile.WALL;		//WEST
		
		return tiles;
	};
	
	public List<TileInfo> getSitesInRange( TileType type, int range )
	{
		List<TileInfo> sitesInRange = new ArrayList<TileInfo>();
		List<TileInfo> sitesOfType = this.siteList.get( type );
		System.out.print(" total sites:" + sitesOfType.size() +  " ");
		
		for( TileInfo tileInfo : sitesOfType )
		{
			if( tileInfo.getDistance() <= range && tileInfo.getDistance() > 0 )
			{
				sitesInRange.add( tileInfo );
			}
		}
		
		System.out.print( "Found " + sitesInRange.size() + " " + type.name() );
		
		System.out.println();
		
		return sitesInRange;
	}
	
	public int getWidth()
	{
		return this.width;
	}
	
	public int getHeight()
	{
		return this.height;
	}

	public int getCenterX()
	{
		return this.centerX;
	}
	
	public int getCenterY()
	{
		return this.centerY;
	}
	
	@Override
	public PathMap clone()
	{
		PathMap pathMap = new PathMap( this );
		return pathMap;
	}
}
