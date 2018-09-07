public class Person {
	private String firstName;
	private String lastName;
	private String spouseFirstName;
	private String spouseLastName;

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getFirstName() {
		return firstName;
	}

	public void setLastName(String lastName) {
			this.lastName = lastName;				
	}
	
	public String getLastName() {
		return lastName;
	}

	public void setSpouseFirstName(String spouseFirstName) {
		if(spouseFirstName == null) {
			this.spouseFirstName = "";
		} else {
			this.spouseFirstName = spouseFirstName;
		}
	}
	
	public String getSpouseFirstName() {
		return spouseFirstName;
	}

	public void setSpouseLastName(String spouseLastName) {
		if(spouseLastName == null){
			this.spouseLastName = "";
		} else {
			this.spouseLastName = spouseLastName;
		}
	}
	public String getSpouseLastName() {
		return spouseLastName;
	}
}