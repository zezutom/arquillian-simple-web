package org.zezutom.arquillian.simpleweb;

import java.io.Serializable;

import javax.annotation.ManagedBean;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

@ManagedBean
@Named
@SessionScoped
public class LoginController implements Serializable {
	
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
	
	public String getColor() {
		return currentUser.getColor();
	}
	
	public void red() {
		setColor(Color.RED);
	}
	
	public void green() {
		setColor(Color.GREEN);
	}
	
	public void blue() {
		setColor(Color.BLUE);
	}
	
	private void setColor(Color color) {
		currentUser.setColor(color);
	}
	private boolean isValid(String username, String password) {
		return "demo".equals(username) && "demo".equals(password);
	}

}
