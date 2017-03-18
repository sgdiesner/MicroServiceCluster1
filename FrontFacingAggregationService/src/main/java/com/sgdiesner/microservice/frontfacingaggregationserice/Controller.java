package com.sgdiesner.microservice.frontfacingaggregationserice;

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

	@Value("${provider.name}")
	private String providerName;

	@Value("${provider.location}")
	private String providerLocation;

	@Value("${customer.locate.uri}")
	private String customerLocateUri;
	

	@Value("${fireandforget.uri}")
	private String fireAndForgetUri;
	

	@CrossOrigin
	@RequestMapping(value = "/notify/customer/{value}", method = RequestMethod.GET)
	String notify(
			@PathVariable("value") @Size(min = 6, max = 10, message = "Customer id has to be between 6 and 10 characters") @Pattern(regexp = "[0=9]*") String customerId) {
		logger.info("cusomer id to locate=" + customerId);
		logger.info("provider.location=" + providerLocation);

		// Check the customer exists
		Customer customer = checkWithCustomerLocateService(customerId);
		if (customer == null) {
			return "Customer " + customerId + " does not exist";
		}

		logger.info("Customer located: " + customer);

		// Send notification event
		customer.setIdentifier(customerId);
		if (!sendNotificationEvent(customer)) {
			return "Problems sending notification event";
		} else {
			return "Customer verified and notification sent";
		}
	}

	private boolean sendNotificationEvent(Customer customer) {
		RestTemplate restTemplate = new RestTemplate();
		try {
			restTemplate.postForEntity(fireAndForgetUri + "/fireandforget/customer", customer, null);
		} catch (HttpClientErrorException hcee) {
			logger.info("Problem: ", hcee);
			HttpStatus status = hcee.getStatusCode();
			switch (status) {
			case NOT_FOUND:
				logger.info("fire and forget service not found error for customer =" + customer.getIdentifier());
				return false;
			default:
				logger.info("error calling customer locate service=" + status, hcee);
				return false;
			}
		} catch (Exception e) {
			logger.info("Unexpected exception for " + customer, e);
			return false;
		}
		return true;
	}

	private Customer checkWithCustomerLocateService(String customerId) {
		RestTemplate restTemplate = new RestTemplate();
		Customer customer = null;
		try {
			customer = restTemplate.getForObject(customerLocateUri + "/customer/" + customerId, Customer.class);
		} catch (HttpClientErrorException hcee) {
			logger.info("Problem: ", hcee);
			HttpStatus status = hcee.getStatusCode();
			switch (status) {
			case NOT_FOUND:
				logger.info("cusomer id not found by customer locate service=" + customerId);
				return null;
			default:
				logger.info("error calling customer locate service=" + status, hcee);
			}
		} catch (Exception e) {
			logger.info("Unexpected exception for " + customerId, e);
			return null;
		}
		return customer;
	}

	@CrossOrigin
	@RequestMapping("/provider")
	Provider provider() {
		logger.info("provider.name=" + providerName);
		logger.info("provider.location=" + providerLocation);
		return new Provider(providerName, providerLocation);
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
