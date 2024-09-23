package zh.dragon.zl;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author 16784
 */
public class MainApp extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("controller/MainRkView.fxml"));
		primaryStage.setTitle("文件加密与解密");
		Parent root = loader.load();
		primaryStage.setScene(new Scene(root));
		primaryStage.show();
	}


}
