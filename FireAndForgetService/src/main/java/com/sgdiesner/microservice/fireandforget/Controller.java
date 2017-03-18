package com.sgdiesner.microservice.fireandforget;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class Controller {
	private static final Logger logger = LoggerFactory.getLogger(Controller.class);

	@Value("${service.title}")
	private String serviceTitle;
	
	@Value("${messaging.queue.name:CustomerQ}")
	private String messagingQueueName;
		
	RabbitMessagingTemplate template;
	
	@Autowired
	Controller(RabbitMessagingTemplate template){
		this.template = template;
	}
	
	public void send(String message){
		template.convertAndSend("CustomerQ", message);
	}

	@Bean
	Queue queue() {
		return new Queue(messagingQueueName, false);
	}

	//@CrossOrigin
	@RequestMapping(value = "/fireandforget/customer", method = RequestMethod.POST)
	String faf(@RequestBody Customer customer) throws JsonProcessingException {
		logger.info("SERVICE=" + serviceTitle);
		logger.info("cusomer id to fire and forget=" + customer.getIdentifier());
		
		send(new ObjectMapper().writeValueAsString(customer));
		
	    
		return "FireAndForget OK";

	}


	
}
