package com.github.thibstars.jhaapimcp.configuration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for HomeAssistantProperties.
 *
 * @author Thibstars
 */
class HomeAssistantPropertiesTest {

    @Test
    void shouldVerifyDefaultProperties() {
        HomeAssistantProperties homeAssistantProperties = new HomeAssistantProperties();

        Assertions.assertEquals("http://homeassistant.local:8123/api", homeAssistantProperties.getBaseUrl());
        Assertions.assertNull(homeAssistantProperties.getToken());
        Assertions.assertFalse(homeAssistantProperties.isEntityEnabled());
        Assertions.assertEquals("switch.jhaapi_application", homeAssistantProperties.getEntityIdentifier());
        Assertions.assertEquals("JHAAPI Application", homeAssistantProperties.getEntityFriendlyName());
        Assertions.assertTrue(homeAssistantProperties.isEntityReadOnly());
        Assertions.assertFalse(homeAssistantProperties.isEntityShutdownEnabled());
    }

    @Test
    void shouldSetAndRetrieveCustomProperties() {
        HomeAssistantProperties homeAssistantProperties = new HomeAssistantProperties();

        homeAssistantProperties.setBaseUrl("http://custom-host:8123/api");
        homeAssistantProperties.setToken("custom_token_value");
        homeAssistantProperties.setEntityEnabled(true);
        homeAssistantProperties.setEntityIdentifier("switch.custom_application");
        homeAssistantProperties.setEntityFriendlyName("Custom Application");
        homeAssistantProperties.setEntityReadOnly(false);
        homeAssistantProperties.setEntityShutdownEnabled(true);

        Assertions.assertEquals("http://custom-host:8123/api", homeAssistantProperties.getBaseUrl());
        Assertions.assertEquals("custom_token_value", homeAssistantProperties.getToken());
        Assertions.assertTrue(homeAssistantProperties.isEntityEnabled());
        Assertions.assertEquals("switch.custom_application", homeAssistantProperties.getEntityIdentifier());
        Assertions.assertEquals("Custom Application", homeAssistantProperties.getEntityFriendlyName());
        Assertions.assertFalse(homeAssistantProperties.isEntityReadOnly());
        Assertions.assertTrue(homeAssistantProperties.isEntityShutdownEnabled());
    }
}
