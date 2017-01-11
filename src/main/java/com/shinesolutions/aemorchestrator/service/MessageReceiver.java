package com.shinesolutions.aemorchestrator.service;

import java.util.concurrent.TimeUnit;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazon.sqs.javamessaging.SQSConnection;

/*
 * Polls the SQS queue checking for messages. If a message is found, then pass it to a message handler
 */
@Component
public class MessageReceiver {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private SQSConnection connection;

	@Autowired
	private MessageConsumer consumer;

	@Autowired
	private SqsMessageHandler messageHandler;

	public void receiveMessages() throws JMSException {
		try {
			while (true) {
				logger.debug("Waiting for messages");
				// TODO figure out what wait time to use
				Message message = consumer.receive(TimeUnit.MINUTES.toMillis(1));

				if (message != null) {
					logger.info("Message received with id: " + message.getJMSMessageID());

					messageHandler.handleMessgae((com.amazonaws.services.sqs.model.Message)message);

					message.acknowledge();
					logger.info("Acknowledged message " + message.getJMSMessageID());
				} else {
					logger.error("Null message received, stopping MessageReceiver");
					break;
				}

			}
		} catch (JMSException e) {
			logger.error("Error receiving from SQS: ", e);
		}
	}

	@PostConstruct
	public void initIt() throws Exception {
		logger.debug("Initialising message receiver, starting SQS connection");
		connection.start();
	}

	@PreDestroy
	public void cleanUp() throws Exception {
		logger.debug("Destroying message receiver, stopping SQS connection");
		connection.stop();
	}

}
