package com.acme.springamqp.testingissue.differenttypes;

import com.acme.springamqp.testingissue.differenttypes.model.SimpleMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@PropertySource("classpath:different-types.properties")
@Service
public class DifferentTypesSender {

  private static final Logger LOGGER = LoggerFactory.getLogger(DifferentTypesSender.class);

  private final String differentTypesExchangeName;
  private static final String DIFFERENT_TYPES_ROUTING_KEY = "";

  private final RabbitTemplate jsonRabbitTemplate;

    public DifferentTypesSender(
      RabbitTemplate jsonRabbitTemplate,
      @Value("${different.types.exchange.name}") String differentTypesExchangeName
  ) {
    this.jsonRabbitTemplate = jsonRabbitTemplate;
    this.differentTypesExchangeName = differentTypesExchangeName;
  }

  @Scheduled(cron = "${different.types.sender.cron}")
  private void reportCurrentTime() {
    sendSimpleNews("Message 1 ...");
    sendSimpleNews("Message 2 ...");
  }

  void sendSimpleNews(String message) {
    LOGGER.info("Sending following message: {}", message);
    jsonRabbitTemplate.convertAndSend(differentTypesExchangeName, DIFFERENT_TYPES_ROUTING_KEY, message);
    jsonRabbitTemplate.convertAndSend(differentTypesExchangeName, DIFFERENT_TYPES_ROUTING_KEY, new SimpleMessage(message));
  }

}
