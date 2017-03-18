package com.sgdiesner.microservice.messagingservice;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
class Receiver {
	
	private static final Logger logger = LoggerFactory.getLogger(Receiver.class);
	
	@Value("${messaging.queue.name:CustomerQ}")
	private String messagingQueueName;
	
	@Value("${email.uri}")
	private String emailUri;
	
	@Value("${event.uri}")
	private String eventUri;
	
	@Bean
	Queue queue() {
		return new Queue(messagingQueueName, false);
	}
	
	@RabbitListener(queues = "CustomerQ")
    public void processMessage(String customerJsonObject) throws JsonParseException, JsonMappingException, IOException {
       logger.info(customerJsonObject);
       
       Customer customer = new ObjectMapper().readValue(customerJsonObject, Customer.class);
       
       //Send to event recorder service
       callEventRecorder(customer);
       
       //Send to email service
       callEmailService(customer);
    }

	private boolean callEmailService(Customer customer) {
		RestTemplate restTemplate = new RestTemplate();
		try {
			restTemplate.postForEntity(emailUri + "/email/customer", customer, null);
		} catch (HttpClientErrorException hcee) {
			logger.info("Problem: ", hcee);
			HttpStatus status = hcee.getStatusCode();
			switch (status) {
			case NOT_FOUND:
				logger.info("email service not found error for customer =" + customer.getIdentifier());
				return false;
			default:
				logger.info("error calling email service=" + status, hcee);
				return false;
			}
		} catch (Exception e) {
			logger.info("Unexpected email exception for " + customer, e);
			return false;
		}
		return true;
		
	}

	private boolean callEventRecorder(Customer customer) {
		RestTemplate restTemplate = new RestTemplate();
		try {
			restTemplate.postForEntity(eventUri + "/event/customer", customer, null);
		} catch (HttpClientErrorException hcee) {
			logger.info("Problem: ", hcee);
			HttpStatus status = hcee.getStatusCode();
			switch (status) {
			case NOT_FOUND:
				logger.info("event service not found error for customer =" + customer.getIdentifier());
				return false;
			default:
				logger.info("error calling event service=" + status, hcee);
				return false;
			}
		} catch (Exception e) {
			logger.info("Unexpected event recorder exception for " + customer, e);
			return false;
		}
		return true;
	}
	
}