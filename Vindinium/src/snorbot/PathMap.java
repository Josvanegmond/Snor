package snorbot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import vindinium.Board.Tile;
import vindinium.Direction;
import vindinium.Hero;

public class PathMap
{
	public enum TileType
	{
		AIR,
		FREE_MINE,
		TAVERN,
		WALL
	};
	
	private Tile[][] tileMap;
	private TileInfo[][] pathMap;
	private int centerX, centerY, width, height;
	private CollisionHashMap< TileType, TileInfo > siteList;

	private static Direction intToDir( int i )
	{
		Direction[] directions = { Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST };
		return directions[i];
	}
	
	public PathMap( Tile[][] tileMap, int x, int y )
	{
		this.centerX = x;
		this.centerY = y;
		this.width = tileMap[0].length;
		this.height = tileMap.length;
		this.tileMap = tileMap;
		this.pathMap = new TileInfo[ height ][ width ];
		this.siteList = new CollisionHashMap< TileType, TileInfo >();
		
		updatePathMap();
	}
	
	public TileInfo getInfo( int x, int y )
	{
		return pathMap[x][y];
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
		for( int i = 0; i < pathMap.length; i++ )
		{
			for( int j = 0; j < pathMap[0].length; j++ )
			{
				pathMap[i][j] = new TileInfo( tileMap[i][j], -1, i, j, new Stack<Direction>() );
			}
		}
		
		updatePathMap( centerX, centerY, 0, new Stack<Direction>() );
	}
	
	private void updatePathMap( int centerX, int centerY, int distance, Stack<Direction> cameFromDir )
	{
		int checkDistance = pathMap[ centerY ][ centerX ].getDistance();
		
		if( checkDistance > distance || checkDistance == -1 )
		{
			pathMap[ centerY ][ centerX ].setShortestPath( cameFromDir );
			pathMap[ centerY ][ centerX ].setDistance( distance );
			
			Tile[] tiles = getNeighbourTiles( centerX, centerY );
			
			Tile tile = tileMap[ centerY ][ centerX ];

			if( tile == Tile.FREE_MINE ) { siteList.put( TileType.FREE_MINE, pathMap[ centerY ][ centerX ] ); }
			if( tile == Tile.TAVERN ) { siteList.put( TileType.TAVERN, pathMap[ centerY ][ centerX ] ); }
	
			Stack<Direction> cloneCameFromDir = (Stack<Direction>)cameFromDir.clone(); 
			if( tiles[0] != Tile.WALL ) { cloneCameFromDir.push( Direction.SOUTH ); updatePathMap( centerX, centerY-1, distance+1, cloneCameFromDir ); }
			if( tiles[1] != Tile.WALL ) { cloneCameFromDir.push( Direction.WEST ); updatePathMap( centerX+1, centerY, distance+1, cloneCameFromDir ); }
			if( tiles[2] != Tile.WALL ) { cloneCameFromDir.push( Direction.NORTH ); updatePathMap( centerX, centerY+1, distance+1, cloneCameFromDir ); }
			if( tiles[3] != Tile.WALL ) { cloneCameFromDir.push( Direction.EAST ); updatePathMap( centerX-1, centerY, distance+1, cloneCameFromDir ); }
		}
	}
	
	private Tile[] getNeighbourTiles( int x, int y )
	{ 
		int xMax = tileMap[0].length;
		int yMax = tileMap.length;
		
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
		
		for( TileInfo tileInfo : sitesOfType )
		{
			if( tileInfo.getDistance() <= range && tileInfo.getDistance() != -1 )
			{
				sitesInRange.add( tileInfo );
			}
		}
		
		System.out.println( "Found " + sitesInRange.size() + " " + type.name() );
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
}
