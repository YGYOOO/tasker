package com.yifan.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yifan.domain.things.Thing;
import com.yifan.domain.users.User;
import com.yifan.repositories.ThingRepository;

@Service
public class ThingService {
	@Autowired
	private ThingRepository thingRepository;
	
	public Thing createThing( User owner, Thing thing) {
		thing.setId( UUID.randomUUID().toString() );
		thing.setOwnerId( owner.getId() );
				
		return thingRepository.saveThing( thing );
	}
	
	public List<Thing> getThingsByOwner( User user ) {
		return thingRepository.getThingsByOwner(user);
	}

	public Thing getThing(User user, String tid) {
		Thing result = thingRepository.getThing( tid );
		if( result.getOwnerId().equals(user.getId())) {
			return result;
		} else {
			return null;
		}
	
	}

	public Thing deleteThing(User user, String tid) {
		Thing result = thingRepository.getThing( tid );
		if( result.getOwnerId().equals(user.getId())) {
			return thingRepository.deleteThing( result );
		} else {
			return null;
		}				
	}
}
