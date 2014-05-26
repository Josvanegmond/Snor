package snorbot;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Stack;

import javax.swing.JFrame;
import javax.swing.JLabel;

import vindinium.State;

public class VisualPathMap extends JFrame implements MouseWheelListener
{
	//we can only have one visual frame, and it shouldn't be recreated if it's already made, so let's make a Singleton
	private static VisualPathMap _instance;
	
	public static VisualPathMap getInstance( PathMap pathMap )
	{
		if( _instance == null )
		{
			_instance = new VisualPathMap( pathMap );
		}
		
		else
		{
			_instance.update( pathMap );
		}
		
		return _instance;
	}
	
	
	private JLabel labels[][];
	private Stack<PathMap> history;
	private int stepsBack = 0;
	
	private VisualPathMap( PathMap pathMap )
	{
		int sizeX = pathMap.getWidth();
		int sizeY = pathMap.getHeight();
		
		this.history = new Stack<PathMap>();
		
		this.setSize( sizeX * 24, sizeY * 24 );
		this.setDefaultCloseOperation( EXIT_ON_CLOSE );
		this.setTitle( "Visual PathMap for Vindinium SnorBot" );
		this.setLocationRelativeTo( null );
		
		this.addMouseWheelListener( this );
		
		this.setLayout( new GridLayout( sizeX, sizeY ) );
		this.labels = new JLabel[sizeX][sizeY];
		
		for( int i = 0; i < sizeX; i++ )
		{
			for( int j = 0; j < sizeY; j++ )
			{
				labels[i][j] = new JLabel("-1");
				this.add( labels[i][j] );
			}
		}
		
		update( pathMap );
		
		this.setVisible( true );
	}
	
	
	//each time update is called, it will keep track of the history
	private void update( PathMap pathMap )
	{
		this.history.push( pathMap );
		display();
	}
		
	private void display()
	{
		PathMap pathMap = this.history.get( this.stepsBack );
		int sizeX = pathMap.getWidth();
		int sizeY = pathMap.getHeight();
		
		for( int y = 0; y < sizeY; y++ )
		{
			for( int x = 0; x < sizeX; x++ )
			{
				TileInfo info = pathMap.getInfo( x, y );
				labels[y][x].setText( info.getDistance() + "" );
				labels[y][x].setForeground( (x == pathMap.getCenterX() && y == pathMap.getCenterY() ) ? Color.red : Color.black );
			}
		}
	}

	@Override
	public void mouseWheelMoved( MouseWheelEvent e )
	{
		float scrollDir = Math.signum( e.getWheelRotation() );
		stepsBack += scrollDir;
		stepsBack = Math.min( this.history.size()-1, Math.max( stepsBack, 0 ) );
		display();
	}
}
