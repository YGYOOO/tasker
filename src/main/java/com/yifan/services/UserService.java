package com.yifan.services;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.yifan.domain.users.Role;
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
	
	public User findUserById(String id) throws UsernameNotFoundException{
		User user = userRepository.findById(id);
		if(user == null) throw new UsernameNotFoundException("userId");
		
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
	
	public User createUser(User user){
		user.setId(UUID.randomUUID().toString());
		user.setPassword(Long.toHexString(Double.doubleToLongBits(Math.random())));
		List<Role> roles = Arrays.asList(new Role[]{new Role("ROLE_USER")});
		user.setRoles(roles);
		
		String GREEN = "\u001B[32;1m";
		String RED = "\u001B[31;1m";
		String CYAN = "\u001B[36;1m";
		String RESET = "\u001B[0m";
		System.out.println(CYAN + "=============================================");
		System.out.println(RESET +  "Initail password for "+ GREEN + user.getUsername() +": " + RED + user.getPassword());
		System.out.println(CYAN + "=============================================");
		
		return userRepository.saveUser(user);
	}
	
	public User updateUser(User user){
		return userRepository.updateUser(user);
	}
	
	public User deleteUser(String uid){
		User user = findUserById(uid);
		return userRepository.deleteUser(user);
	}
}
