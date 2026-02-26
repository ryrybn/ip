package nuknagnel.gui;

import java.io.IOException;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableDoubleValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;

/** A single chat message bubble in the conversation view. */
public class DialogBox extends HBox {
  private static final double BUBBLE_PADDING_WIDTH = 140;
  private static final double MAX_BUBBLE_WIDTH = 620;

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
    styleProfilePicture();
  }

  public static DialogBox getUserDialog(String text, ObservableDoubleValue containerWidth) {
    Image userImage = new Image(DialogBox.class.getResourceAsStream("/images/DaUser.png"));
    DialogBox dialogBox = new DialogBox(text, userImage);
    dialogBox.getStyleClass().add("user-dialog");
    dialogBox.bindBubbleWidth(containerWidth);
    return dialogBox;
  }

  public static DialogBox getBotDialog(String text, ObservableDoubleValue containerWidth) {
    Image botImage = new Image(DialogBox.class.getResourceAsStream("/images/DaDuke.png"));
    DialogBox dialogBox = new DialogBox(text, botImage);
    dialogBox.flip();
    dialogBox.bindBubbleWidth(containerWidth);
    return dialogBox;
  }

  public static DialogBox getErrorDialog(String text, ObservableDoubleValue containerWidth) {
    DialogBox dialogBox = getBotDialog(text, containerWidth);
    dialogBox.getStyleClass().add("error-dialog");
    return dialogBox;
  }

  private void flip() {
    getStyleClass().add("bot-dialog");
    this.setAlignment(Pos.TOP_LEFT);
    this.getChildren().setAll(displayPicture, dialog);
  }

  private void bindBubbleWidth(ObservableDoubleValue containerWidth) {
    dialog.maxWidthProperty().bind(
        Bindings.min(MAX_BUBBLE_WIDTH, Bindings.subtract(containerWidth, BUBBLE_PADDING_WIDTH)));
  }

  private void styleProfilePicture() {
    Circle clip = new Circle(15, 15, 15);
    displayPicture.setClip(clip);
  }
}
