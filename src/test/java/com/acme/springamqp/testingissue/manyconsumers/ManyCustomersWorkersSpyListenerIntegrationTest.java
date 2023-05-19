package com.acme.springamqp.testingissue.manyconsumers;

import com.acme.springamqp.testingissue.RabbitMqTestContainer;
import com.acme.springamqp.testingissue.RabbitTemplateTestBeans;
import com.acme.springamqp.testingissue.config.DefaultContainerFactoryConfig;
import com.acme.springamqp.testingissue.config.MessageConverterBeans;
import com.acme.springamqp.testingissue.manyconsumers.config.ManyConsumersConfig;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ContextConfiguration(
    classes = {
        MessageConverterBeans.class,
        RabbitTemplateTestBeans.class,
        DefaultContainerFactoryConfig.class,
        ManyConsumersConfig.class,
        ManyConsumersWorkersTestListener.class
    }
)
public class ManyCustomersWorkersSpyListenerIntegrationTest extends RabbitMqTestContainer {

  public static final int WANTED_NUMBER_OF_INVOCATIONS_C1 = 1;
  public static final int WANTED_NUMBER_OF_INVOCATIONS_C2 = 1;

  @Autowired
  private RabbitTemplate rabbitTemplate;

  @Value("${many.consumers.exchange.name}")
  private String MANY_TYPES_EXCHANGE_NAME;

  private final ManyConsumersWorker manyConsumersWorker1;
  private final ManyConsumersWorker manyConsumersWorker2;

  ManyCustomersWorkersSpyListenerIntegrationTest(
      @Autowired ManyConsumersWorkersTestListener manyConsumersWorkersListenerSpyInstance
  ) {
    this.manyConsumersWorker1 = manyConsumersWorkersListenerSpyInstance.receiveMessageConsumer1();
    this.manyConsumersWorker2 = manyConsumersWorkersListenerSpyInstance.receiveMessageConsumer2();
  }

  @Test
  public void testDifferentSendingMessages() {
    String message = "Simple Message ...";
    String routingKey = "";
    rabbitTemplate.convertAndSend(MANY_TYPES_EXCHANGE_NAME, routingKey, message);

    try {
      verify(manyConsumersWorker1, times(WANTED_NUMBER_OF_INVOCATIONS_C1))
          .receiveMessageContent(any(String.class));
      verify(manyConsumersWorker2, times(WANTED_NUMBER_OF_INVOCATIONS_C2))
          .receiveMessageContent(any(String.class));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
