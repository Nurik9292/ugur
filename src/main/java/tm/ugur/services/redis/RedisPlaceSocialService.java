package tm.ugur.services.redis;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import tm.ugur.dto.SocialNetworkDTO;
import tm.ugur.util.Constant;

@Service
public class RedisPlaceSocialService {

    private final RedisTemplate<String, SocialNetworkDTO> redisTemplate;

    @Autowired
    public RedisPlaceSocialService(RedisTemplate<String, SocialNetworkDTO> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void addInstagram(SocialNetworkDTO social) {
        redisTemplate.opsForValue().set(Constant.PLACE_INSTAGRAM_SOCIAL, social);
    }

    public void addTikTok(SocialNetworkDTO social) {
        redisTemplate.opsForValue().set(Constant.PLACE_TIKTOK_SOCIAL, social);
    }

    public SocialNetworkDTO getInstagram() {
        return redisTemplate.opsForValue().get(Constant.PLACE_INSTAGRAM_SOCIAL);
    }

    public SocialNetworkDTO getTikTok() {
        return redisTemplate.opsForValue().get(Constant.PLACE_TIKTOK_SOCIAL);
    }

    public void delete() {
        redisTemplate.delete(Constant.PLACE_INSTAGRAM_SOCIAL);
        redisTemplate.delete(Constant.PLACE_TIKTOK_SOCIAL);
    }
}
