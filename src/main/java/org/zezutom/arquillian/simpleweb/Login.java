package org.zezutom.arquillian.simpleweb;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;


@ManagedBean
@SessionScoped
public class Login implements Serializable {
	
	public static final String SUCCESS = "Welcome";
	
	public static final String FAILURE = "Incorrect username and password combination";

	private static final long serialVersionUID = 1L;
	
	private final User currentUser = new User();
	
	@Inject
	private Credentials credentials;

	public String login() {
		String view = null;
		
		if (isValid(credentials.getUsername(), credentials.getPassword())) {
			view = "home.xhtml";
			currentUser.setUsername(credentials.getUsername());
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(SUCCESS));
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, FAILURE, FAILURE));
		}
		
		return view;
	}
	
	public boolean isLoggedIn() {
		return !currentUser.isAnonymous();
	}

	public String getUsername() {
		return currentUser.getUsername();
	}

	public void setColor(String color) {
		currentUser.setColor(color);
	}	
	
	public String getColor() {
		return currentUser.getColor();
	}
			
	public String getCurrentTime() {
		return new SimpleDateFormat("HH:mm:ss").format(new Date());
	}
	
	private boolean isValid(String username, String password) {
		return "demo".equals(username) && "demo".equals(password);
	}

}
