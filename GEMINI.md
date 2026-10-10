# JHAAPI-MCP Agent Harness (Gemini)

## Project Overview
JHAAPI-MCP is a Model Context Protocol (MCP) server designed to give Gemini and other language models structured, type-safe, and robust access to Home Assistant smart home environments via the JHAAPI client library.

### Technology Stack
- **Java Version**: Java 25 (LTS)
- **Framework**: Spring Boot (with Spring AI MCP Server)
- **Build System**: Apache Maven (via Maven Wrapper `mvnw` / `mvnw.cmd`)
- **Integration Library**: JHAAPI (`com.github.thibstars:JHAAPI`)
- **Protocol**: Model Context Protocol (MCP) supporting standard input/output (STDIO) transport

## Strict Operating Invariants
All agents, automated assistants, and human developers contributing to this codebase must strictly observe:

1. **No Single-Letter Variables**: Never use single-letter variable names anywhere in production code or tests (the only exception is standard exception catching such as `catch (Exception exception)` or `catch (Exception e)`).
2. **No Abbreviations**: Never use abbreviations in variable names, class names, method names, field names, parameter names, or any identifiers. Always prefer the full descriptive name (for instance, use `request`, `response`, `configuration`, `context`, `parameter`, `properties`, `implementation`, `message`, `identifier`, `entityIdentifier`).
3. **Dead Code Elimination**: Get rid of all dead, unreachable, commented-out, or obsolete code after every implementation step.
4. **Import Cleanliness**: Keep all imports clean and minimal. Get rid of all unused imports after implementation.
5. **Git Tracking for New Files**: All newly added files must be immediately staged in git (`git add`) so that no created files are omitted when pushing or committing.

## Build and Verification Commands
- **Compile**: `./mvnw clean compile` (Unix) or `.\mvnw.cmd clean compile` (Windows)
- **Run Tests**: `./mvnw test` (Unix) or `.\mvnw.cmd test` (Windows)
- **Package Application**: `./mvnw clean package` (Unix) or `.\mvnw.cmd clean package` (Windows)
- **Run Application**: `./mvnw spring-boot:run` (Unix) or `.\mvnw.cmd spring-boot:run` (Windows)

## Project Structure & Architecture
- `com.github.thibstars.jhaapimcp`
  - `configuration`: Spring configuration properties and JHAAPI service bean definitions.
  - `tools`: MCP tool definitions exposing Home Assistant capabilities (states, services, lights, switches, media, templates, configuration, events, history, logbook, error logs).
  - `service`: High-level services wrapping JHAAPI calls and formatting responses for language model utility.
