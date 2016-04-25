package com.yifan.repositories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.yifan.domain.things.Task;
import com.yifan.domain.users.User;

@Repository
public class TaskRepository {
	public Map<String, Task> tasks;
	
	public TaskRepository() {
		tasks = new HashMap<String, Task>();
	}
	
	public Task getTask( String id ) {
		return tasks.get( id );
	}
	
	public Task saveTask( Task task ) {
		tasks.put(task.getId(), task);
		return task;
	}
	
	public List<Task> getTasksByOwner( User owner ) {
		List<Task> result = new ArrayList<Task>();
		for( Task task : tasks.values() ) {
			if( task.getOwnerId() != null &&
				task.getOwnerId().equals( owner.getId() ) ) {
				result.add( task );
			}
		}
		return result;
	}
	
	public Task updateTask(Task t){
		tasks.put(t.getId(), t);
		return t;
	}
	
	public Task deleteTask( Task t ) {
		tasks.remove( t.getId() );
		return t;
	}
}
