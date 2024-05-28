package tm.ugur.config.rabbit;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class RabbitConfig {

//    @Value("${spring.rabbitmq.host}")
//    private String hostName;
//
//    @Bean
//    public ConnectionFactory connectionFactory() {
//        return new CachingConnectionFactory(hostName);
//    }
//
//    @Bean
//    public RabbitTemplate rabbitTemplate() {
//        return new RabbitTemplate(connectionFactory());
//    }
//
//    @Bean
//    public DirectExchange directExchange() {
//        return new DirectExchange("direct-exchange");
//    }
//
//    @Bean
//    public Queue smsQueue() {
//        return new Queue("smsQueue");
//    }
//
//    @Bean
//    public Binding bindingSms() {
//        return BindingBuilder.bind(smsQueue()).to(directExchange()).withQueueName();
//    }
//
//
//    @Bean
//    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
//        return new Jackson2JsonMessageConverter();
//    }

}
