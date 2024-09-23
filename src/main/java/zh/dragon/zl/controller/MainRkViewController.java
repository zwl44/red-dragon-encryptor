package zh.dragon.zl.controller;


import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import zh.dragon.zl.util.EncryptDecryptUtil;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class MainRkViewController {


	@FXML
	private ProgressBar progressBar;
	@FXML
	private ImageView imageView;
	@FXML
	private Label filePathLabel;
	@FXML
	private Label schedule;
	private File selectedFile;
	private File outputDirectory;


	@FXML
	public void initialize() {
		Image image = new Image("/zh/dragon/zl/images/upload.jpg");
		imageView.setImage(image);
		// 允许拖拽文件
		imageView.setOnDragOver(event -> {
			if (event.getDragboard().hasFiles()) {
				event.acceptTransferModes(javafx.scene.input.TransferMode.COPY);
			}
			event.consume();
		});

		imageView.setOnDragDropped(event -> {
			if (event.getDragboard().hasFiles()) {
				selectedFile = event.getDragboard().getFiles().get(0);
				filePathLabel.setText(selectedFile.getAbsolutePath());
			}
			event.setDropCompleted(true);
			event.consume();
		});

		// 添加监听器来监听进度条的变化
		progressBar.progressProperty().addListener((observable, oldValue, newValue) -> {
			double v = newValue.doubleValue();
			BigDecimal decimal = new BigDecimal(v).setScale(2, RoundingMode.HALF_UP);
			// 乘以100并转换为整数
			BigInteger integerValue = decimal.multiply(new BigDecimal(100)).toBigInteger();
			schedule.setText(integerValue + "%");
		});
	}

	@FXML
	private void encryptFile() {
		if (selectedFile != null) {
			Task<Void> task = new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					if (outputDirectory == null) {
						outputDirectory = selectedFile.getParentFile();
					}
					String selectedFileName = selectedFile.getName();
					int i = selectedFileName.lastIndexOf(".");
					String substring = selectedFileName.substring(0, i);
					File encryptedFile = new File(outputDirectory, substring + ".dragon");
					EncryptDecryptUtil.encryptFile(selectedFile, encryptedFile, this::updateProgress);
					return null;
				}
			};

			task.setOnSucceeded(event -> showAlert("加密完成", "文件已成功加密！"));
			progressBar.progressProperty().bind(task.progressProperty());
			new Thread(task).start();
		} else {
			showAlert("错误", "请先选择要加密的文件！");
		}
	}

	@FXML
	private void decryptFile() {
		if (selectedFile != null && selectedFile.getName().endsWith(".dragon")) {
			Task<Void> task = new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					if (outputDirectory == null) {
						outputDirectory = selectedFile.getParentFile();
					}
					EncryptDecryptUtil.decryptFile(selectedFile, outputDirectory, this::updateProgress);
					return null;
				}
			};

			task.setOnSucceeded(event -> showAlert("解密完成", "文件已成功解密！"));
			progressBar.progressProperty().bind(task.progressProperty());
			new Thread(task).start();
		} else {
			showAlert("解密失败", "请选择一个 '.dragon' 文件进行解密。");
		}
	}

	@FXML
	private void chooseOutputPath() {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		File chosenDir = directoryChooser.showDialog(filePathLabel.getScene().getWindow());
		if (chosenDir != null) {
			outputDirectory = chosenDir;
			showAlert("输出路径", "选择的输出路径为：" + outputDirectory.getAbsolutePath());
		}
	}

	private void showAlert(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}
}
