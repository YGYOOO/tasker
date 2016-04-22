package com.yifan.domain.users;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.yifan.domain.users.Role;

@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User implements UserDetails {
	private List<Role> roles;
	private String password;
	private String username;
	private String id;
	public List<Role> getRoles() {
		return roles;
	}
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	private User(User.Builder builder) {
		this.username = builder.username;
		this.id = builder.id;
		this.password = builder.password;
		this.roles = builder.roles;	
	}
	
	public static class Builder {
		private String username;
		private String id;
		private List<Role> roles;
		private String password;
		
		public Builder() {			
		}
		
		public Builder password(String password) {
			this.password = password;
			return this;
		}
		
		public Builder roles(List<Role> roles) {
			this.roles = roles;
			return this;
		}
		
		public Builder id(String id) {
			this.id = id;
			return this;
		}
		
		
		public Builder userName(String username) {
			this.username = username;
			return this;
		}
		
		public User build() {
			return new User(this);
		}
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return roles;
	}
		
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
