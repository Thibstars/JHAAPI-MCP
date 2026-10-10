package com.github.thibstars.jhaapimcp.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for connecting to a Home Assistant instance via JHAAPI.
 *
 * @author Thibstars
 */
@ConfigurationProperties(prefix = "homeassistant")
public class HomeAssistantProperties {

    private String baseUrl = "http://homeassistant.local:8123/api";

    private String token;

    private boolean entityEnabled = false;

    private String entityIdentifier = "switch.jhaapi_application";

    private String entityFriendlyName = "JHAAPI Application";

    private boolean entityReadOnly = true;

    private boolean entityShutdownEnabled = false;

    public String getBaseUrl() {
        return this.baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isEntityEnabled() {
        return this.entityEnabled;
    }

    public void setEntityEnabled(boolean entityEnabled) {
        this.entityEnabled = entityEnabled;
    }

    public String getEntityIdentifier() {
        return this.entityIdentifier;
    }

    public void setEntityIdentifier(String entityIdentifier) {
        this.entityIdentifier = entityIdentifier;
    }

    public String getEntityFriendlyName() {
        return this.entityFriendlyName;
    }

    public void setEntityFriendlyName(String entityFriendlyName) {
        this.entityFriendlyName = entityFriendlyName;
    }

    public boolean isEntityReadOnly() {
        return this.entityReadOnly;
    }

    public void setEntityReadOnly(boolean entityReadOnly) {
        this.entityReadOnly = entityReadOnly;
    }

    public boolean isEntityShutdownEnabled() {
        return this.entityShutdownEnabled;
    }

    public void setEntityShutdownEnabled(boolean entityShutdownEnabled) {
        this.entityShutdownEnabled = entityShutdownEnabled;
    }
}
