package zh.dragon.zl;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import zh.dragon.zl.util.BackGroundUtil;

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
		VBox root = loader.load();
		root.setBackground(BackGroundUtil.addBackGroundImage("/zh/dragon/zl/images/bg/bh.jpg"));
		primaryStage.setScene(new Scene(root));
		primaryStage.show();
	}


}
