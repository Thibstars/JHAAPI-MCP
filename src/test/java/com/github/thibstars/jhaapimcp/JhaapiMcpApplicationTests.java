package com.github.thibstars.jhaapimcp;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

/**
 * Integration test verifying Spring Boot application context startup.
 *
 * @author Thibstars
 */
@SpringBootTest(classes = JhaapiMcpApplication.class)
class JhaapiMcpApplicationTests {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void contextLoads() {
        Assertions.assertNotNull(this.applicationContext);
        Assertions.assertNotNull(this.applicationContext.getBean(ToolCallbackProvider.class));
    }
}
