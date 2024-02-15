package tm.ugur.config;

import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

    @Bean
    public GeometryFactory geometryFactory(){
        return new GeometryFactory(new PrecisionModel(), 4326);
    }

    @Bean
    public Lock getLock(){
        return new ReentrantLock();
    }

}
