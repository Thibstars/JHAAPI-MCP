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
import com.github.thibstars.jhaapi.client.history.response.Attributes;
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
import com.github.thibstars.jhaapi.client.sun.response.SunAttributes;
import com.github.thibstars.jhaapi.client.templates.TemplateService;
import com.github.thibstars.jhaapi.client.updates.UpdateService;
import com.github.thibstars.jhaapi.client.updates.response.Update;
import com.github.thibstars.jhaapi.client.updates.response.UpdateAttributes;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Unit tests for HomeAssistantTools.
 *
 * @author Thibstars
 */
class HomeAssistantToolsTest {

    private StatusService mockStatusService;
    private StatesService mockStatesService;
    private LightService mockLightService;
    private SwitchService mockSwitchService;
    private ServiceService mockServiceService;
    private ConfigService mockConfigService;
    private TemplateService mockTemplateService;
    private ErrorLogService mockErrorLogService;
    private EventService mockEventService;
    private SunService mockSunService;
    private UpdateService mockUpdateService;
    private CalendarService mockCalendarService;
    private LogbookService mockLogbookService;

    private HomeAssistantTools homeAssistantTools;

    @BeforeEach
    void setUp() {
        this.mockStatusService = Mockito.mock(StatusService.class);
        this.mockStatesService = Mockito.mock(StatesService.class);
        this.mockLightService = Mockito.mock(LightService.class);
        this.mockSwitchService = Mockito.mock(SwitchService.class);
        this.mockServiceService = Mockito.mock(ServiceService.class);
        this.mockConfigService = Mockito.mock(ConfigService.class);
        this.mockTemplateService = Mockito.mock(TemplateService.class);
        this.mockErrorLogService = Mockito.mock(ErrorLogService.class);
        this.mockEventService = Mockito.mock(EventService.class);
        this.mockSunService = Mockito.mock(SunService.class);
        this.mockUpdateService = Mockito.mock(UpdateService.class);
        this.mockCalendarService = Mockito.mock(CalendarService.class);
        this.mockLogbookService = Mockito.mock(LogbookService.class);

        this.homeAssistantTools = new HomeAssistantTools(
                this.mockStatusService,
                this.mockStatesService,
                this.mockLightService,
                this.mockSwitchService,
                this.mockServiceService,
                this.mockConfigService,
                this.mockTemplateService,
                this.mockErrorLogService,
                this.mockEventService,
                this.mockSunService,
                this.mockUpdateService,
                this.mockCalendarService,
                this.mockLogbookService
        );
    }

    @Test
    void shouldReturnHomeAssistantStatusWhenPresent() {
        Status sampleStatus = new Status("API is running.");
        Mockito.when(this.mockStatusService.getStatus()).thenReturn(Optional.of(sampleStatus));

        String result = this.homeAssistantTools.getHomeAssistantStatus();
        Assertions.assertEquals("API is running.", result);
    }

    @Test
    void shouldReturnFallbackStatusWhenEmpty() {
        Mockito.when(this.mockStatusService.getStatus()).thenReturn(Optional.empty());

        String result = this.homeAssistantTools.getHomeAssistantStatus();
        Assertions.assertEquals("Home Assistant instance status could not be retrieved.", result);
    }

    @Test
    void shouldReturnAllStatesUnfiltered() {
        State lightState = new State(new Attributes("Living Room Light", null), "light.living_room", OffsetDateTime.now(), "on");
        State switchState = new State(new Attributes("Heater Switch", null), "switch.heater", OffsetDateTime.now(), "off");
        Mockito.when(this.mockStatesService.getStates()).thenReturn(List.of(lightState, switchState));

        List<State> result = this.homeAssistantTools.getAllStates(null);
        Assertions.assertEquals(2, result.size());
    }

    @Test
    void shouldReturnFilteredStatesByDomain() {
        State lightState = new State(new Attributes("Living Room Light", null), "light.living_room", OffsetDateTime.now(), "on");
        State switchState = new State(new Attributes("Heater Switch", null), "switch.heater", OffsetDateTime.now(), "off");
        Mockito.when(this.mockStatesService.getStates()).thenReturn(List.of(lightState, switchState));

        List<State> lightResults = this.homeAssistantTools.getAllStates("light");
        Assertions.assertEquals(1, lightResults.size());
        Assertions.assertEquals("light.living_room", lightResults.getFirst().entityId());
    }

    @Test
    void shouldReturnEntityStateWhenFound() {
        State lightState = new State(new Attributes("Living Room Light", null), "light.living_room", OffsetDateTime.now(), "on");
        Mockito.when(this.mockStatesService.getState("light.living_room")).thenReturn(Optional.of(lightState));

        State result = this.homeAssistantTools.getEntityState("light.living_room");
        Assertions.assertNotNull(result);
        Assertions.assertEquals("light.living_room", result.entityId());
    }

    @Test
    void shouldReturnNullWhenEntityStateNotFound() {
        Mockito.when(this.mockStatesService.getState("nonexistent.entity")).thenReturn(Optional.empty());

        State result = this.homeAssistantTools.getEntityState("nonexistent.entity");
        Assertions.assertNull(result);
    }

    @Test
    void shouldUpdateEntityState() {
        State updatedState = new State(new Attributes("Room Temperature", "°C"), "sensor.temperature", OffsetDateTime.now(), "21.5");
        Mockito.when(this.mockStatesService.updateState("sensor.temperature", "21.5", "Room Temperature", true))
                .thenReturn(Optional.of(updatedState));

        State result = this.homeAssistantTools.updateEntityState("sensor.temperature", "21.5", "Room Temperature", true);
        Assertions.assertNotNull(result);
        Assertions.assertEquals("21.5", result.state());
    }

    @Test
    void shouldToggleLight() {
        String result = this.homeAssistantTools.toggleLight("light.bedroom");
        Mockito.verify(this.mockLightService).toggle("light.bedroom");
        Assertions.assertEquals("Toggled light: light.bedroom", result);
    }

    @Test
    void shouldToggleSwitch() {
        String result = this.homeAssistantTools.toggleSwitch("switch.fan");
        Mockito.verify(this.mockSwitchService).toggle("switch.fan");
        Assertions.assertEquals("Toggled switch: switch.fan", result);
    }

    @Test
    void shouldCallServiceWithData() {
        String result = this.homeAssistantTools.callService("light", "turn_on", "{\"brightness\": 255}");
        Mockito.verify(this.mockServiceService).callService("light", "turn_on", "{\"brightness\": 255}");
        Assertions.assertEquals("Executed service: light.turn_on", result);
    }

    @Test
    void shouldCallServiceWithoutData() {
        String result = this.homeAssistantTools.callService("switch", "turn_off", null);
        Mockito.verify(this.mockServiceService).callService("switch", "turn_off");
        Assertions.assertEquals("Executed service: switch.turn_off", result);
    }

    @Test
    void shouldGetHomeAssistantConfiguration() {
        Config sampleConfig = new Config(
                List.of("frontend"),
                null,
                100,
                50.85,
                "Home",
                4.35,
                "Europe/Brussels",
                null,
                "2026.1.0",
                null,
                null,
                null,
                null,
                false,
                null,
                null,
                null,
                "EUR",
                "BE",
                "en",
                false,
                false,
                null
        );
        Mockito.when(this.mockConfigService.getConfig()).thenReturn(Optional.of(sampleConfig));

        Config result = this.homeAssistantTools.getHomeAssistantConfiguration();
        Assertions.assertNotNull(result);
        Assertions.assertEquals("Home", result.locationName());
    }

    @Test
    void shouldCheckHomeAssistantConfiguration() {
        CheckConfigResponse sampleResponse = new CheckConfigResponse("valid", null);
        Mockito.when(this.mockConfigService.checkConfig()).thenReturn(Optional.of(sampleResponse));

        CheckConfigResponse result = this.homeAssistantTools.checkHomeAssistantConfiguration();
        Assertions.assertNotNull(result);
        Assertions.assertEquals("valid", result.result());
    }

    @Test
    void shouldRenderTemplate() {
        Mockito.when(this.mockTemplateService.renderTemplate("{{ 1 + 1 }}")).thenReturn("2");

        String result = this.homeAssistantTools.renderTemplate("{{ 1 + 1 }}");
        Assertions.assertEquals("2", result);
    }

    @Test
    void shouldGetErrorLogWhenPresent() {
        ErrorLog sampleErrorLog = new ErrorLog("2026-10-10 12:00:00 ERROR Integration error");
        Mockito.when(this.mockErrorLogService.getErrorLog()).thenReturn(Optional.of(sampleErrorLog));

        String result = this.homeAssistantTools.getErrorLog();
        Assertions.assertEquals("2026-10-10 12:00:00 ERROR Integration error", result);
    }

    @Test
    void shouldReturnFallbackWhenErrorLogEmpty() {
        Mockito.when(this.mockErrorLogService.getErrorLog()).thenReturn(Optional.empty());

        String result = this.homeAssistantTools.getErrorLog();
        Assertions.assertEquals("No error logs available.", result);
    }

    @Test
    void shouldGetEvents() {
        Event sampleEvent = new Event("state_changed", 10);
        Mockito.when(this.mockEventService.getEvents()).thenReturn(List.of(sampleEvent));

        List<Event> result = this.homeAssistantTools.getEvents();
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("state_changed", result.getFirst().event());
    }

    @Test
    void shouldGetSunInformation() {
        SunAttributes sampleSunAttributes = new SunAttributes(null, null, null, null, null, null, 45.0, 180.0, true, "Sun");
        Sun sampleSun = new Sun(sampleSunAttributes, "sun.sun", OffsetDateTime.now(), "above_horizon");
        Mockito.when(this.mockSunService.getSunInfo()).thenReturn(Optional.of(sampleSun));

        Sun result = this.homeAssistantTools.getSunInformation();
        Assertions.assertNotNull(result);
        Assertions.assertEquals("sun.sun", result.entityId());
    }

    @Test
    void shouldGetAvailableUpdates() {
        UpdateAttributes sampleUpdateAttributes = new UpdateAttributes(false, "2026.1.0", "2026.1.1", null, null, null, "Core", null, "Home Assistant Core", null);
        Update sampleUpdate = new Update(sampleUpdateAttributes, "update.home_assistant_core", OffsetDateTime.now(), OffsetDateTime.now(), "on");
        Mockito.when(this.mockUpdateService.getAvailableUpdates()).thenReturn(List.of(sampleUpdate));

        List<Update> result = this.homeAssistantTools.getAvailableUpdates();
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("update.home_assistant_core", result.getFirst().entityId());
    }

    @Test
    void shouldGetCalendars() {
        Calendar sampleCalendar = new Calendar("calendar.work", "Work Calendar");
        Mockito.when(this.mockCalendarService.getCalendars()).thenReturn(List.of(sampleCalendar));

        List<Calendar> result = this.homeAssistantTools.getCalendars();
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("calendar.work", result.getFirst().entityId());
    }

    @Test
    void shouldGetCalendarEventsWithTimeRange() {
        CalendarEvent sampleCalendarEvent = new CalendarEvent(OffsetDateTime.parse("2026-10-10T10:00:00Z"), OffsetDateTime.parse("2026-10-10T11:00:00Z"), "Meeting", "Discuss MCP", null, null, null, null);
        Mockito.when(this.mockCalendarService.getCalendarEvents(
                Mockito.eq("calendar.work"),
                Mockito.any(OffsetDateTime.class),
                Mockito.any(OffsetDateTime.class)
        )).thenReturn(List.of(sampleCalendarEvent));

        List<CalendarEvent> result = this.homeAssistantTools.getCalendarEvents("calendar.work", "2026-10-10T00:00:00Z", "2026-10-10T23:59:59Z");
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("Meeting", result.getFirst().summary());
    }

    @Test
    void shouldGetLogbookEntriesUnfiltered() {
        Log sampleLog = new Log(null, "light", "light.living_room", "Living Room Light turned on", "Living Room", OffsetDateTime.now());
        Mockito.when(this.mockLogbookService.getLogs()).thenReturn(List.of(sampleLog));

        List<Log> result = this.homeAssistantTools.getLogbookEntries(null);
        Assertions.assertEquals(1, result.size());
    }

    @Test
    void shouldGetLogbookEntriesFilteredByEntity() {
        Log sampleLog = new Log(null, "light", "light.living_room", "Living Room Light turned on", "Living Room", OffsetDateTime.now());
        Mockito.when(this.mockLogbookService.getLogs("light.living_room")).thenReturn(List.of(sampleLog));

        List<Log> result = this.homeAssistantTools.getLogbookEntries("light.living_room");
        Assertions.assertEquals(1, result.size());
    }
}
