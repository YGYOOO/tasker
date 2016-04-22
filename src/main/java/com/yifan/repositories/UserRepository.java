package com.yifan.repositories;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.yifan.domain.users.Role;
import com.yifan.domain.users.User;

@Repository
public class UserRepository {
	public User user1, user2;
	
	public UserRepository(){
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
	}
	
	public User findByUserName(String username) {
		if( user1.getUsername().equals(username)) {
			return user1;
		} else if( user2.getUsername().equals( username) ) {
			return user2;
		} else {
			return null;
		}
	}
	
	public List<User> getUsers() {
		return Arrays.asList( new User[]{ user1, user2 } );
	}
}
