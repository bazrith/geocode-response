public class Address {
	private String streetNumber;
	private String streetName;
	private String streetType;
	private String streetOrientation;
	private String cityName;
	private String province;
	private String postCode;

	public void setStreetNumber(String streetNumber) {
		if(streetNumber.contains("-"))
			this.streetNumber = streetNumber.split("-")[1];
		else 
			this.streetNumber = streetNumber;
	}

	public String getStreetNumber() {
		return streetNumber;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public String getStreetName() {
		return streetName;
	}

	public void setStreetType(String streetType) {
		this.streetType = streetType;
	}

	public String getStreetType() {
		return streetType;
	}

	public void setStreetOrientation(String streetOrientation) {
		if (streetOrientation != null && streetOrientation.length() != 0) {
			this.streetOrientation = streetOrientation.substring(0, 1);	
		} else {
			this.streetOrientation = "";
		}		
	}

	public String getStreetOrientation() {
		return streetOrientation;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getProvince() {
		return province;
	}

	public void setPostalCode(String postCode) {
		this.postCode = postCode;
	}

	public String getPostalCode() {
		return postCode;
	}
}
