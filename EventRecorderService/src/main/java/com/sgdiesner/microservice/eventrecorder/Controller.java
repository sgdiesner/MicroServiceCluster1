package com.sgdiesner.microservice.eventrecorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
public class Controller {
	private static final Logger logger = LoggerFactory.getLogger(Controller.class);

	@Value("${service.title}")
	private String serviceTitle;

	@RequestMapping(value = "/event/customer", method = RequestMethod.POST)
	String faf(@RequestBody Customer customer) throws JsonProcessingException {
		logger.info("SERVICE=" + serviceTitle);
		logger.info("cusomer id to event=" + customer.getIdentifier());

		logger.info("customer evented=" + customer);

		return "Event OK";
	}
}
