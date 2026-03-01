package nuknagnel;

import javafx.application.Application;

/** Launches the JavaFX application. */
public class Launcher {
  public static void main(String[] args) {
    try {
      Application.launch(Main.class, args);
    } catch (RuntimeException e) {
      if (!isJavaFxStartupFailure(e)) {
        throw e;
      }
      System.err.println("GUI is unavailable in this environment. Starting CLI mode instead.");
      NukNagnel.main(args);
    }
  }

  private static boolean isJavaFxStartupFailure(Throwable throwable) {
    Throwable current = throwable;
    while (current != null) {
      String message = current.getMessage();
      if (message != null
          && (message.contains("No toolkit found")
              || message.contains("QuantumRenderer")
              || message.contains("no suitable pipeline found"))) {
        return true;
      }
      current = current.getCause();
    }
    return false;
  }
}
