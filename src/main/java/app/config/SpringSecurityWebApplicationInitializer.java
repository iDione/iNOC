package app.config;

import org.springframework.core.annotation.Order;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

@Order(2)
public class SpringSecurityWebApplicationInitializer extends AbstractSecurityWebApplicationInitializer {

    public SpringSecurityWebApplicationInitializer() {
        super();
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
    }
}
