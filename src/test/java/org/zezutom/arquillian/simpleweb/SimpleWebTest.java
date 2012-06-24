package org.zezutom.arquillian.simpleweb;

import static org.jboss.arquillian.ajocado.Graphene.id;
import static org.jboss.arquillian.ajocado.Graphene.jq;
import static org.jboss.arquillian.ajocado.Graphene.waitForHttp;
import static org.jboss.arquillian.ajocado.Graphene.waitForXhr;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.jboss.arquillian.ajocado.framework.GrapheneSelenium;
import org.jboss.arquillian.ajocado.locator.IdLocator;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class SimpleWebTest {
	
	public static final String WEBAPP_SRC = "src/main/webapp";
	
	public static final IdLocator USERNAME_FIELD = id("loginForm:username");
	
	public static final IdLocator PASSWORD_FIELD = id("loginForm:password");
	
	public static final IdLocator LOGIN_BUTTON = id("loginForm:login");
	
	public static final IdLocator CHANGE_COLOR_BUTTON = id("menu:changeColor");
	
	public static final IdLocator PAGE_LOAD_TIME_FIELD = id("pageLoadTime");
	
	public static final JQueryLocator LOGGED_IN = jq("li:contains('Welcome')");
	
	@Drone
	private GrapheneSelenium browser;
	
	@ArquillianResource
	private URL deploymentUrl;
	
	private static String pageLoadTime;
		
	@Deployment(testable=false)
	public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "login.war")
                .addClasses(Credentials.class, User.class, Login.class)
                .addAsWebResource(getFile("login.xhtml"))
                .addAsWebResource(getFile("home.xhtml"))                
                .addAsWebInfResource(getFile("faces-config.xml"))
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
	}

	private static File getFile(String filename) {
		return new File(WEBAPP_SRC, filename);
	}
	
	private static URL url(String location) {
		URL url = null;
		try {
			url = new URL(location);
		} catch (MalformedURLException e) {
			fail("Invalid URL: " + e);
		}
		return url;
	}
	
	@Test
	@InSequence(1)
	public void should_login_successfullly() {
		browser.open(url(deploymentUrl + "login.jsf"));
		
		browser.highlight(USERNAME_FIELD);
		browser.type(USERNAME_FIELD, "demo");
		
		browser.highlight(PASSWORD_FIELD);
		browser.type(PASSWORD_FIELD, "demo");
		
		browser.highlight(LOGIN_BUTTON);
		
		waitForHttp(browser).click(LOGIN_BUTTON);	
		captureScreen("successful_login");
		assertTrue("User should be logged in!", browser.isElementPresent(LOGGED_IN));

		// Page load time should be displayed
		pageLoadTime = browser.getText(PAGE_LOAD_TIME_FIELD);
		assertNotNull(pageLoadTime);
		assertFalse(pageLoadTime.isEmpty());		 
	}
		
	@Test
	@InSequence(2)
	public void should_be_grey_by_default() {
		assertColor(User.DEFAULT_COLOR);
	}

	@Test
	@InSequence(3)
	public void should_be_able_to_change_color() {
		changeColor("red");
		changeColor("green");
		changeColor("blue");		
	}	
	
	@Test
	@InSequence(4)
	public void no_page_reload_should_happen() {
		assertEquals(pageLoadTime, browser.getText(PAGE_LOAD_TIME_FIELD));
	}
	
	private void changeColor(String color) {
		final JQueryLocator option = jq("input[value=\"" + color + "\"]");
		browser.highlight(option);
		browser.click(option);
		waitForXhr(browser).click(CHANGE_COLOR_BUTTON);
		captureScreen("color_changed_to_" + color);		
		assertColor(color);
	}
	
	private String colorSelector(String color) {		
		return "div[style='background-color: " + color + ";']";
	}
	
	private void assertColor(String color) {
		assertTrue(color + " expected!", browser.isElementPresent(jq(colorSelector(color))));
	}

	private void captureScreen(String filename) {		
		try {
			ImageIO.write(browser.captureScreenshot(), "png", new File("screenshots/" + filename + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
