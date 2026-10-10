# JHAAPI-MCP Agent Harness Guidelines

## Overview
JHAAPI-MCP exposes Home Assistant REST and WebSocket functionalities via the Model Context Protocol (MCP) using the JHAAPI Java client library.

### Core Stack
- **Language**: Java 25 (LTS)
- **Framework**: Spring Boot 3.4.x / Spring AI MCP Server
- **Client Library**: JHAAPI 1.0.15-SNAPSHOT
- **Build Tool**: Apache Maven Wrapper

## Operating Invariants
1. **No Single-Letter Variables**: Never use single-letter variable names (the sole exception is `catch (Exception exception)` or `catch (Exception e)`).
2. **No Abbreviations**: Always use full descriptive names for variables, classes, methods, parameters, and fields (e.g., `configuration`, `request`, `response`, `properties`, `context`, `identifier`).
3. **Dead Code Elimination**: Remove all dead, obsolete, or unused code promptly.
4. **Import Cleanliness**: Keep imports organized and remove unused imports.
5. **Git Tracking for New Files**: Always track newly created files with `git add`.
