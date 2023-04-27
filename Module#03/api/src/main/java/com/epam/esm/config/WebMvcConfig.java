package com.epam.esm.config;

import lombok.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.resource.PathResourceResolver;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
            "classpath:/META-INF/resources/", "classpath:/resources/",
            "classpath:/static/", "classpath:/public/","/favicon.ico" };

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(CLASSPATH_RESOURCE_LOCATIONS)
                .addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS)
                .setCacheControl(CacheControl.maxAge(30, TimeUnit.DAYS))
                .resourceChain(true)
                .addResolver(new PathResourceResolver() {
                    @Override
                    protected Resource getResource(
                            @NonNull String resourcePath,
                            @NonNull Resource location) throws IOException {
                        Resource resource = location.createRelative(resourcePath);
                        return resource.exists()
                                && resource.isReadable()
                                ? resource
                                : new ClassPathResource("/static/favicon.ico");
                    }
                });
    }

    @Bean
    public SimpleUrlHandlerMapping faviconHandlerMapping() {
        SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
        mapping.setOrder(Integer.MIN_VALUE);
        mapping.setUrlMap(Collections.singletonMap("/favicon.ico", faviconRequestHandler()));
        return mapping;
    }

    @Bean
    public ResourceHttpRequestHandler faviconRequestHandler() {
        ResourceHttpRequestHandler requestHandler = new ResourceHttpRequestHandler();
        requestHandler.setLocations(Collections.singletonList(new ClassPathResource("/")));
        requestHandler.setCacheControl(CacheControl.maxAge(30, TimeUnit.DAYS));
        return requestHandler;
    }
}
