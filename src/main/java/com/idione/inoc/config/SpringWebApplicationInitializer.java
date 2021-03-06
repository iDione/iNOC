package com.idione.inoc.config;

import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

@Order(1)
public class SpringWebApplicationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    public SpringWebApplicationInitializer() {
        super();
    }
    
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] { SpringSecurityConfig.class };
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] { ThymeleafConfig.class };
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }
}