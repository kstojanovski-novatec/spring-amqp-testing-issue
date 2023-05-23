package com.acme.springamqp.testingissue.manyconsumers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:many-consumers.properties")
@RabbitListener(
    id = "worker",
    queues = {"${many.consumers.queue.name.workers}"},
    containerFactory = "defaultContainerFactory",
    messageConverter = "simpleMessageConverter"
)
public class ManyConsumersWorker {

  private static final Logger LOGGER = LoggerFactory.getLogger(ManyConsumersWorker.class);

  private final int snCo;

  public ManyConsumersWorker(int snCo) {
    this.snCo = snCo;
  }


  @RabbitHandler
  public void receiveMessageContent(final String message) {
    LOGGER.info("consumer {},  Received Simple News from Queue Workers: {}", snCo, message);
  }

}

