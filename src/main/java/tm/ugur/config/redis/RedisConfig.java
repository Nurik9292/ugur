package tm.ugur.config.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private Integer port;

    @Bean
    public RedisConnectionFactory redisConnectionFactory(){
        RedisStandaloneConfiguration redisConfiguration = new RedisStandaloneConfiguration(host, port);
        return new JedisConnectionFactory(redisConfiguration);
    }

    @Bean <F, S> RedisTemplate<F, S> redisTemplate(){
        final RedisTemplate<F, S> redisTemplate = new RedisTemplate<>();

        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }
}
