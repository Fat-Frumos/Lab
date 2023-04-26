package com.epam.esm.config;

import org.springframework.lang.NonNull;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.FrameworkServlet;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

        @Override
    public Class<?>[] getRootConfigClasses() {
        return new Class[]{AppConfig.class};
    }

        @Override
    public Class<?>[] getServletConfigClasses() {
        return new Class[]{RestConfig.class};
    }

        @NonNull
    @Override
    public String[] getServletMappings() {
        return new String[]{"/"};
    }

        @Override
    public void onStartup(
            @NonNull final ServletContext servletContext)
            throws ServletException {
        super.onStartup(servletContext);
        servletContext.setInitParameter(
                "spring.profiles.active",
                "dev");
    }

        @NonNull
    @Override
    protected FrameworkServlet createDispatcherServlet(
            final @NonNull WebApplicationContext context) {
        DispatcherServlet dispatcherServlet = new DispatcherServlet(context);
        dispatcherServlet.setThrowExceptionIfNoHandlerFound(true);
        return dispatcherServlet;
    }
}
