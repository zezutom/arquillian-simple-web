package org.zezutom.arquillian.simpleweb;

import java.io.Serializable;

public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	private String username;

	private Color color = Color.GREY;
	
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
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public String getColor() {		
		return color.getValue();
	}
	
	public boolean isAnonymous() {
		return username == null || username.isEmpty();
	}
}
