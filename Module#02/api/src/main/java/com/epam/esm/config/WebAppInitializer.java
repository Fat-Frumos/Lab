package com.epam.esm.config;

import org.springframework.lang.NonNull;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.FrameworkServlet;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * WebAppInitializer class is a concrete implementation of the
 * {@link AbstractAnnotationConfigDispatcherServletInitializer}
 * <p>
 * This class is used to configure the web application context
 * and set up the DispatcherServlet in a pure Java-based approach,
 * without the need for any XML configuration files.
 */
public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    /**
     * Returns {@code AppConfig.class} an array of configuration classes
     * which contains configuration for the entire application,
     * such as data source configuration and transaction management.
     *
     * @return an array of root context configuration classes
     */
    @Override
    public Class<?>[] getRootConfigClasses() {
        return new Class[]{AppConfig.class};
    }

    /**
     * Returns {@code RestConfig.class} which contain configuration
     * for the REST web layer, such as resource mapping.
     * Methods to provide the configuration classes
     * for the root context and the DispatcherServlet, respectively.
     *
     * @return an array of servlet context configuration classes
     */
    @Override
    public Class<?>[] getServletConfigClasses() {
        return new Class[]{RestConfig.class};
    }

    /**
     * Returns an array of servlet mapping paths.
     * A single string "/" which maps all incoming requests to the servlet context.
     *
     * @return an array of servlet mapping paths
     */
    @NonNull
    @Override
    public String[] getServletMappings() {
        return new String[]{"/"};
    }

    /**
     * Callback method that is called when
     * the servlet container starts the application.
     * Sets the "spring.profiles.active" context parameter
     * to "dev" to activate the development profile.
     *
     * @param servletContext the servlet context of the application
     * @throws ServletException if an error occurs during application startup
     */
    @Override
    public void onStartup(
            @NonNull final ServletContext servletContext)
            throws ServletException {
        super.onStartup(servletContext);
        servletContext.setInitParameter(
                "spring.profiles.active",
                "dev");
    }

    /**
     * Create a {@link DispatcherServlet} (or other kind of {@link FrameworkServlet}-
     * derived dispatcher) with the specified {@link WebApplicationContext}.
     * <p>Note: This allows for any {@link FrameworkServlet} subclass as of 4.2.3.
     * Previously, it insisted on returning a {@link DispatcherServlet} or subclass thereof.
     *
     * @param context the web application context for the servlet
     * @return a new instance of {@link DispatcherServlet} configured
     * with the specified web application context
     * "throwExceptionIfNoHandlerFound" set to {@code true}
     */
    @NonNull
    @Override
    protected FrameworkServlet createDispatcherServlet(
            final @NonNull WebApplicationContext context) {
        DispatcherServlet dispatcherServlet = new DispatcherServlet(context);
        dispatcherServlet.setThrowExceptionIfNoHandlerFound(true);
        return dispatcherServlet;
    }
}
