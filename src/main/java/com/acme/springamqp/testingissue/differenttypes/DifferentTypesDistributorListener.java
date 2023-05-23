package com.acme.springamqp.testingissue.differenttypes;

import com.acme.springamqp.testingissue.differenttypes.model.SimpleMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;


@Service
@PropertySource("classpath:different-types.properties")
@RabbitListener(
    id="message-types",
    queues = {"${different.types.queue.name.message-types}"},
    containerFactory = "defaultContainerFactory",
    messageConverter = "jackson2Converter"
)
public class DifferentTypesDistributorListener {

  private static final Logger LOGGER = LoggerFactory.getLogger(DifferentTypesDistributorListener.class);

  @RabbitHandler
  public void receiveGeneralTopicsString(String message) {
    LOGGER.info("Received message as String: " + message);
  }

  @RabbitHandler
  public void receiveGeneralTopics(final SimpleMessage simpleMessage) {
    LOGGER.info("Received message as SimpleMessage: '{}'.", simpleMessage.messageContent());
  }

}
