package kuleuven.group2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import kuleuven.group2.ui.MainController;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("ui/Main.fxml"));
			Parent root = (Parent) loader.load();
			MainController controller = (MainController) loader.getController();
			Scene scene = new Scene(root, 400, 400);
			primaryStage.setTitle("JUnit Test Daemon");
			primaryStage.setScene(scene);
			primaryStage.setOnCloseRequest(controller);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

}
