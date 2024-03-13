package tm.ugur.config.cors;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfiguration implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Применить ко всем эндпоинтам
                .allowedOrigins("http://192.168.37.61:8083") // Разрешить запросы из всех источников
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE") // Разрешенные HTTP методы
                .allowedHeaders("*"); // Разрешить все заголовки
    }
}
