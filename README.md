# craig-defaults-gradle-plugin

A custom plugin that applies my preferred Gradle settings.

## What It Does

### Spotless & Git Hooks

If the Spotless plugin has been added to the build file, git hooks are installed to ensure spotless is executed as a pre-commit hook.

### Publishing

If the maven-publish plugin has been added to the build file, a maven publication will be constructed pointing at my Nexus server.

### Spring Boot Jar Building

If it is a Spring Boot project, and the bootJar task is enabled, the default jar task will be disabled.