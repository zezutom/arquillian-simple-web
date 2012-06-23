package org.zezutom.arquillian.simpleweb;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.thoughtworks.selenium.DefaultSelenium;

@RunWith(Arquillian.class)
public class SimpleWebTest {
	
	public static final String WEBAPP_SRC = "src/main/webapp";
	
	@Drone
	private DefaultSelenium browser;
	
	@ArquillianResource
	private URL deploymentUrl;
	
	@Deployment(testable=false)
	public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "login.war")
                .addClasses(Credentials.class, User.class, Color.class, LoginController.class)
                .addAsWebResource(new File(WEBAPP_SRC, "login.xhtml"))
                .addAsWebResource(new File(WEBAPP_SRC, "home.xhtml"))
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsWebInfResource(
                    new StringAsset("<faces-config version=\"2.0\"/>"),
                    "faces-config.xml");
	}

	@Test
	@InSequence(1)
	public void should_login_successfullly() throws InterruptedException {
		browser.open(deploymentUrl + "login.jsf");
		
		browser.highlight("id=loginForm:username");
		browser.type("id=loginForm:username", "demo");
		
		browser.highlight("id=loginForm:password");
		browser.type("id=loginForm:password", "demo");
		
		browser.highlight("id=loginForm:login");
		browser.click("id=loginForm:login");
		browser.waitForPageToLoad("15000");
		
		assertTrue("User should be logged in!", browser.isElementPresent("xpath=//li[contains(text(), 'Welcome')]"));
	}
		
	@Test
	@InSequence(2)
	public void should_be_grey_by_default() {
		assertColor(Color.GREY);
	}

	@Test
	@InSequence(3)
	public void should_be_able_to_change_color() {
		changeColor(Color.RED);
		changeColor(Color.GREEN);
		changeColor(Color.BLUE);
	}	
		
	private void changeColor(Color color) {
		final String button = "id=colorForm:" + color.getValue();
		browser.highlight(button);
		browser.click(button);
		browser.waitForPageToLoad("15000");
		assertColor(color);
	}
	
	private void assertColor(Color color) {
		final String value = color.getValue();
		assertTrue(value + " expected!", browser.isElementPresent("xpath=//div[@style='background-color: " + value + ";']"));
	}
	
}
