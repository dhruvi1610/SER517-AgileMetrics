package edu.asu.cassess.initializer;

import edu.asu.cassess.config.DBConfig;
import edu.asu.cassess.config.MvcConfig;
import edu.asu.cassess.config.SecurityConfig;
import edu.asu.cassess.config.ServiceConfig;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class MvcWebApplicationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{SecurityConfig.class, ServiceConfig.class, DBConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{MvcConfig.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

}
