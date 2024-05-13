package tm.ugur.services.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import tm.ugur.dto.PlacePhoneDTO;
import tm.ugur.util.Constant;

import java.util.List;
import java.util.Objects;

@Service
public class RedisPlacePhoneService {

    private final RedisTemplate<String, PlacePhoneDTO> redisTemplate;

    @Autowired
    public RedisPlacePhoneService(RedisTemplate<String, PlacePhoneDTO> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void addCity(PlacePhoneDTO city) {
        redisTemplate.opsForValue().set(Constant.PLACE_CITY_PHONE, city);
    }

    public void addMob(PlacePhoneDTO mob) {
        redisTemplate.opsForValue().set(Constant.PLACE_MOB_PHONE + mob.getNumber(), mob);
    }

    public PlacePhoneDTO getCity() {
        return redisTemplate.opsForValue().get(Constant.PLACE_CITY_PHONE);
    }

    public List<PlacePhoneDTO> getMob() {
        return  redisTemplate.opsForValue()
                .multiGet(Objects.requireNonNull(redisTemplate.keys(Constant.PLACE_MOB_PHONE + "*")));
    }

    public void deleteAll() {
        deleteCity();
        deleteMob();
    }

    public void deleteCity() {
        redisTemplate.delete(Constant.PLACE_CITY_PHONE);
    }

    public void deleteMob(){
        redisTemplate.delete(Objects.requireNonNull(redisTemplate.keys(Constant.PLACE_MOB_PHONE + "*")));
    }
}
