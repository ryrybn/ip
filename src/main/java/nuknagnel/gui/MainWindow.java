package nuknagnel.gui;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import nuknagnel.NukNagnel;

/** Controller for the app's main GUI window. */
public class MainWindow {
  @FXML private ScrollPane scrollPane;
  @FXML private VBox dialogContainer;
  @FXML private TextField userInput;
  @FXML private Button sendButton;

  private NukNagnel nukNagnel;

  @FXML
  private void initialize() {
    scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
  }

  public void setNukNagnel(NukNagnel nukNagnel) {
    this.nukNagnel = nukNagnel;
  }

  public void showWelcome() {
    dialogContainer
        .getChildren()
        .add(DialogBox.getBotDialog(nukNagnel.getWelcomeMessage(), dialogContainer.widthProperty()));
  }

  @FXML
  private void handleUserInput() {
    String input = userInput.getText();
    if (input == null || input.trim().isEmpty()) {
      return;
    }
    String response = nukNagnel.getResponse(input);
    boolean isErrorResponse = isErrorResponse(response);

    dialogContainer.getChildren().addAll(
        DialogBox.getUserDialog(input, dialogContainer.widthProperty()),
        isErrorResponse
            ? DialogBox.getErrorDialog(response, dialogContainer.widthProperty())
            : DialogBox.getBotDialog(response, dialogContainer.widthProperty()));
    userInput.clear();

    if (nukNagnel.isExit()) {
      PauseTransition delay = new PauseTransition(Duration.millis(250));
      delay.setOnFinished(event -> Platform.exit());
      delay.play();
    }
  }

  private boolean isErrorResponse(String response) {
    return response.startsWith("I couldn't")
        || response.startsWith("I need")
        || response.startsWith("Use `")
        || response.startsWith("That task number")
        || response.startsWith("The event end time");
  }
}
