package com.cowin.util;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

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
			driver = new ChromeDriver(ChromeDriverProvider.getChromeOptions());
		}
		return driver;
	}
	
	public static ChromeOptions getChromeOptions() {
        final ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("disable-infobars");
        chromeOptions.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
        chromeOptions.setExperimentalOption("useAutomationExtension", false);

        chromeOptions.addArguments("--disable-gpu");
        chromeOptions.addArguments("--disable-extensions");
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--disable-dev-shm-usage");
        chromeOptions.addArguments("--headless");
        chromeOptions.addArguments("--window-size=1920,1200");

        final HashMap<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        chromeOptions.setExperimentalOption("prefs", prefs);

        return chromeOptions;
    }

}
