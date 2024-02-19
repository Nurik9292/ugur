package tm.ugur.services.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import tm.ugur.models.Route;
import tm.ugur.models.Stop;

@Service
public class RedisStartRouteStopService {

    private RedisTemplate<String, String> redisTemplate;

    public void addStartRouteStop(Route route, Stop stop){

    }
}
