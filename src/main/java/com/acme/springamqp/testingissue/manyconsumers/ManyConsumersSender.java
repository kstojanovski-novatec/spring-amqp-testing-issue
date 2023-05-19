package com.acme.springamqp.testingissue.manyconsumers;

import com.acme.springamqp.testingissue.differenttypes.model.SimpleMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@PropertySource("classpath:many-consumers.properties")
@Service
public class ManyConsumersSender {

  private static final Logger LOGGER = LoggerFactory.getLogger(ManyConsumersSender.class);

  private final String manyConsumersExchangeName;
  private static final String DIFFERENT_TYPES_ROUTING_KEY = "";

  private final RabbitTemplate rabbitTemplate;

    public ManyConsumersSender(
      RabbitTemplate rabbitTemplate,
      @Value("${many.consumers.exchange.name}") String manyConsumersExchangeName
  ) {
    this.rabbitTemplate = rabbitTemplate;
    this.manyConsumersExchangeName = manyConsumersExchangeName;
  }

  @Scheduled(cron = "${many.consumers.sender.cron}")
  private void reportCurrentTime() {
    sendSimpleNews("News 1 ...");
    sendSimpleNews("News 2 ...");
  }

  void sendSimpleNews(String message) {
    LOGGER.info("Sending following message: {}", message);
    rabbitTemplate.convertAndSend(manyConsumersExchangeName, DIFFERENT_TYPES_ROUTING_KEY, message);
  }

}
