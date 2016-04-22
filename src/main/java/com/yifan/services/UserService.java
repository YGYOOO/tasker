package com.yifan.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.yifan.domain.users.User;
import com.yifan.repositories.UserRepository;

@Service
public class UserService implements UserDetailsService {
	@Autowired
	private UserRepository userRepository;
	
	public User loadUserByUsername(String username) throws UsernameNotFoundException {		
		User user = userRepository.findByUserName(username);		
		if(user == null) throw new UsernameNotFoundException("username");
		
		User copy = new User.Builder()
				.userName(user.getUsername())
				.password(user.getPassword())
				.roles( user.getRoles())
				.id( user.getId())
				.build();
		return copy;
	}
	
	
	public List<User> getUsers() {
		return userRepository.getUsers();
	}
}
