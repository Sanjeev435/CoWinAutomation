package com.cowin.common;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.cowin.util.ChromeDriverProvider;

public class BaseClass {

	protected WebDriver driver = ChromeDriverProvider.getInstance();

	protected void useJavaScriptClick(WebDriver driver, WebElement element) {
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("arguments[0].click();", element);
	}

	protected void useJavaScriptScrollerForEndOfPage(WebDriver driver) {
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("window.scrollTo(0,document.body.scrollHeight)");
	}

	protected void useJavaScriptScroller(WebDriver driver, String scroller) {
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript(String.format("window.scrollTo(0,document.body.scrollHeight/%s)", scroller));
	}

	protected void implicitWait(int waitTime, TimeUnit timeUnit) {
		driver.manage().timeouts().implicitlyWait(waitTime, timeUnit);
	}

}
