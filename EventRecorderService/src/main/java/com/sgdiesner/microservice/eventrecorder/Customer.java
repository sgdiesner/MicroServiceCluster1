package com.sgdiesner.microservice.eventrecorder;

public class Customer{
	
	
	
	public Customer() {
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getGivenName() {
		return givenName;
	}
	public String getLastName() {
		return lastName;
	}
	
	private String identifier;

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	private String givenName;
	private String lastName;
	private int age;
	private String gender;
	public int getAge() {
		return age;
	}

	public String getGender() {
		return gender;
	}
	@Override
	public String toString() {
		return "Customer [givenName=" + givenName + ", lastName=" + lastName + ", age=" + age + ", gender=" + gender
				+ "]";
	}
	
	

}