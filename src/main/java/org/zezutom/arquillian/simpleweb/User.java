package org.zezutom.arquillian.simpleweb;

import java.io.Serializable;

public class User implements Serializable {

	public static final String DEFAULT_COLOR = "grey";
	
	private static final long serialVersionUID = 1L;

	private String username;

	private String color = DEFAULT_COLOR;
	
	public User() {}
	
	public User(String username) {
		this.username = username;
	}
	
	public String getUsername() {	
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}	
	
	public void setColor(String color) {
		this.color = color;
	}
	
	public String getColor() {		
		return color;
	}
	
	public boolean isAnonymous() {
		return username == null || username.isEmpty();
	}
}
