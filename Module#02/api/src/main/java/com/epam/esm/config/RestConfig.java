package com.epam.esm.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * This class implements the {@link WebMvcConfigurer} interface,
 * which provides a convenient way to customize the configuration
 * of the Spring Web MVC framework.
 * <p>
 * The main purpose of this class is to enable CORS
 * (Cross-Origin Resource Sharing) support for the API
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.epam.esm")
public class RestConfig implements WebMvcConfigurer {
    /**
     * Add view controllers for forwarding to the main API URL.
     *
     * @param registry the ViewControllerRegistry to configure
     */
    @Override
    public void addViewControllers(
            final ViewControllerRegistry registry) {
        registry.addViewController("/").
                setViewName("forward:/api/");
    }

    /**
     * Add resource handlers for serving static resources.
     *
     * @param registry the ResourceHandlerRegistry to configure
     */
    @Override
    public void addResourceHandlers(
            final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**")
                .addResourceLocations("/resources/");
    }

    /**
     * Configure cross-origin resource sharing (CORS) support for the API.
     * which allows a web page from one domain to make AJAX requests to another domain.
     *
     * @param registry the CorsRegistry to configure
     */
    @Override
    public void addCorsMappings(final CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE");
    }

    /**
     * Add message converters, which convert between HTTP requests
     * and responses and Java objects using serializing and deserializing
     *
     * @param converters the HttpMessageConverter list to configure
     */
    @Override
    public void configureMessageConverters(
            final List<HttpMessageConverter<?>> converters) {
        converters.add(new MappingJackson2HttpMessageConverter());
    }

    /**
     * Configure content negotiation is the process of selecting
     * the best representation of a resource based on the client's request.
     * Method is called on the configurer object to set the default content type to JSON.
     *
     * @param configurer the ContentNegotiationConfigurer to configure
     */
    @Override
    public void configureContentNegotiation(
            final ContentNegotiationConfigurer configurer) {
        configurer.defaultContentType(MediaType.APPLICATION_JSON);
    }
}
