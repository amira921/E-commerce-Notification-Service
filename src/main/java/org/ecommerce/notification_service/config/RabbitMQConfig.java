package org.ecommerce.notification_service.config;

import org.ecommerce.notification_service.listener.OrderMessageListener;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

@Configuration
public class RabbitMQConfig {
    @Value("${rabbitmq.queue.name}")
    private String queue;

    @Bean
    public Queue queue (){
        return new Queue(queue);
    }

   @Bean
    public MessageListenerContainer messageListenerContainer(ConnectionFactory connectionFactory, OrderMessageListener listener) {
        SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer();
        simpleMessageListenerContainer.setConnectionFactory(connectionFactory);
        simpleMessageListenerContainer.setQueues(queue());
        simpleMessageListenerContainer.setMessageListener(listener);
        return simpleMessageListenerContainer;
    }
}
