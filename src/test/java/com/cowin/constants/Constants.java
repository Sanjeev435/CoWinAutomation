package com.cowin.constants;

import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

public class Constants {
	
	// Hiding default constructor
	private Constants(){
	}
	
	/*
	 * public static final List<String> PIN_CODES = ImmutableList.<String>builder()
	 * .add("110077") .add("110045") .add("110010") .build();
	 */
	
	public static final List<String> PIN_CODES = ImmutableList.<String>builder()
			.add("110039")
			.build();
	
	public static final Map<String, Integer> MONTH_ORDER = ImmutableMap.<String, Integer>builder()
			.put("Jan", 1)
			.put("Feb", 2)
			.put("Mar", 3)
			.put("Apr", 4)
			.put("May", 5)
			.put("Jun", 6)
			.put("Jul", 7)
			.put("Aug", 8)
			.put("Sep", 9)
			.put("Oct", 10)
			.put("Nov", 11)
			.put("Dec", 12)
			.build();
	
	public static final String COVAXIN  = "Covaxin";
	public static final String COVISHIELD = "Covishield";
	public static final String COWIN_DATE_FORMAT = "dd MMM yyyy";
	
	public static final String NA_AVAILABILITY_STATUS = "NA";
	public static final String BOOKED_AVAILABILITY_STATUS = "BOOKED";
	
	public static final String NO_VACCINATION_AVAILABLE_MSG = "No Vaccination center is available for booking.";
	
	
	public enum MONTHS{
		JANUARY("Jan"),
		FEBURARY("Feb"),
		MARCH("Mar"),
		APRIL("Apr"),
		MAY("May"),
		JUNE("Jun"),
		JULY("Jul"),
		AUGUST("Aug"),
		SEPTEMBER("Sep"),
		OCTOBER("Oct"),
		NOVEMBER("Nov"),
		DECEMBER("Dec");
		
		MONTHS(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}

		private String value;
	}

}
