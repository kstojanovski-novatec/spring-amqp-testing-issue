package com.acme.springamqp.testingissue.differenttypes;

import com.acme.springamqp.testingissue.RabbitMqTestContainer;
import com.acme.springamqp.testingissue.RabbitTemplateTestBeans;
import com.acme.springamqp.testingissue.config.DefaultContainerFactoryConfig;
import com.acme.springamqp.testingissue.config.MessageConverterBeans;
import com.acme.springamqp.testingissue.differenttypes.config.DifferentTypesConfig;
import com.acme.springamqp.testingissue.differenttypes.model.SimpleMessage;
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
        DifferentTypesConfig.class,
        DifferentTypesDistributorListenerSpy.class
    }
)
public class DifferentTypesDistributorListenerIntegrationTest extends RabbitMqTestContainer {

  public static final int WANTED_NUMBER_OF_INVOCATIONS_OBJ = 1;
  public static final int WANTED_NUMBER_OF_INVOCATIONS_STR = 1;

  @Autowired
  private RabbitTemplate jsonRabbitTemplate;

  @Value("${different.types.exchange.name}")
  private String DIFFERENT_TYPES_EXCHANGE_NAME;

  private final DifferentTypesDistributorListener simpleMessageDistributorListener;

  DifferentTypesDistributorListenerIntegrationTest(
      @Autowired DifferentTypesDistributorListener simpleMessageDistributorListener
  ) {
    this.simpleMessageDistributorListener = simpleMessageDistributorListener;
  }

  @Test
  public void testDifferentSendingMessages() {
    String message = "Simple Message ...";
    String routingKey = "";
    jsonRabbitTemplate.convertAndSend(DIFFERENT_TYPES_EXCHANGE_NAME, routingKey, message);
    jsonRabbitTemplate.convertAndSend(DIFFERENT_TYPES_EXCHANGE_NAME, routingKey, new SimpleMessage(message));


    try {
      verify(simpleMessageDistributorListener, times(WANTED_NUMBER_OF_INVOCATIONS_STR))
          .receiveGeneralTopicsString(any(String.class));
      verify(simpleMessageDistributorListener, times(WANTED_NUMBER_OF_INVOCATIONS_OBJ))
          .receiveGeneralTopics(any(SimpleMessage.class));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
