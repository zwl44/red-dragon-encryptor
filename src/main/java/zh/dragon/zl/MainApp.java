package zh.dragon.zl;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
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
		// 设置应用程序图标
		Image icon = new Image(getClass().getResourceAsStream("/zh/dragon/zl/images/ico.png"));
		primaryStage.getIcons().add(icon);
		VBox root = loader.load();
		BorderPane borderPane = (BorderPane) root.getChildren().get(0);
		AnchorPane center = (AnchorPane) borderPane.getCenter();
		center.setBackground(BackGroundUtil.addBackGroundImage("/zh/dragon/zl/images/bg/bh.jpg"));
		AnchorPane anchorPanePane = FXMLLoader.load(getClass().getResource("controller/CommonView.fxml"));
		borderPane.setRight(anchorPanePane);
		primaryStage.setScene(new Scene(root));
		primaryStage.show();
	}


}
