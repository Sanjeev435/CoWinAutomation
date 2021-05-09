package com.cowin.automation;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import com.cowin.common.BaseClass;
import com.cowin.constants.Constants;
import com.cowin.dto.VaccineDataAvailabilty;

public class CoWinScheduleSearchByPin extends BaseClass {

	private static final Logger log = Logger.getLogger(CoWinScheduleSearchByPin.class);
	
	
	private static String AVAILABILITY_INFO_BOX_ELEMENT = "./child::div/div/div[2]/ul/li[%s]";

	private static final String VACCINE_AVAILABILITY = "./child::div/div/a";
	private static final String AVAILABILITY_DATE = "./child::div/li[@class='availability-date']/a/p";
	
	private static final String AVAILABILITY_CENTER = "./child::div/div/div[1]/div/h5[@class='center-name-title']";
	private static final String AVAILABILITY_CENTER_LOCATION = "./child::div/div/div[1]/div/p[@class='center-name-text']";
	
	private static final String AVAILABILITY_INFO_BOX = "//*[@class='center-box']/div/div/div";
	private static final String NO_AVAILABILITY_INFO_BOX = "//*[@class='center-box']/div/div/p";
	private static final String VACCINE_FILTER_BOX = "//*[@class='agefilterblock filerandsearchblock']/div";
	
	private static final String NEXT_DATES_BUTTON = "//a[@class='right carousel-control carousel-control-next']";
	
	
	private final Integer appointmentDate;
	private final String appointmentMonth;
	private final String vaccineRequired;
	private final List<VaccineDataAvailabilty> availabiltyData;

	public CoWinScheduleSearchByPin(Integer appointmentDate, String appointmentMonth, String vaccineRequired) {
		this.appointmentDate = appointmentDate;
		this.appointmentMonth = appointmentMonth;
		this.vaccineRequired = vaccineRequired;
		this.availabiltyData = new ArrayList<>();
	}

	/**
	 * If search by PIN is not enable, enable it by clicking
	 */
	public void checkForPinSearchEnable() {
		useJavaScriptScrollerForEndOfPage(driver);

		try {
			driver.findElement(By.className("pin-search"));
		} catch (Exception ex) {
			log.warn("Pin Code search is not enable by default. Enabling....", ex);
			((JavascriptExecutor) driver).executeScript("document.getElementById('status').click();");
		}
	}

	public void checkForActiveDays() throws Exception {

		try {

			log.info("Started checkForActiveDays method");
			WebElement pinCodeWrapper = driver.findElement(By.className("pin-search"));

			Constants.PIN_CODES.forEach(pin -> {
				pinCodeWrapper.findElement(By.tagName("input")).sendKeys(pin); // Input PIN CODE
				driver.findElement(By.className("pin-search-btn")).click(); // Click on Search

				applyFilters();

				useJavaScriptScroller(driver, "3");
				implicitWait(5, TimeUnit.SECONDS); // Wait for 5 seconds for search

				// Keep checking for available slots till no date is hidden
				int count = 0;
				boolean isFutureDatesHidden = true;
				while (isFutureDatesHidden) {
					isFutureDatesHidden = checkForAvailableSlots(isFutureDatesHidden);

					if (isFutureDatesHidden) {
						driver.findElement(By.xpath(CoWinScheduleSearchByPin.NEXT_DATES_BUTTON)).click();
						implicitWait(7, TimeUnit.SECONDS);
					}

					// avoid infinite loops
					if (count > 20) {
						log.info("Found infinite loop while checking for hidden future dates");
						break;
					}
					count++;
				}
			});
			log.info("Ended checkForActiveDays method");
		} catch (Exception ex) {
			log.error("Error while check for Active Days" + ex.getMessage(), ex);
			throw new Exception("Error while check for Active Days" + ex.getMessage());
		}
		printAvailableData();
	}

	private boolean checkForAvailableSlots(boolean isFutureDatesHidden) {
		List<WebElement> dates = driver.findElement(By.className("carousel-inner")).findElements(By.tagName("slide"));

		int rowCount = 1;
		for (WebElement dateElement : dates) {

			// Get all dates which are not hidden
			boolean isHidden = Boolean.valueOf(dateElement.getAttribute("aria-hidden"));
			String date = dateElement.findElement(By.xpath(CoWinScheduleSearchByPin.AVAILABILITY_DATE)).getText();

			log.debug("Validating for date : " + date + " Hidden : " + isHidden);

			if (!isHidden && isDateGreaterThenAppointmentDate(date)) {

				isFutureDatesHidden = false;
				List<WebElement> infoBoxes = driver
						.findElements(By.xpath(CoWinScheduleSearchByPin.AVAILABILITY_INFO_BOX));

				log.debug("Checking availability for date : " + date);
				log.debug("Location Size : " + infoBoxes.size());

				if (!CollectionUtils.isEmpty(infoBoxes)) {
					for (WebElement infoBox : infoBoxes) {
						WebElement vaccineBox = infoBox.findElement(By.xpath(
								String.format(CoWinScheduleSearchByPin.AVAILABILITY_INFO_BOX_ELEMENT, rowCount)));
						String availabilityData = vaccineBox
								.findElement(By.xpath(CoWinScheduleSearchByPin.VACCINE_AVAILABILITY)).getText().trim();

						log.debug("Availability Data for " + date + " is  : " + availabilityData);

						if (!StringUtils.equalsIgnoreCase(availabilityData, Constants.BOOKED_AVAILABILITY_STATUS)
								&& !StringUtils.equalsIgnoreCase(availabilityData, Constants.NA_AVAILABILITY_STATUS)) {

							String centerName = infoBox
									.findElement(By.xpath(CoWinScheduleSearchByPin.AVAILABILITY_CENTER)).getText();
							String centerLocation = infoBox
									.findElement(By.xpath(CoWinScheduleSearchByPin.AVAILABILITY_CENTER_LOCATION))
									.getText();

							log.debug("Availability Center : " + centerName + " Center Location : " + centerLocation);

							this.availabiltyData.add(new VaccineDataAvailabilty(centerName, centerLocation,
									availabilityData, LocalDate.parse(date.trim(),
											DateTimeFormatter.ofPattern(Constants.COWIN_DATE_FORMAT, Locale.US))));
						}
					}
				} else {
					String availabiltyMessage = driver
							.findElement(By.xpath(CoWinScheduleSearchByPin.NO_AVAILABILITY_INFO_BOX)).getText();
					if (Constants.NO_VACCINATION_AVAILABLE_MSG.equalsIgnoreCase(availabiltyMessage)) {
						break;
					}
				}
			} else {
				isFutureDatesHidden = true;
			}
			if (!isHidden) {
				rowCount++;
			}
		}
		return isFutureDatesHidden;
	}

	/**
	 * Apply filter on search elements
	 */
	private void applyFilters() {
		// Select Age 45+
		((JavascriptExecutor) driver).executeScript("document.getElementById('flexRadioDefault3').click();");
		// Select CoviShield
		List<WebElement> filters = driver.findElements(By.xpath(CoWinScheduleSearchByPin.VACCINE_FILTER_BOX));
		for(WebElement filter : filters) {
			String filterName = filter.findElement(By.tagName("label")).getText();
			log.debug("Filter Found : " +filterName);
			if(this.vaccineRequired.equalsIgnoreCase(filterName)) {
				filter.findElement(By.tagName("label")).click();
				break;
			}
		}
	}

	/**
	 * Check dates
	 * @param date
	 * @return flag
	 */
	private boolean isDateGreaterThenAppointmentDate(String date) {
		String[] dateWithMonth = date.split(" ");
		if (Constants.MONTH_ORDER.get(dateWithMonth[1].trim()) > Constants.MONTH_ORDER.get(this.appointmentMonth)) {
			return true;
		} else if (Constants.MONTH_ORDER.get(dateWithMonth[1].trim()) < Constants.MONTH_ORDER
				.get(this.appointmentMonth)) {
			return false;
		} else if (Integer.parseInt(dateWithMonth[0]) >= this.appointmentDate) {
			return true;
		}
		return false;
	}

	public List<VaccineDataAvailabilty> getAvailabiltyData() {
		return this.availabiltyData;
	}

	/**
	 * Print available slots
	 */
	public void printAvailableData() {
		log.info("\n -------------------------- Available Slots Info Starts -------------------------- \n");
		
		getAvailabiltyData().sort(Comparator.comparing(VaccineDataAvailabilty::getAvailabilityDate)
				.thenComparing(VaccineDataAvailabilty::getAvailableSlots, (s1, s2) -> {
		            return s2.compareTo(s1);
		        }));

		getAvailabiltyData().forEach(data -> {
			String date = data.getAvailabilityDate().format(DateTimeFormatter.ofPattern(Constants.COWIN_DATE_FORMAT, Locale.US));
			log.info("Available Date : " + date
					+ " | Slots Available : " + data.getAvailableSlots()
					+ " | Center : " + data.getCenterName()
					+ " | Center Location : " + data.getLocationName());
		});

		log.info("\n -------------------------- Available Slots Info Ends -------------------------- \n");
	}

}
