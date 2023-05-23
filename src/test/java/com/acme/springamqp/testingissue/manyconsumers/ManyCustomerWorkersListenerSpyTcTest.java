package com.acme.springamqp.testingissue.manyconsumers;

import com.acme.springamqp.testingissue.RabbitMqTestContainer;
import com.acme.springamqp.testingissue.RabbitTemplateTestBeans;
import com.acme.springamqp.testingissue.config.DefaultContainerFactoryConfig;
import com.acme.springamqp.testingissue.config.MessageConverterBeans;
import com.acme.springamqp.testingissue.manyconsumers.config.ManyConsumersConfig;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.test.RabbitListenerTest;
import org.springframework.amqp.rabbit.test.RabbitListenerTestHarness;
import org.springframework.amqp.rabbit.test.mockito.LatchCountDownAndCallRealMethodAnswer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.willAnswer;
import static org.mockito.Mockito.verify;

@Disabled(value = "Works only if the ManyConsumersWorkersListener has one instantiated bean because only then the rabbitMq listener is unique.")
@SpringJUnitConfig
@DirtiesContext
@PropertySource("classpath:different-types.properties")
@ContextConfiguration(
		classes = {
				MessageConverterBeans.class,
				RabbitTemplateTestBeans.class,
				DefaultContainerFactoryConfig.class,
				ManyConsumersConfig.class,
				ManyConsumersWorkersListener.class
		}
)
@RabbitListenerTest
public class ManyCustomerWorkersListenerSpyTcTest extends RabbitMqTestContainer {

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private RabbitListenerTestHarness harness;

	@Value("${many.consumers.queue.name.workers}")
	private String MANY_CUSTOM_WORKERS_QUEUE_NAME;

	@Test
	public void testManyConsumers() throws Exception {
		ManyConsumersWorker manyConsumersWorker = this.harness.getSpy("worker");
		assertThat(manyConsumersWorker).isNotNull();

		LatchCountDownAndCallRealMethodAnswer answer = this.harness.getLatchAnswerFor("worker", 2);
		willAnswer(answer).given(manyConsumersWorker).receiveMessageContent(anyString());

		String bar = "bar";
		String baz = "baz";
		this.rabbitTemplate.convertAndSend(MANY_CUSTOM_WORKERS_QUEUE_NAME, bar);
		this.rabbitTemplate.convertAndSend(MANY_CUSTOM_WORKERS_QUEUE_NAME, baz);
		assertThat(answer.await(10)).isTrue();
		verify(manyConsumersWorker).receiveMessageContent(bar);
		verify(manyConsumersWorker).receiveMessageContent(baz);
	}

}
