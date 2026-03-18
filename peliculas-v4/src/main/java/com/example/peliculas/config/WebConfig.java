package com.example.peliculas.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.peliculas.interceptor.AuthInterceptor;
import com.example.peliculas.interceptor.RoleInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;
    private final RoleInterceptor roleInterceptor;

    public WebConfig(AuthInterceptor authInterceptor,
                     RoleInterceptor roleInterceptor) {
        this.authInterceptor = authInterceptor;
        this.roleInterceptor = roleInterceptor;
        
    }

    @Override
    
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/admin/**", "/api/admin/**");

        registry.addInterceptor(roleInterceptor)
                .addPathPatterns("/admin/**", "/api/admin/**");
    }
}