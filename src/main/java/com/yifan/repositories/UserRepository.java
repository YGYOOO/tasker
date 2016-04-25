package com.yifan.repositories;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.yifan.domain.users.Role;
import com.yifan.domain.users.User;

@Repository
public class UserRepository {
	public User user1, user2;
	public Map<String, User> users;
	
	public UserRepository(){
		users = new HashMap<String, User>();
		List<Role> roles = Arrays.asList(new Role[]{new Role("ROLE_USER")});
		user1 = new User.Builder()
				.userName("yifan")
				.password("123")
				.roles(roles)
				.id( UUID.randomUUID().toString() )
				.build();
		
		roles = Arrays.asList( new Role[] { new Role("ROLE_USER"), new Role("ROLE_ADMIN") } );
		user2 = new User.Builder()
				.userName("erfan")
				.password("123")
				.roles(roles)
				.id( UUID.randomUUID().toString() )
				.build();
	
		users.put(user1.getId(), user1);
		users.put(user2.getId(), user2);
	}
	
	public User findByUserName(String username) {
		User u = null;
		for(User user: users.values()){
			if(user.getUsername().equals(username))
				u = user;
		}
		return u;
	}
	
	public User findById(String id) {
		User u = null;
		for(User user: users.values()){
			if(user.getId().equals(id))
				u = user;
		}
		return u;
	}
	
	public List<User> getUsers() {
		List<User> result = new ArrayList<User>();
		for(User user: users.values()){
			result.add(user);
		}
		return result;
	}
	
	public User saveUser(User user){
		users.put(user.getId(), user);
		return user;
	}
	
	public User updateUser(User user){
		users.put(user.getId(), user);
		return user;
	}
	
	public User deleteUser(User user){
		users.remove(user.getId());
		return user;
	}
}
