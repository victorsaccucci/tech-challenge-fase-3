package serviceschedule_api.fiap.serviceschedule_api.common;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    
    public static final String APPOINTMENT_EXCHANGE = "appointment.exchange";
    public static final String NOTIFICATION_QUEUE = "appointment.notification.queue";
    public static final String NOTIFICATION_ROUTING_KEY = "appointment.notification";
    
    @Bean
    public TopicExchange appointmentExchange() {
        return new TopicExchange(APPOINTMENT_EXCHANGE);
    }
    
    @Bean
    public Queue notificationQueue() {
        return QueueBuilder.durable(NOTIFICATION_QUEUE).build();
    }
    
    @Bean
    public Binding notificationBinding() {
        return BindingBuilder
                .bind(notificationQueue())
                .to(appointmentExchange())
                .with(NOTIFICATION_ROUTING_KEY);
    }
}