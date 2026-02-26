# NukNagnel

NukNagnel is a task management chatbot with both CLI and GUI interfaces.
It supports:
- `todo`, `deadline`, `event` task creation
- mark/unmark/delete/list task operations
- natural weekday date parsing (e.g., `Mon`, `Tue 1400`)
- persistent storage with graceful recovery from corrupted lines
- duplicate-task detection and user-friendly error feedback

## Running

Prerequisites: JDK 17.

1. Open the project in IntelliJ IDEA.
1. Run the JavaFX launcher:
   - Class: `nuknagnel.Launcher`
1. Or run tests:
   - `./gradlew test`

## Build

Create a runnable fat JAR:
- `./gradlew shadowJar`

Output artifact:
- `build/libs/nuknagnel.jar`
