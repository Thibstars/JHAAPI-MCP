package com.github.thibstars.jhaapimcp.configuration;

import com.github.thibstars.jhaapi.Configuration;
import com.github.thibstars.jhaapi.client.calendars.CalendarService;
import com.github.thibstars.jhaapi.client.calendars.CalendarServiceImpl;
import com.github.thibstars.jhaapi.client.config.ConfigService;
import com.github.thibstars.jhaapi.client.config.ConfigServiceImpl;
import com.github.thibstars.jhaapi.client.errors.ErrorLogService;
import com.github.thibstars.jhaapi.client.errors.ErrorLogServiceImpl;
import com.github.thibstars.jhaapi.client.events.EventService;
import com.github.thibstars.jhaapi.client.events.EventServiceImpl;
import com.github.thibstars.jhaapi.client.history.HistoryService;
import com.github.thibstars.jhaapi.client.history.HistoryServiceImpl;
import com.github.thibstars.jhaapi.client.logbook.LogbookService;
import com.github.thibstars.jhaapi.client.logbook.LogbookServiceImpl;
import com.github.thibstars.jhaapi.client.services.LightService;
import com.github.thibstars.jhaapi.client.services.LightServiceImpl;
import com.github.thibstars.jhaapi.client.services.MediaService;
import com.github.thibstars.jhaapi.client.services.MediaServiceImpl;
import com.github.thibstars.jhaapi.client.services.ServiceService;
import com.github.thibstars.jhaapi.client.services.ServiceServiceImpl;
import com.github.thibstars.jhaapi.client.services.SwitchService;
import com.github.thibstars.jhaapi.client.services.SwitchServiceImpl;
import com.github.thibstars.jhaapi.client.states.EntityManagementService;
import com.github.thibstars.jhaapi.client.states.EntityManagementServiceImpl;
import com.github.thibstars.jhaapi.client.states.StatesService;
import com.github.thibstars.jhaapi.client.states.StatesServiceImpl;
import com.github.thibstars.jhaapi.client.status.StatusService;
import com.github.thibstars.jhaapi.client.status.StatusServiceImpl;
import com.github.thibstars.jhaapi.client.sun.SunService;
import com.github.thibstars.jhaapi.client.sun.SunServiceImpl;
import com.github.thibstars.jhaapi.client.templates.TemplateService;
import com.github.thibstars.jhaapi.client.templates.TemplateServiceImpl;
import com.github.thibstars.jhaapi.client.updates.UpdateService;
import com.github.thibstars.jhaapi.client.updates.UpdateServiceImpl;
import com.github.thibstars.jhaapimcp.tools.HomeAssistantTools;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * Spring configuration providing JHAAPI client services.
 *
 * @author Thibstars
 */
@org.springframework.context.annotation.Configuration
public class HomeAssistantConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public Configuration jhaapiConfiguration(HomeAssistantProperties homeAssistantProperties) {
        String token = homeAssistantProperties.getToken();
        if (token == null || token.isBlank()) {
            token = "placeholder_access_token";
        }

        return Configuration.builder()
                .baseUrl(homeAssistantProperties.getBaseUrl())
                .token(token)
                .entityEnabled(homeAssistantProperties.isEntityEnabled())
                .entityId(homeAssistantProperties.getEntityIdentifier())
                .entityFriendlyName(homeAssistantProperties.getEntityFriendlyName())
                .entityReadOnly(homeAssistantProperties.isEntityReadOnly())
                .entityShutdownEnabled(homeAssistantProperties.isEntityShutdownEnabled())
                .build();
    }

    @Bean
    @ConditionalOnMissingBean
    public StatusService statusService(Configuration configuration) {
        return new StatusServiceImpl(configuration);
    }

    @Bean
    @ConditionalOnMissingBean
    public StatesService statesService(Configuration configuration) {
        return new StatesServiceImpl(configuration);
    }

    @Bean
    @ConditionalOnMissingBean
    public EntityManagementService entityManagementService(Configuration configuration, StatesService statesService) {
        return new EntityManagementServiceImpl(configuration, statesService);
    }

    @Bean
    @ConditionalOnMissingBean
    public LightService lightService(Configuration configuration) {
        return new LightServiceImpl(configuration);
    }

    @Bean
    @ConditionalOnMissingBean
    public SwitchService switchService(Configuration configuration) {
        return new SwitchServiceImpl(configuration);
    }

    @Bean
    @ConditionalOnMissingBean
    public MediaService mediaService(Configuration configuration) {
        return new MediaServiceImpl(configuration);
    }

    @Bean
    @ConditionalOnMissingBean
    public ServiceService serviceService(Configuration configuration) {
        return new ServiceServiceImpl(configuration);
    }

    @Bean
    @ConditionalOnMissingBean
    public ConfigService configService(Configuration configuration) {
        return new ConfigServiceImpl(configuration);
    }

    @Bean
    @ConditionalOnMissingBean
    public LogbookService logbookService(Configuration configuration) {
        return new LogbookServiceImpl(configuration);
    }

    @Bean
    @ConditionalOnMissingBean
    public HistoryService historyService(Configuration configuration) {
        return new HistoryServiceImpl(configuration);
    }

    @Bean
    @ConditionalOnMissingBean
    public TemplateService templateService(Configuration configuration) {
        return new TemplateServiceImpl(configuration);
    }

    @Bean
    @ConditionalOnMissingBean
    public ErrorLogService errorLogService(Configuration configuration) {
        return new ErrorLogServiceImpl(configuration);
    }

    @Bean
    @ConditionalOnMissingBean
    public EventService eventService(Configuration configuration) {
        return new EventServiceImpl(configuration);
    }

    @Bean
    @ConditionalOnMissingBean
    public SunService sunService(Configuration configuration) {
        return new SunServiceImpl(configuration);
    }

    @Bean
    @ConditionalOnMissingBean
    public UpdateService updateService(Configuration configuration) {
        return new UpdateServiceImpl(configuration);
    }

    @Bean
    @ConditionalOnMissingBean
    public CalendarService calendarService(Configuration configuration) {
        return new CalendarServiceImpl(configuration);
    }

    @Bean
    @ConditionalOnMissingBean
    public ToolCallbackProvider homeAssistantToolCallbackProvider(HomeAssistantTools homeAssistantTools) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(homeAssistantTools)
                .build();
    }
}
