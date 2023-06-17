# craig-defaults-gradle-plugin

A custom plugin that applies my preferred Gradle settings.

## What It Does

### Spotless & Git Hooks

If the Spotless plugin has been added to the build file, git hooks are installed to ensure spotless is executed as a pre-commit hook.

### Publishing

If the maven-publish plugin has been added to the build file, a maven publication will be constructed pointing at my Nexus server.

If this is done in a Spring Boot application, and the bootJar task is enabled, the artifact produced by that task will be published. Otherwise, the kotlin artifact will be published.

### Spring Boot Jar Building

If it is a Spring Boot project, and the bootJar task is enabled, the default jar task will be disabled.