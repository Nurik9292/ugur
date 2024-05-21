package tm.ugur.config;

import org.apache.catalina.filters.CorsFilter;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Configuration
public class AppConfig implements WebMvcConfigurer {

    @Value("${upload.image}")
    private String uploadPath;


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/api/images/**", "/images/**")
                .addResourceLocations("file:" + uploadPath);

    }


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
