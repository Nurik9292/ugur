package tm.ugur.services.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import tm.ugur.dto.BusDTO;
import tm.ugur.dto.TranslationDTO;
import tm.ugur.util.Constant;

import java.util.List;

@Service
public class RedisPlaceTranslationService {

    private final RedisTemplate<String, TranslationDTO> redisTemplate;

    @Autowired
    public RedisPlaceTranslationService(RedisTemplate<String, TranslationDTO> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void addTm(TranslationDTO translation) {
        redisTemplate.opsForValue().set(Constant.PLACE_TM, translation);
    }

    public void addRu(TranslationDTO translation) {
        redisTemplate.opsForValue().set(Constant.PLACE_RU, translation);
    }

    public void addEn(TranslationDTO translation) {
        redisTemplate.opsForValue().set(Constant.PLACE_EN, translation);
    }

    public TranslationDTO getTm() {
        return redisTemplate.opsForValue().get(Constant.PLACE_TM);
    }

    public TranslationDTO getRu() {
        return redisTemplate.opsForValue().get(Constant.PLACE_RU);
    }

    public TranslationDTO getEn() {
        return redisTemplate.opsForValue().get(Constant.PLACE_EN);
    }
}
