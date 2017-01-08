package com.idione.inoc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import app.config.ActiveJdbcFilter;

@SpringBootApplication
public class InocApplication {

    public static void main(String[] args) {
        SpringApplication.run(InocApplication.class, args);
    }

    @Bean
    public FilterRegistrationBean activeJdbcFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new ActiveJdbcFilter());
        registration.addUrlPatterns("/*");
        registration.setName("activeJdbcFilter");
        return registration;
    }
}
