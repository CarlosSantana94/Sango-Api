package sango.bucapps.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
class WebMvcCofig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
       /* registry.addMapping("/**")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedOriginPatterns("*")
                .allowCredentials(false)
                .allowedHeaders("*");*/
    }


}
