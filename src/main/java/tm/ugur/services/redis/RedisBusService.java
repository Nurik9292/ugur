package tm.ugur.services.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import tm.ugur.dto.BusDTO;
import tm.ugur.util.Constant;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    public List<BusDTO> getAll() {
        List<List<BusDTO>> busses = redisTemplate.opsForValue()
                .multiGet(Objects.requireNonNull(redisTemplate.keys(Constant.BUSES_DIVIDED_INTO_ROUTES + "*")));

        List<BusDTO> finnish = new ArrayList<>();
        Objects.requireNonNull(busses).forEach(finnish::addAll);
        return finnish;
    }

    public void deleteBuses(String key){
        redisTemplate.delete(Constant.BUSES_DIVIDED_INTO_ROUTES + key);
    }
}
