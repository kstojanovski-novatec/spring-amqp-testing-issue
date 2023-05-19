# Spring AMQP testing issue

##

Running the application works as expected, but exception appears on integration test.

## Quickstart

Quick-start instruction of this application which works well.

1. Start the RabbitMQ container (docker-compose\docker-compose.yml). 
```
services:
  rabbitmq:
    image: rabbitmq:management
    ports:
      - "5672:5672"
      - "15672:15672"
```
2. Start the application (The different types part is started trough scheduler).
3. If you want to start the other part "many consumers" start modify the property files by setting the cron value to "-" in the different-types.properties file and modifying the many-consumers.property file.

## Issue

The test-container tests where classes with @RabbitListener and @RabbitHandler are used cause trouble by throwing AmqpException (`Caused by: org.springframework.amqp.AmqpException: Ambiguous methods for payload type`).

It this the expected behaviour, and if yes how those classes can be tested?