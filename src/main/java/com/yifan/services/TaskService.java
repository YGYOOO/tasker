package com.yifan.services;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yifan.domain.things.Task;
import com.yifan.domain.users.User;
import com.yifan.repositories.TaskRepository;

@Service
public class TaskService {
	@Autowired
	private TaskRepository taskRepository;
	
	public Task createTask( User owner, Task task) {
		task.setId( UUID.randomUUID().toString() );
		task.setOwnerId( owner.getId() );
				
		return taskRepository.saveTask( task );
	}
	
	public List<Task> getTasksByOwner( User user, String incomplete, String overDue ) throws ParseException {
		List<Task> tasks = new ArrayList<Task>();
		tasks = taskRepository.getTasksByOwner(user);
		if(incomplete.equals("true")){
			if(overDue.equals("true"))
				tasks = null;
			else{	
				Iterator<Task> t = tasks.iterator();
				while (t.hasNext()) {
					if(t.next().isCompleted()){
						t.remove();
					}
				}
			}
		}
		else{
			if(overDue.equals("true")){
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				Date date = new Date();
				Iterator<Task> t = tasks.iterator();
				while(t.hasNext()){
					Task t1 = t.next();
					int r = compareDates(df, df.parse(t1.getDue().toString()), df.parse(df.format(date)));
					if(r!=-1 || t1.isCompleted()){
						t.remove();
					}
				}
			}
		}
		return tasks;
	}

	public Task getTask(User user, String tid) {
		Task result = taskRepository.getTask( tid );
		if( result.getOwnerId().equals(user.getId())) {
			return result;
		} else {
			return null;
		}
	
	}
	
	public Task updateTask(User user, Task task){
		if(task.getOwnerId().equals(user.getId())){
			return taskRepository.updateTask(task);
		}
		else{
			return null;
		}
	}

	public Task deleteTask(User user, String tid) {
		Task result = taskRepository.getTask( tid );
		if( result.getOwnerId().equals(user.getId())) {
			return taskRepository.deleteTask( result );
		} else {
			return null;
		}				
	}
	
	 public Integer compareDates(DateFormat df, Date oldDate, Date newDate) {
	        //how to check if two dates are equals in java
	        if (oldDate.equals(newDate)) {
	            System.out.println(df.format(oldDate) + " and " + df.format(newDate) + " are equal to each other");
	            return 0;
	        }

	        //checking if date1 comes before date2
	        if (oldDate.before(newDate)) {
	            System.out.println(df.format(oldDate) + " comes before " + df.format(newDate));
	            return -1;
	        }

	        //checking if date1 comes after date2
	        if (oldDate.after(newDate)) {
	            System.out.println(df.format(oldDate) + " comes after " + df.format(newDate));
	            return 1;
	        }
	        return 0;
	    }
}
