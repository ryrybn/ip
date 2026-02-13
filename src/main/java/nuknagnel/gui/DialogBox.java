package nuknagnel.gui;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/** A single chat message bubble in the conversation view. */
public class DialogBox extends HBox {
  @FXML private Label dialog;
  @FXML private ImageView displayPicture;

  private DialogBox(String text, Image img) {
    FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("/view/DialogBox.fxml"));
    fxmlLoader.setController(this);
    fxmlLoader.setRoot(this);
    try {
      fxmlLoader.load();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    dialog.setText(text);
    displayPicture.setImage(img);
  }

  public static DialogBox getUserDialog(String text) {
    Image userImage = new Image(DialogBox.class.getResourceAsStream("/images/DaUser.png"));
    DialogBox dialogBox = new DialogBox(text, userImage);
    dialogBox.getStyleClass().add("user-dialog");
    return dialogBox;
  }

  public static DialogBox getBotDialog(String text) {
    Image dukeImage = new Image(DialogBox.class.getResourceAsStream("/images/DaDuke.png"));
    DialogBox dialogBox = new DialogBox(text, dukeImage);
    dialogBox.flip();
    return dialogBox;
  }

  private void flip() {
    getStyleClass().add("bot-dialog");
    this.getChildren().setAll(displayPicture, dialog);
  }
}
