# JHAAPI-MCP

[![Java](https://img.shields.io/badge/Java-25%20LTS-orange.svg)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.x-green.svg)](https://spring.io/projects/spring-boot)
[![Spring AI](https://img.shields.io/badge/Spring%20AI-1.0.0-blue.svg)](https://spring.io/projects/spring-ai)
[![Protocol](https://img.shields.io/badge/MCP-Model%20Context%20Protocol-purple.svg)](https://modelcontextprotocol.io/)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE)

**JHAAPI-MCP** is a Model Context Protocol (MCP) server engineered to maximize the operational utility of AI agents and automated assistants by providing structured, type-safe, and real-time integration with [Home Assistant](https://www.home-assistant.io/) smart home ecosystems via the [JHAAPI](https://github.com/Thibstars/JHAAPI) Java client library.

By standardizing access to entities, services, templates, updates, and event logs through MCP tool abstractions, JHAAPI-MCP empowers intelligent systems to optimize physical environments, conserve energy, monitor critical infrastructure, and execute home automation tasks with high reliability and zero unnecessary overhead.

---

## Table of Contents
1. [Core Features & Utilities](#core-features--utilities)
2. [Architecture & Technology Stack](#architecture--technology-stack)
3. [Prerequisites](#prerequisites)
4. [Setup Guide](#setup-guide)
   - [1. Obtain Home Assistant Access Token](#1-obtain-home-assistant-access-token)
   - [2. Configure Application Properties](#2-configure-application-properties)
   - [3. Build the Application](#3-build-the-application)
   - [4. Run the MCP Server](#4-run-the-mcp-server)
5. [Connecting to MCP Clients](#connecting-to-mcp-clients)
   - [Claude Desktop Configuration](#claude-desktop-configuration)
   - [Gemini / Generic MCP Client Configuration](#gemini--generic-mcp-client-configuration)
6. [Available MCP Tools](#available-mcp-tools)
7. [Operating Invariants & Contributing](#operating-invariants--contributing)
8. [License](#license)

---

## Core Features & Utilities

- **Comprehensive Entity State Management**: Retrieve all entity states or inspect specific devices with domain filtering (`light`, `switch`, `sensor`, `climate`, etc.) to produce accurate situational awareness.
- **Direct Device & Service Invocation**: Seamlessly toggle lights and switches, or call arbitrary Home Assistant domain services with optional structured JSON payloads.
- **Dynamic Jinja2 Template Evaluation**: Render complex Home Assistant templates server-side for calculated states, aggregated metrics, and dynamic conditions.
- **System Health & Validation**: Check core system status, validate YAML/system configurations, and diagnose errors via real-time error log inspection.
- **Astronomical & Environmental Context**: Access solar elevation, azimuth, sunrise, and sunset timings to optimize lighting and climate schedules.
- **Maintenance & Schedule Awareness**: Inspect available component updates and query calendar schedules to coordinate automated actions around human routines.
- **History & Audit Trails**: Query historical logbook entries to track entity state transitions and verify action outcomes.

---

## Architecture & Technology Stack

- **Runtime**: Java 25 (LTS)
- **Framework**: Spring Boot 3.5.x with Spring AI MCP Server starter
- **Integration Client**: JHAAPI 1.0.15-SNAPSHOT (Java Home Assistant API)
- **Transport Layer**: Standard Input/Output (STDIO) synchronous MCP transport
- **Build System**: Apache Maven Wrapper (`mvnw` / `mvnw.cmd`)

```
+---------------------------+        +---------------------------+        +---------------------------+
|    AI Assistant / Client  | <====> |        JHAAPI-MCP         | <====> |      Home Assistant       |
| (Claude, Gemini, Agents)  |  STDIO |  (Spring AI + JHAAPI)     |  REST  | (Core / OS / Supervised)  |
+---------------------------+        +---------------------------+        +---------------------------+
```

---

## Prerequisites

Before setting up JHAAPI-MCP, ensure your environment provides:

1. **Java Development Kit (JDK)**: OpenJDK 25 or higher installed and configured with `JAVA_HOME`.
2. **Home Assistant Instance**: An active Home Assistant deployment (accessible over HTTP or HTTPS).
3. **Long-Lived Access Token**: A valid bearer token generated from your Home Assistant user profile.
4. **Git**: Installed for repository management and version tracking.

---

## Setup Guide

### 1. Obtain Home Assistant Access Token

To enable authenticated communication with maximum security:
1. Open your Home Assistant web frontend.
2. Click on your user profile icon (bottom-left corner).
3. Scroll down to the **Long-Lived Access Tokens** section.
4. Click **Create Token**, provide a descriptive name (e.g. `JHAAPI-MCP-Server`), and copy the generated token string.

### 2. Configure Application Properties

Configuration parameters can be supplied via `src/main/resources/application.properties` or overridden via environment variables.

#### Example `application.properties`:
```properties
spring.application.name=jhaapi-mcp
spring.main.banner-mode=off

# Model Context Protocol (MCP) Server Configuration
spring.ai.mcp.server.enabled=true
spring.ai.mcp.server.name=jhaapi-mcp-server
spring.ai.mcp.server.version=1.0.0
spring.ai.mcp.server.type=SYNC
spring.ai.mcp.server.stdio=true

# Home Assistant Connection Settings
homeassistant.base-url=http://homeassistant.local:8123/api
homeassistant.token=${HOMEASSISTANT_TOKEN:your_long_lived_access_token_here}
homeassistant.entity-enabled=false
homeassistant.entity-identifier=switch.jhaapi_application
homeassistant.entity-friendly-name=JHAAPI Application
homeassistant.entity-read-only=true
homeassistant.entity-shutdown-enabled=false
```

#### Environment Variables Reference:
| Environment Variable | Description | Default Value |
|----------------------|-------------|---------------|
| `HOMEASSISTANT_TOKEN` | Home Assistant Long-Lived Access Token | _None (Required)_ |
| `HOMEASSISTANT_BASE_URL` | Base URL of the Home Assistant REST API | `http://homeassistant.local:8123/api` |

### 3. Build the Application

Compile and package the standalone executable JAR using the Maven wrapper:

**Unix / macOS:**
```bash
./mvnw clean package
```

**Windows (PowerShell / Command Prompt):**
```powershell
.\mvnw.cmd clean package
```

The repackaged JAR artifact will be located at `target/JHAAPI-MCP-1.0-SNAPSHOT.jar`.

### 4. Run the MCP Server

You can execute the server directly in standalone mode:

```bash
java -jar target/JHAAPI-MCP-1.0-SNAPSHOT.jar
```

Or run via Maven Spring Boot plugin during local development:

```bash
./mvnw spring-boot:run
```

---

## Connecting to MCP Clients

### Claude Desktop Configuration

Add the JHAAPI-MCP server definition to your `claude_desktop_config.json` file:

- **macOS**: `~/Library/Application Support/Claude/claude_desktop_config.json`
- **Windows**: `%APPDATA%\Claude\claude_desktop_config.json`

```json
{
  "mcpServers": {
    "homeassistant": {
      "command": "java",
      "args": [
        "-jar",
        "/absolute/path/to/JHAAPI-MCP/target/JHAAPI-MCP-1.0-SNAPSHOT.jar"
      ],
      "env": {
        "HOMEASSISTANT_TOKEN": "YOUR_LONG_LIVED_ACCESS_TOKEN",
        "HOMEASSISTANT_BASE_URL": "http://homeassistant.local:8123/api"
      }
    }
  }
}
```

### Gemini / Generic MCP Client Configuration

For any MCP client supporting STDIO server execution, register the server command:

```json
{
  "mcpServers": {
    "jhaapi-homeassistant": {
      "command": "java",
      "args": [
        "-jar",
        "/absolute/path/to/JHAAPI-MCP/target/JHAAPI-MCP-1.0-SNAPSHOT.jar"
      ],
      "env": {
        "HOMEASSISTANT_TOKEN": "YOUR_LONG_LIVED_ACCESS_TOKEN",
        "HOMEASSISTANT_BASE_URL": "http://homeassistant.local:8123/api"
      }
    }
  }
}
```

---

## Available MCP Tools

JHAAPI-MCP exposes the following high-utility tool endpoints to connected language models:

| Tool Name | Parameters | Description |
|-----------|------------|-------------|
| `getHomeAssistantStatus` | _None_ | Retrieves the core health check status of the Home Assistant instance. |
| `getAllStates` | `domainFilter` (optional string) | Retrieves states and attributes for all entities, optionally filtered by domain prefix (e.g. `light`, `sensor`). |
| `getEntityState` | `entityIdentifier` (required string) | Retrieves full state, attributes, and last updated timestamps for a specific entity ID. |
| `updateEntityState` | `entityIdentifier`, `state`, `friendlyName`, `persist` | Manually sets or overrides the state of a virtual or managed entity. |
| `toggleLight` | `entityIdentifier` (required string) | Toggles the state of a light entity (`on` <-> `off`). |
| `toggleSwitch` | `entityIdentifier` (required string) | Toggles the state of a switch entity (`on` <-> `off`). |
| `callService` | `domain`, `service`, `serviceDataJson` | Executes a Home Assistant service within a given domain with optional JSON payload. |
| `getHomeAssistantConfiguration` | _None_ | Fetches core configuration metadata (location, timezone, unit system, version). |
| `checkHomeAssistantConfiguration` | _None_ | Validates Home Assistant configuration files for syntax and semantic errors. |
| `renderTemplate` | `template` (required string) | Evaluates and renders a Jinja2 template against current Home Assistant state context. |
| `getErrorLog` | _None_ | Retrieves logged integration and system error messages for diagnostic purposes. |
| `getEvents` | _None_ | Returns active event listeners and associated listener counts. |
| `getSunInformation` | _None_ | Provides astronomical solar data including elevation, azimuth, and horizon state. |
| `getAvailableUpdates` | _None_ | Lists pending firmware, addon, and Core software updates. |
| `getCalendars` | _None_ | Lists configured calendar entities in the system. |
| `getCalendarEvents` | `calendarEntityIdentifier`, `startDateTime`, `endDateTime` | Queries calendar events within a specified ISO-8601 time range. |
| `getLogbookEntries` | `entityIdentifier` (optional string) | Retrieves chronological logbook activity entries, optionally filtered by entity ID. |

---

## Operating Invariants & Contributing

To maintain peak code quality, maintainability, and systematic correctness, all modifications must strictly uphold these rules:

1. **No Single-Letter Variables**: Variable names must be fully articulated and meaningful (the only exception being exception handling blocks such as `catch (Exception exception)`).
2. **No Abbreviations**: Always use full, clear names for identifiers (e.g., `configuration`, `request`, `response`, `properties`, `context`, `identifier`).
3. **Dead Code Elimination**: Prune obsolete or unreferenced code paths immediately.
4. **Import Cleanliness**: Maintain minimal and organized imports without unused references.
5. **Git Tracking for New Files**: Always track newly created files immediately via `git add`.

### Running Verification Tests

Execute all unit and integration tests across configuration and tool components:

```bash
./mvnw clean verify
```

---

## License

This project is licensed under the Apache License 2.0. See the [LICENSE](LICENSE) file for full details.
