package com.sgdiesner.microservice.customerlocate;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@RestController
public class Controller {
	private static final Logger logger = LoggerFactory.getLogger(Controller.class);

	@Value("${service.title}")
	private String serviceTitle;

	@Value("${service.locate.customer.givename}")
	private String givenName;

	@Value("${service.locate.customer.surname}")
	private String surname;

	@Value("${service.locate.customer.age}")
	private int age;

	@Value("${service.locate.customer.gender}")
	private String gender;

	//@CrossOrigin
	@RequestMapping(value = "/customer/{value}", method = RequestMethod.GET)
	Customer notify(
			@PathVariable("value") @Size(min = 6, max = 10, message = "Customer id has to be between 6 and 10 characters") @Pattern(regexp = "[0=9]*") String customerId) {
		logger.info("SERVICE=" + serviceTitle);
		logger.info("cusomer id to locate=" + customerId);

	    Customer customer = new Customer();
	    customer.setAge(age);
	    customer.setGender(gender);
	    customer.setGivenName(givenName);
	    customer.setLastName(surname);

	    logger.info("cusomer located=" + customer);
	    
		return customer;

	}


	class Customer {
		
	public Customer(){
			
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

	class Provider {

		public Provider(String n, String l) {
			location = l;
			name = n;
		}

		private String location;
		private String name;

		public String getLocation() {
			return location;
		}

		public String getName() {
			return name;
		}
	}

}
