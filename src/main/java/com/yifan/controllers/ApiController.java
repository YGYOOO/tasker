package com.yifan.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.yifan.domain.things.Thing;
import com.yifan.services.ThingService;
import com.yifan.domain.users.User;
import com.yifan.services.UserService;

@RestController
@RequestMapping("/api")
public class ApiController {
	@Autowired
	private UserService userService;
	
	@Autowired
	private ThingService thingService;
	
	private User getAuthenticatedUser(String id, Principal p ) {
		User user = userService.loadUserByUsername( p.getName() );
		if( user == null || p == null || !user.getId().equals( id ) ) {
			throw new BadCredentialsException("Invalid user");
		}
		return user;
	}
	
	@RequestMapping(value = "/users/{uid}/tasks", method = RequestMethod.POST )
	@PreAuthorize("hasAnyRole('ROLE_USER') and #user.id == #uid") 
	public Thing createTask( 
			@PathVariable String uid,
			@AuthenticationPrincipal User user,
			@RequestBody Thing thing,
			Principal p ) {
		User mUser = getAuthenticatedUser( uid, p );
		thing.setOwnerId( mUser.getId() );
		thingService.createThing( mUser, thing );
		return thing;
	}
	
	@RequestMapping(value="/users/{uid}/tasks", method = RequestMethod.GET)
	@PreAuthorize("hasAnyRole('ROLE_USER') and #user.id == #uid")
	public List<Thing> getTaks(
			@PathVariable String uid,
			@AuthenticationPrincipal User user,
			Principal p){
		return thingService.getThingsByOwner(user);
	}
	
	@Secured({"ROLE_ADMIN", "ROLE_USER"} )
	@RequestMapping( value = "/user", method = RequestMethod.GET)
	public User getAuthenticatedUser( Principal p ) {
		User user = userService.loadUserByUsername (p.getName() );
		return user;
	}
	
	@RequestMapping(value = "/users/{uid}/things", method = RequestMethod.GET )
	@PreAuthorize("hasAnyRole('ROLE_USER') and #user.id == #uid") 
	//@Secured({"ROLE_ADMIN","ROLE_USER"})
	public String getThings(@PathVariable String uid,
										@AuthenticationPrincipal User user,
										 Principal p ) {
		//User user = getAuthenticatedUser( uid, p );
		return "test";
	}
}
