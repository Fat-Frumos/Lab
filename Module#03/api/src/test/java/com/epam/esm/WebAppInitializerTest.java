package com.epam.esm;

import com.epam.esm.config.AppConfig;
import com.epam.esm.config.RestConfig;
import com.epam.esm.config.WebAppInitializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebAppConfiguration
class WebAppInitializerTest {

    private WebAppInitializer webAppInitializer;

    @BeforeEach
    public void setUp() {
        webAppInitializer = new WebAppInitializer();
    }

    @Test
    @DisplayName("getServletMappings should return the expected mapping")
    void getServletMappingsShouldReturnExpectedMapping() {
        String[] expectedMapping = {"/"};
        String[] result = webAppInitializer.getServletMappings();
        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals(expectedMapping[0], result[0]);
        assertArrayEquals(expectedMapping, webAppInitializer.getServletMappings());
    }

    @Test
    @DisplayName("getRootConfigClasses should  return the AppConfig class")
    void getRootConfigClassesShouldReturnExpectedClass() {
        Class<?> expectedClass = AppConfig.class;
        Class<?>[] result = webAppInitializer.getRootConfigClasses();
        assertNotNull(result );
        assertEquals(1, result.length);
        assertEquals(expectedClass, result[0]);
        Class<?>[] expectedClasses = new Class<?>[] {AppConfig.class};
        assertArrayEquals(expectedClasses, webAppInitializer.getRootConfigClasses());
    }

    @Test
    @DisplayName("getServletConfigClasses should return the expected class")
    void getServletConfigClassesShouldReturnExpectedClass() {
        Class<?>[] expectedClasses = new Class<?>[] {RestConfig.class};
        Class<?>[] result = webAppInitializer.getServletConfigClasses();
        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals(expectedClasses[0], result[0]);
        assertArrayEquals(expectedClasses, webAppInitializer.getServletConfigClasses());
    }

    @Test
    void testOnStartup() throws ServletException {
        ServletContext servletContext = mock(ServletContext.class);
        ServletRegistration.Dynamic registration = mock(ServletRegistration.Dynamic.class);
        when(servletContext.addServlet(anyString(), any(DispatcherServlet.class))).thenReturn(registration);
        webAppInitializer.onStartup(servletContext);
        verify(servletContext).addServlet(eq("dispatcher"), any(DispatcherServlet.class));
        verify(registration).setLoadOnStartup(1);
        verify(registration).addMapping("/");
    }
}
