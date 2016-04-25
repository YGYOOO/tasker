package com.yifan.controllers;

import java.security.Principal;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yifan.domain.things.Task;
import com.yifan.services.TaskService;
import com.yifan.domain.users.Role;
import com.yifan.domain.users.User;
import com.yifan.services.UserService;

@RestController
@RequestMapping("/api")
public class ApiController {
	@Autowired
	private UserService userService;
	
	@Autowired
	private TaskService taskService;
	
	public final String superAdminName = "Bilbo";
	
	private User getAuthenticatedUser(String id, Principal p ) {
		User user = userService.loadUserByUsername( p.getName() );
		if( user == null || p == null || !user.getId().equals( id ) ) {
			throw new BadCredentialsException("Invalid user");
		}
		return user;
	}
	
	@RequestMapping(value = "/users/{uid}/tasks", method = RequestMethod.POST )
	@PreAuthorize("hasAnyRole('ROLE_USER') and #user.id == #uid") 
	public Task createTask( 
			@PathVariable String uid,
			@AuthenticationPrincipal User user,
			@RequestBody Task task,
			Principal p ) {
		User mUser = getAuthenticatedUser( uid, p );
		task.setOwnerId( mUser.getId() );
		taskService.createTask( mUser, task );
		return task;
	}
	
	@RequestMapping(value="/users/{uid}/tasks", method = RequestMethod.GET)
	@PreAuthorize("hasAnyRole('ROLE_USER') and #user.id == #uid")
	public List<Task> getTaks(
			@PathVariable String uid,
			@RequestParam(defaultValue="false", required=false) String incomplete,
			@RequestParam(defaultValue="false", required=false) String overdue,
			@AuthenticationPrincipal User user,
			Principal p) throws ParseException{
		return taskService.getTasksByOwner(user, incomplete, overdue);
	}
	
	@RequestMapping(value="/users/{uid}/tasks/{tid}", method = RequestMethod.PUT)
	@PreAuthorize("hasAnyRole('ROLE_USER') and #user.id == #uid")
	public Task updateTaks(
			@PathVariable String uid,
			@PathVariable String tid,
			@AuthenticationPrincipal User user,
			@RequestBody Task task,
			Principal p){
		return taskService.updateTask(user, task);
	}
	
	@RequestMapping(value="/users/{uid}/tasks/{tid}", method = RequestMethod.DELETE)
	@PreAuthorize("hasAnyRole('ROLE_USER') and #user.id == #uid")
	public Task deleteTaks(
			@PathVariable String uid,
			@PathVariable String tid,
			@AuthenticationPrincipal User user,
			Principal p){
		return taskService.deleteTask(user, tid);
	}
	
	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	@RequestMapping( value = "/user", method = RequestMethod.GET)
	public User getAuthenticatedUser( @AuthenticationPrincipal User user ) {
		User user1 = userService.findUserById(user.getId() );
		return user1;
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/user", method = RequestMethod.POST)
	public User createUser(@RequestBody User user){
//		if(userService.loadUserByUsername(user.getUsername()) != null)
//			return null;
		try{
			userService.loadUserByUsername(user.getUsername());
	    }catch(UsernameNotFoundException e){
	    	return userService.createUser(user);
	    }
		return null;
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public List<User> getUsers(){
		return userService.getUsers();
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/users/{uid}", method = RequestMethod.PUT)
	public User updateUser(
			@PathVariable String uid,
			@RequestParam(defaultValue="false", required=false) String user,
			@RequestParam(defaultValue="false", required=false) String admin
			){
		User u = userService.findUserById(uid);
		if(u.getUsername().equals(superAdminName))
			return null;
		boolean isUser = true;
		boolean isAdmin = true;
		if(user.equals("false"))
			isUser = false;
		if(admin.equals("false"))
			isAdmin = false;
		List<Role> roles = Arrays.asList(new Role[]{new Role("")});
		if(isUser && isAdmin)
			roles = Arrays.asList( new Role[] { new Role("ROLE_USER"), new Role("ROLE_ADMIN") } );
		else if(isUser && !isAdmin)
			roles = Arrays.asList(new Role[]{new Role("ROLE_USER")});
		else if(!isUser && isAdmin)
			roles = Arrays.asList(new Role[]{new Role("ROLE_ADMIN")});
		u.setRoles(roles);
		return userService.updateUser(u);
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/users/{uid}", method = RequestMethod.DELETE)
	public User deleteUser(@PathVariable String uid){
		User u = userService.findUserById(uid);
		if(u.getUsername().equals(superAdminName))
			return null;
		return userService.deleteUser(uid);
	}
}
