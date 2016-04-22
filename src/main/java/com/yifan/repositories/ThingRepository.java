package com.yifan.repositories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.yifan.domain.things.Thing;
import com.yifan.domain.users.User;

@Repository
public class ThingRepository {
	public Map<String, Thing> things;
	
	public ThingRepository() {
		things = new HashMap<String, Thing>();
	}
	
	public Thing getThing( String id ) {
		return things.get( id );
	}
	
	public Thing saveThing( Thing thing ) {
		thing.setId( UUID.randomUUID().toString() );
		things.put(thing.getId(), thing);
		return thing;
	}
	
	public List<Thing> getThingsByOwner( User owner ) {
		List<Thing> result = new ArrayList<Thing>();
		for( Thing thing : things.values() ) {
			if( thing.getOwnerId() != null &&
				thing.getOwnerId().equals( owner.getId() ) ) {
				result.add( thing );
			}
		}
		return result;
	}
	
	public Thing deleteThing( Thing t ) {
		things.remove( t.getId() );
		return t;
	}
}
