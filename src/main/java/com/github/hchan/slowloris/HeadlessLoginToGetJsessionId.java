package com.github.hchan.slowloris;
import java.io.File;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
/**
 * 
 * @author henry.chan
 * Please download phantomJS
 * return JSESSIONID via OAuth2 Code flow using selenium/phantomJS
 */
public class HeadlessLoginToGetJsessionId {
	
	public static String getJsessionId() throws Exception {
		File file = new File(SlowlorisApp.phantomJSLocation);
        System.setProperty("phantomjs.binary.path", file.getAbsolutePath());
		WebDriver driver = new PhantomJSDriver();
		driver.get(SlowlorisApp.initialUrl);
		WebElement username = driver.findElement(By.id("username"));
		username.sendKeys(SlowlorisApp.USERNAME);
		WebElement password = driver.findElement(By.id("password"));
		password.sendKeys(SlowlorisApp.PASSWORD);
		WebElement login = driver.findElement(By.id("kc-login"));
		login.click();
		String jSessionId = driver.manage().getCookieNamed("JSESSIONID").getValue();
		return jSessionId;
	}
}

