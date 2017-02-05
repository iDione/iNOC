package app.config;

import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

@Order(1)
public class SpringWebApplicationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    public SpringWebApplicationInitializer() {
        super();
        System.out.println("()()()()()()()()()()()()()()()()()()()()()()()()()()()()()()()()()()()()()()()()()()()(");
    }
    
    @Override
    protected Class<?>[] getRootConfigClasses() {
        System.out.println("_________________________________________________________________");
        return new Class[] { SpringSecurityConfig.class };
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        return new Class[] { ThymeleafConfig.class };
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }
}
