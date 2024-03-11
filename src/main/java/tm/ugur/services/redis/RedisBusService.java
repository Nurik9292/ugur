package tm.ugur.services.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import tm.ugur.dto.BusDTO;
import tm.ugur.util.Constant;

import java.util.List;

@Service
public class RedisBusService {

    private final RedisTemplate<String, List<BusDTO>> redisTemplate;


    @Autowired
    public RedisBusService(RedisTemplate<String, List<BusDTO>> redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    public void addBuses(String key, List<BusDTO> busDTO){

        redisTemplate.opsForValue().set(Constant.BUSES_DIVIDED_INTO_ROUTES + key, busDTO);
    }

    public List<BusDTO> getBuses(String key){
        return redisTemplate.opsForValue().get(Constant.BUSES_DIVIDED_INTO_ROUTES + key);
    }

    public void deleteBuses(String key){
        redisTemplate.delete(Constant.BUSES_DIVIDED_INTO_ROUTES + key);
    }
}