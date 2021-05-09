package com.cowin.util;

import java.io.File;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * Singleton Class for ChromeDriverProvider
 * 
 * @author Sanjeev
 *
 */
public class ChromeDriverProvider {

	public static String driverLocation = File.separator + "src" + File.separator + "test" + File.separator
			+ "resources" + File.separator + "SeleniumDrivers" + File.separator + "chromedriver_win32_v90.0.4430.24"
			+ File.separator + "chromedriver.exe";
	public static WebDriver driver = null;

	public static WebDriver getInstance() {
		if (driver == null) {
			System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + driverLocation);
			driver = new ChromeDriver();
		}
		return driver;
	}

}
