package snorbot;

import java.awt.Point;
import java.util.Stack;

import vindinium.Board.Tile;
import vindinium.Direction;

public class TileInfo
{
	private Tile tile;
	private int distance;	//should be the same as shortestPath.size(), right?
	private Point position;
	private Stack<Direction> shortestPath;
	
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
		return this.shortestPath;
	}
	
	public void setShortestPath( Stack<Direction> shortestPath )
	{
		this.shortestPath = shortestPath;
	}
}
