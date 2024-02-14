package tm.ugur.services.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import tm.ugur.util.StaticParams;


@Service
public class RedisClientRouteNumberService {

    private final RedisTemplate<String, Integer> redisTemplate;


    @Autowired
    public RedisClientRouteNumberService(RedisTemplate<String, Integer> redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    public void addRouteNumber(String key, Integer number){

        redisTemplate.opsForValue().set(StaticParams.ROUTE_NUMBER_FROM_CLIENT + key, number);
    }

    public Integer getRouteNumber(String key){
        return redisTemplate.opsForValue().get(StaticParams.ROUTE_NUMBER_FROM_CLIENT + key);
    }

    public void deleteBuses(String key){
        redisTemplate.delete(StaticParams.ROUTE_NUMBER_FROM_CLIENT + key);
    }
}
