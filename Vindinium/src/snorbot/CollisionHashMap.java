package snorbot;

import java.util.ArrayList;
import java.util.HashMap;

public class CollisionHashMap<K,V>
{
	private HashMap<K,ArrayList<V>> hashMap;
	
	public CollisionHashMap()
	{
		this.hashMap = new HashMap<K,ArrayList<V>>();
	}
	
	public ArrayList<V> put( K key, V value )
	{
		ArrayList<V> list = this.hashMap.get( key );
		
		if( list == null )
		{
			list = new ArrayList<V>();
			this.hashMap.put( key, list );
		}
		
		list.add( value );
		return list;
	}
	
	public ArrayList<V> get( K key )
	{
		ArrayList<V> list = this.hashMap.get( key );
		if( list == null ) { list = new ArrayList<V>(); }
		
		return list;
	}

	public void clear()
	{
		this.hashMap.clear();
	}
}
