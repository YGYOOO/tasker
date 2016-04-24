package com.yifan.controllers;

import java.security.Principal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yifan.domain.things.Task;
import com.yifan.services.TaskService;
import com.yifan.domain.users.User;
import com.yifan.services.UserService;

@RestController
@RequestMapping("/api")
public class ApiController {
	@Autowired
	private UserService userService;
	
	@Autowired
	private TaskService taskService;
	
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
	
	@Secured({"ROLE_ADMIN", "ROLE_USER"} )
	@RequestMapping( value = "/user", method = RequestMethod.GET)
	public User getAuthenticatedUser( Principal p ) {
		User user = userService.loadUserByUsername (p.getName() );
		return user;
	}
}
