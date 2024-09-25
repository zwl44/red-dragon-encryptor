package zh.dragon.zl.controller;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import zh.dragon.zl.util.BackGroundUtil;


/**
 * @author zwl
 * @date 2024年09月09日 20:54:08
 * @packageName zh.dragon.zl.controller
 * @className CommonViewController
 */
public class CommonViewController extends Application {
	@FXML
	private Hyperlink hyperlink;
	@FXML
	private ImageView imageView1;
	@FXML
	private ImageView imageView2;
	@FXML
	private ImageView imageView3;
	@FXML
	private AnchorPane anchorPane;

	@FXML
	public void initialize() {
		hyperlink.setOnAction((event) -> {
			getHostServices().showDocument(hyperlink.getText());
		});
		Image image1 = new Image("/zh/dragon/zl/images/gg/wx.jpg");
		Image image2 = new Image("/zh/dragon/zl/images/gg/zfb.jpg");
		Image image3 = new Image("/zh/dragon/zl/images/gg/gzh.jpg");
		imageView1.setImage(image1);
		imageView2.setImage(image2);
		imageView3.setImage(image3);
		anchorPane.setBackground(BackGroundUtil.addBackGroundImage("/zh/dragon/zl/images/back2.png"));

	}

	@Override
	public void start(Stage primaryStage) throws Exception {

	}
}
