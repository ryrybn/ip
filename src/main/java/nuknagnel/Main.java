package nuknagnel;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nuknagnel.gui.MainWindow;

/** JavaFX entry point for NukNagnel. */
public class Main extends Application {
  private static final double DEFAULT_WIDTH = 760;
  private static final double DEFAULT_HEIGHT = 560;
  private static final double MIN_WIDTH = 620;
  private static final double MIN_HEIGHT = 460;
  private static final NukNagnel NUK_NAGNEL = new NukNagnel("data/nuknagnel.txt");

  @Override
  public void start(Stage stage) throws IOException {
    FXMLLoader loader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
    Scene scene = new Scene(loader.load(), DEFAULT_WIDTH, DEFAULT_HEIGHT);
    scene.getStylesheets().add(Main.class.getResource("/view/style.css").toExternalForm());
    stage.setTitle("NukNagnel");
    stage.setMinWidth(MIN_WIDTH);
    stage.setMinHeight(MIN_HEIGHT);
    stage.setScene(scene);

    MainWindow mainWindow = loader.getController();
    mainWindow.setNukNagnel(NUK_NAGNEL);
    mainWindow.showWelcome();

    stage.show();
  }
}
