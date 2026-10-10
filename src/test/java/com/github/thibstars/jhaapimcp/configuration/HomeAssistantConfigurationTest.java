package com.github.thibstars.jhaapimcp.configuration;

import com.github.thibstars.jhaapi.Configuration;
import com.github.thibstars.jhaapi.client.calendars.CalendarService;
import com.github.thibstars.jhaapi.client.config.ConfigService;
import com.github.thibstars.jhaapi.client.errors.ErrorLogService;
import com.github.thibstars.jhaapi.client.events.EventService;
import com.github.thibstars.jhaapi.client.history.HistoryService;
import com.github.thibstars.jhaapi.client.logbook.LogbookService;
import com.github.thibstars.jhaapi.client.services.LightService;
import com.github.thibstars.jhaapi.client.services.MediaService;
import com.github.thibstars.jhaapi.client.services.ServiceService;
import com.github.thibstars.jhaapi.client.services.SwitchService;
import com.github.thibstars.jhaapi.client.states.EntityManagementService;
import com.github.thibstars.jhaapi.client.states.StatesService;
import com.github.thibstars.jhaapi.client.status.StatusService;
import com.github.thibstars.jhaapi.client.sun.SunService;
import com.github.thibstars.jhaapi.client.templates.TemplateService;
import com.github.thibstars.jhaapi.client.updates.UpdateService;
import com.github.thibstars.jhaapimcp.tools.HomeAssistantTools;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.ai.tool.ToolCallbackProvider;

/**
 * Unit tests for HomeAssistantConfiguration.
 *
 * @author Thibstars
 */
class HomeAssistantConfigurationTest {

    @Test
    void shouldCreateAllJhaapiServiceBeans() {
        HomeAssistantConfiguration homeAssistantConfiguration = new HomeAssistantConfiguration();
        HomeAssistantProperties homeAssistantProperties = new HomeAssistantProperties();
        homeAssistantProperties.setBaseUrl("http://localhost:8123/api");
        homeAssistantProperties.setToken("test_token_123");

        Configuration configuration = homeAssistantConfiguration.jhaapiConfiguration(homeAssistantProperties);
        Assertions.assertNotNull(configuration);
        Assertions.assertEquals("http://localhost:8123/api", configuration.getBaseUrl().toString());
        Assertions.assertEquals("test_token_123", configuration.getLongLivedAccessToken());

        StatusService statusService = homeAssistantConfiguration.statusService(configuration);
        Assertions.assertNotNull(statusService);

        StatesService statesService = homeAssistantConfiguration.statesService(configuration);
        Assertions.assertNotNull(statesService);

        EntityManagementService entityManagementService = homeAssistantConfiguration.entityManagementService(configuration, statesService);
        Assertions.assertNotNull(entityManagementService);

        LightService lightService = homeAssistantConfiguration.lightService(configuration);
        Assertions.assertNotNull(lightService);

        SwitchService switchService = homeAssistantConfiguration.switchService(configuration);
        Assertions.assertNotNull(switchService);

        MediaService mediaService = homeAssistantConfiguration.mediaService(configuration);
        Assertions.assertNotNull(mediaService);

        ServiceService serviceService = homeAssistantConfiguration.serviceService(configuration);
        Assertions.assertNotNull(serviceService);

        ConfigService configService = homeAssistantConfiguration.configService(configuration);
        Assertions.assertNotNull(configService);

        LogbookService logbookService = homeAssistantConfiguration.logbookService(configuration);
        Assertions.assertNotNull(logbookService);

        HistoryService historyService = homeAssistantConfiguration.historyService(configuration);
        Assertions.assertNotNull(historyService);

        TemplateService templateService = homeAssistantConfiguration.templateService(configuration);
        Assertions.assertNotNull(templateService);

        ErrorLogService errorLogService = homeAssistantConfiguration.errorLogService(configuration);
        Assertions.assertNotNull(errorLogService);

        EventService eventService = homeAssistantConfiguration.eventService(configuration);
        Assertions.assertNotNull(eventService);

        SunService sunService = homeAssistantConfiguration.sunService(configuration);
        Assertions.assertNotNull(sunService);

        UpdateService updateService = homeAssistantConfiguration.updateService(configuration);
        Assertions.assertNotNull(updateService);

        CalendarService calendarService = homeAssistantConfiguration.calendarService(configuration);
        Assertions.assertNotNull(calendarService);

        HomeAssistantTools homeAssistantTools = Mockito.mock(HomeAssistantTools.class);
        ToolCallbackProvider toolCallbackProvider = homeAssistantConfiguration.homeAssistantToolCallbackProvider(homeAssistantTools);
        Assertions.assertNotNull(toolCallbackProvider);
        Assertions.assertNotNull(toolCallbackProvider.getToolCallbacks());
        Assertions.assertTrue(toolCallbackProvider.getToolCallbacks().length > 0);
    }

    @Test
    void shouldFallbackToDefaultTokenWhenTokenIsEmpty() {
        HomeAssistantConfiguration homeAssistantConfiguration = new HomeAssistantConfiguration();
        HomeAssistantProperties homeAssistantProperties = new HomeAssistantProperties();
        homeAssistantProperties.setToken(null);

        Configuration configuration = homeAssistantConfiguration.jhaapiConfiguration(homeAssistantProperties);
        Assertions.assertNotNull(configuration);
        Assertions.assertEquals("placeholder_access_token", configuration.getLongLivedAccessToken());
    }
}
