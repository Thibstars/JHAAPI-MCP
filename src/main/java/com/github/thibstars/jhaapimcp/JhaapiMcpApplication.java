package com.github.thibstars.jhaapimcp;

import com.github.thibstars.jhaapimcp.configuration.HomeAssistantProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * Main application entry point for the JHAAPI-MCP server.
 *
 * @author Thibstars
 */
@SpringBootApplication
@EnableConfigurationProperties(HomeAssistantProperties.class)
public class JhaapiMcpApplication {

    public static void main(String[] arguments) {
        SpringApplication.run(JhaapiMcpApplication.class, arguments);
    }
}
