package com.cowin.test;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;
import org.testng.log4testng.Logger;

import com.cowin.constants.Constants;
import com.cowin.automation.CoWinScheduleSearchByPin;
import com.cowin.util.ChromeDriverProvider;

import junit.framework.Assert;

public class CoWinSlotCalculatorTest {

	private static Logger LOG = Logger.getLogger(CoWinSlotCalculatorTest.class);

	CoWinScheduleSearchByPin coWinVaccineSearch = new CoWinScheduleSearchByPin(10, Constants.MONTHS.MAY.getValue(),
			Constants.COVISHIELD);

	private WebDriver driver;

	@Test
	public void getAvailableSlotsForCovidByPin() throws Exception {
		LOG.info("getAvailableSlotsForCovid started");

		driver = ChromeDriverProvider.getInstance();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		driver.get("https://www.cowin.gov.in/home");

		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

		Assert.assertEquals("CoWIN", driver.getTitle());

		coWinVaccineSearch.checkForPinSearchEnable();
		coWinVaccineSearch.checkForActiveDays();

		LOG.info("getAvailableSlotsForCovid end");
	}

	@AfterTest
	public void tearDown() {
		LOG.info("Closing  started");
		driver.quit();
	}

}
