package snorbot;

import java.awt.Point;
import java.util.Stack;

import vindinium.Board.Tile;
import vindinium.Direction;

public class TileInfo
{
	private Tile tile;
	private int distance;	//is the same as shortestPath.size()
	private Point position;
	private Stack<Direction> shortestPath;
	
	public TileInfo( TileInfo tileInfo )
	{
		this.tile = tileInfo.tile;
		this.distance = tileInfo.distance;
		this.position = (Point) tileInfo.position.clone();
		this.shortestPath = (Stack<Direction>) tileInfo.shortestPath.clone();
	}
	
	public TileInfo( Tile tile, int distance, int x, int y, Stack<Direction> shortestPath )
	{
		this.tile = tile;
		this.distance = distance;
		this.position = new Point( x, y );
		this.shortestPath = shortestPath;
	}
	
	public int getX()
	{
		return position.x;
	}
	
	public int getY()
	{
		return position.y;
	}
	
	public Point getPosition()
	{
		return this.position;
	}
	
	public int getDistance()
	{
		return this.distance;
	}
	
	public void setDistance( int distance )
	{
		this.distance = distance;
	}
	
	public Tile getTile()
	{
		return this.tile;
	}
	
	public Stack<Direction> getShortestPath()
	{
		System.out.println("shortestpath ["+this.getX()+","+this.getY()+"]"+ ": " + this.shortestPath.size() );
		return this.shortestPath;
	}
	
	public void setShortestPath( Stack<Direction> shortestPath )
	{
		this.shortestPath = shortestPath;
	}
	
	public TileInfo clone()
	{
		return new TileInfo( this );
	}
}
