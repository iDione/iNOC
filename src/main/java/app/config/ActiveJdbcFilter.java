package app.config;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.javalite.activejdbc.Base;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

// Using this filter until we figure out jndi for the application, once thats done we can use the class directly from activejdbc.web
/**
 * This is a filter for opening a connection before and closing connection after servlet.
 * Example of configuration:
 * <pre>
     &lt;filter&gt;
        &lt;filter-name&gt;activeJdbcFilter&lt;/filter-name&gt;
        &lt;filter-class&gt;org.javalite.activejdbc.web.ActiveJdbcFilter&lt;/filter-class&gt;
        &lt;init-param&gt;
            &lt;param-name&gt;jndiName&lt;/param-name&gt;
            &lt;param-value&gt;jdbc/test_jndi&lt;/param-value&gt;
        &lt;/init-param&gt;
    &lt;/filter&gt;
 * </pre>
 * @author Igor Polevoy
 */
@Component
public class ActiveJdbcFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActiveJdbcFilter.class);

    // private String jndiName;

    @Override
    public void init(FilterConfig config) throws ServletException {
        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
        // jndiName = config.getInitParameter("jndiName");
        // if(jndiName == null)
        // throw new IllegalArgumentException("must provide jndiName parameter
        // for this filter");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        long before = System.currentTimeMillis();
        try {
            // Base.open(jndiName);
            Base.open();
            Base.connection().setAutoCommit(true);
            // Base.openTransaction();
            chain.doFilter(req, resp);
            // Base.commitTransaction();
        } catch (IOException e) {
            // Base.rollbackTransaction();
            throw e;
        } catch (ServletException e) {
            // Base.rollbackTransaction();
            throw e;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            Base.close();
        }
        LOGGER.info("Processing took: {} milliseconds", System.currentTimeMillis() - before);
    }

    @Override
    public void destroy() {
    }
}