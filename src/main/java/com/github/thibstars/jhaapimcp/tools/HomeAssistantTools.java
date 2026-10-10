package com.github.thibstars.jhaapimcp.tools;

import com.github.thibstars.jhaapi.client.calendars.CalendarService;
import com.github.thibstars.jhaapi.client.calendars.response.Calendar;
import com.github.thibstars.jhaapi.client.calendars.response.CalendarEvent;
import com.github.thibstars.jhaapi.client.config.ConfigService;
import com.github.thibstars.jhaapi.client.config.response.CheckConfigResponse;
import com.github.thibstars.jhaapi.client.config.response.Config;
import com.github.thibstars.jhaapi.client.errors.ErrorLogService;
import com.github.thibstars.jhaapi.client.errors.response.ErrorLog;
import com.github.thibstars.jhaapi.client.events.EventService;
import com.github.thibstars.jhaapi.client.events.response.Event;
import com.github.thibstars.jhaapi.client.logbook.LogbookService;
import com.github.thibstars.jhaapi.client.logbook.response.Log;
import com.github.thibstars.jhaapi.client.services.LightService;
import com.github.thibstars.jhaapi.client.services.ServiceService;
import com.github.thibstars.jhaapi.client.services.SwitchService;
import com.github.thibstars.jhaapi.client.states.StatesService;
import com.github.thibstars.jhaapi.client.states.response.State;
import com.github.thibstars.jhaapi.client.status.StatusService;
import com.github.thibstars.jhaapi.client.status.response.Status;
import com.github.thibstars.jhaapi.client.sun.SunService;
import com.github.thibstars.jhaapi.client.sun.response.Sun;
import com.github.thibstars.jhaapi.client.templates.TemplateService;
import com.github.thibstars.jhaapi.client.updates.UpdateService;
import com.github.thibstars.jhaapi.client.updates.response.Update;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

/**
 * Model Context Protocol (MCP) tools for interacting with Home Assistant via JHAAPI.
 *
 * @author Thibstars
 */
@Service
public class HomeAssistantTools {

    private final StatusService statusService;
    private final StatesService statesService;
    private final LightService lightService;
    private final SwitchService switchService;
    private final ServiceService serviceService;
    private final ConfigService configService;
    private final TemplateService templateService;
    private final ErrorLogService errorLogService;
    private final EventService eventService;
    private final SunService sunService;
    private final UpdateService updateService;
    private final CalendarService calendarService;
    private final LogbookService logbookService;

    public HomeAssistantTools(
            StatusService statusService,
            StatesService statesService,
            LightService lightService,
            SwitchService switchService,
            ServiceService serviceService,
            ConfigService configService,
            TemplateService templateService,
            ErrorLogService errorLogService,
            EventService eventService,
            SunService sunService,
            UpdateService updateService,
            CalendarService calendarService,
            LogbookService logbookService) {
        this.statusService = statusService;
        this.statesService = statesService;
        this.lightService = lightService;
        this.switchService = switchService;
        this.serviceService = serviceService;
        this.configService = configService;
        this.templateService = templateService;
        this.errorLogService = errorLogService;
        this.eventService = eventService;
        this.sunService = sunService;
        this.updateService = updateService;
        this.calendarService = calendarService;
        this.logbookService = logbookService;
    }

    @Tool(description = "Retrieves the status and health check message of the connected Home Assistant instance.")
    public String getHomeAssistantStatus() {
        Optional<Status> statusOptional = this.statusService.getStatus();
        if (statusOptional.isPresent()) {
            return statusOptional.get().message();
        }
        return "Home Assistant instance status could not be retrieved.";
    }

    @Tool(description = "Retrieves current states and attributes of all entities in Home Assistant, optionally filtered by domain (e.g. 'light', 'sensor', 'switch').")
    public List<State> getAllStates(
            @ToolParam(description = "Optional domain filter (e.g. 'light', 'sensor', 'switch') to restrict returned entities", required = false) String domainFilter) {
        List<State> allStates = this.statesService.getStates();
        if (domainFilter != null && !domainFilter.isBlank()) {
            String domainPrefix = domainFilter.trim().toLowerCase() + ".";
            return allStates.stream()
                    .filter(state -> state.entityId() != null && state.entityId().toLowerCase().startsWith(domainPrefix))
                    .toList();
        }
        return allStates;
    }

    @Tool(description = "Retrieves the detailed state, attributes, and metadata for a specific entity identifier.")
    public State getEntityState(
            @ToolParam(description = "The unique entity identifier in Home Assistant, such as 'light.living_room' or 'sensor.temperature'", required = true) String entityIdentifier) {
        Optional<State> stateOptional = this.statesService.getState(entityIdentifier);
        return stateOptional.orElse(null);
    }

    @Tool(description = "Updates the state, friendly name, and read-only status of an entity in Home Assistant.")
    public State updateEntityState(
            @ToolParam(description = "The unique entity identifier to update", required = true) String entityIdentifier,
            @ToolParam(description = "The new state value to set", required = true) String stateValue,
            @ToolParam(description = "The friendly display name for the entity", required = false) String friendlyName,
            @ToolParam(description = "Whether the entity should be read-only in Home Assistant", required = false) Boolean readOnly) {
        boolean effectiveReadOnly = readOnly != null && readOnly;
        Optional<State> updatedStateOptional = this.statesService.updateState(
                entityIdentifier,
                stateValue,
                friendlyName != null ? friendlyName : entityIdentifier,
                effectiveReadOnly
        );
        return updatedStateOptional.orElse(null);
    }

    @Tool(description = "Toggles a light entity on or off by its name or entity identifier.")
    public String toggleLight(
            @ToolParam(description = "The name or entity identifier of the light (e.g. 'light.kitchen_ceiling')", required = true) String lightName) {
        this.lightService.toggle(lightName);
        return "Toggled light: " + lightName;
    }

    @Tool(description = "Toggles a switch entity on or off by its name or entity identifier.")
    public String toggleSwitch(
            @ToolParam(description = "The name or entity identifier of the switch (e.g. 'switch.coffee_maker')", required = true) String switchName) {
        this.switchService.toggle(switchName);
        return "Toggled switch: " + switchName;
    }

    @Tool(description = "Calls any Home Assistant service within a domain with an action name and optional service data.")
    public String callService(
            @ToolParam(description = "The domain name of the service, such as 'light', 'switch', 'climate', 'media_player'", required = true) String domainName,
            @ToolParam(description = "The action or service name to execute, such as 'turn_on', 'turn_off', 'toggle'", required = true) String serviceName,
            @ToolParam(description = "Optional JSON payload string or parameters to pass to the service", required = false) String serviceData) {
        if (serviceData != null && !serviceData.isBlank()) {
            this.serviceService.callService(domainName, serviceName, serviceData);
        } else {
            this.serviceService.callService(domainName, serviceName);
        }
        return "Executed service: " + domainName + "." + serviceName;
    }

    @Tool(description = "Retrieves the system configuration of Home Assistant, including version, location, time zone, components, and unit system.")
    public Config getHomeAssistantConfiguration() {
        Optional<Config> configurationOptional = this.configService.getConfig();
        return configurationOptional.orElse(null);
    }

    @Tool(description = "Validates the Home Assistant configuration files and returns the validation result.")
    public CheckConfigResponse checkHomeAssistantConfiguration() {
        Optional<CheckConfigResponse> checkConfigResponseOptional = this.configService.checkConfig();
        return checkConfigResponseOptional.orElse(null);
    }

    @Tool(description = "Renders a Jinja2 template string in Home Assistant and returns the evaluated result.")
    public String renderTemplate(
            @ToolParam(description = "The Jinja2 template string to render (e.g. '{{ states(\\'sensor.temperature\\') }}')", required = true) String templateString) {
        return this.templateService.renderTemplate(templateString);
    }

    @Tool(description = "Retrieves the recent error log from the Home Assistant server for troubleshooting.")
    public String getErrorLog() {
        Optional<ErrorLog> errorLogOptional = this.errorLogService.getErrorLog();
        if (errorLogOptional.isPresent()) {
            return errorLogOptional.get().content();
        }
        return "No error logs available.";
    }

    @Tool(description = "Retrieves the list of active event listeners and registered event types in Home Assistant.")
    public List<Event> getEvents() {
        return this.eventService.getEvents();
    }

    @Tool(description = "Retrieves astronomical sun position and daylight data including elevation, rising, setting, and daylight state.")
    public Sun getSunInformation() {
        Optional<Sun> sunOptional = this.sunService.getSunInfo();
        return sunOptional.orElse(null);
    }

    @Tool(description = "Retrieves the list of available software and integration updates for Home Assistant components.")
    public List<Update> getAvailableUpdates() {
        return this.updateService.getAvailableUpdates();
    }

    @Tool(description = "Retrieves all configured calendar entities in Home Assistant.")
    public List<Calendar> getCalendars() {
        return this.calendarService.getCalendars();
    }

    @Tool(description = "Retrieves calendar events for a specific calendar entity within an optional start and end date-time range.")
    public List<CalendarEvent> getCalendarEvents(
            @ToolParam(description = "The calendar entity identifier (e.g. 'calendar.personal')", required = true) String calendarIdentifier,
            @ToolParam(description = "Start timestamp in ISO-8601 format (e.g. '2026-10-10T00:00:00Z')", required = false) String startTimestamp,
            @ToolParam(description = "End timestamp in ISO-8601 format (e.g. '2026-10-11T00:00:00Z')", required = false) String endTimestamp) {
        OffsetDateTime startDateTime = null;
        OffsetDateTime endDateTime = null;
        if (startTimestamp != null && !startTimestamp.isBlank()) {
            startDateTime = OffsetDateTime.parse(startTimestamp);
        }
        if (endTimestamp != null && !endTimestamp.isBlank()) {
            endDateTime = OffsetDateTime.parse(endTimestamp);
        }
        return this.calendarService.getCalendarEvents(calendarIdentifier, startDateTime, endDateTime);
    }

    @Tool(description = "Retrieves logbook entries recording past events and actions in Home Assistant, optionally filtered by entity identifier.")
    public List<Log> getLogbookEntries(
            @ToolParam(description = "Optional entity identifier to filter logbook entries", required = false) String entityIdentifier) {
        if (entityIdentifier != null && !entityIdentifier.isBlank()) {
            return this.logbookService.getLogs(entityIdentifier);
        }
        return this.logbookService.getLogs();
    }
}
