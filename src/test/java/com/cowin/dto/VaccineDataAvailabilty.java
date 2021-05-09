package com.cowin.dto;

import java.time.LocalDate;

public class VaccineDataAvailabilty {
	
	private String centerName;
	private String locationName;
	private String availableSlots;
	private LocalDate availabilityDate;
	
	public VaccineDataAvailabilty(){
	}
	
	public VaccineDataAvailabilty(String centerName, String locationName,
			String availableSlots, LocalDate availabilityDate){
		this.centerName = centerName;
		this.locationName = locationName;
		this.availableSlots = availableSlots;
		this.availabilityDate = availabilityDate;
	}
	
	public String getCenterName() {
		return centerName;
	}
	public void setCenterName(String centerName) {
		this.centerName = centerName;
	}
	public String getLocationName() {
		return locationName;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	public String getAvailableSlots() {
		return availableSlots;
	}
	public void setAvailableSlots(String availableSlots) {
		this.availableSlots = availableSlots;
	}
	public LocalDate getAvailabilityDate() {
		return availabilityDate;
	}
	public void setAvailabilityDate(LocalDate availabilityDate) {
		this.availabilityDate = availabilityDate;
	}
	
	

}
