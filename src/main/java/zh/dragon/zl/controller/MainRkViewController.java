package zh.dragon.zl.controller;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import zh.dragon.zl.util.EncryptDecryptUtil;

import java.io.File;
import java.io.IOException;
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
	@FXML
	private Label outLabel;
	@FXML
	private ComboBox<Integer> comboBox;
	private File selectedFile;
	private File outputDirectory;

	@FXML
	public void initialize() {
		Image image = new Image("/zh/dragon/zl/images/upload.jpg");
		imageView.setImage(image);
		imageView.setOnMouseClicked(event -> {
			// 创建 FileChooser 实例
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("选择文件");
			// 设置文件过滤器，可以选择所有类型或特定类型
			fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Folders", "*"),
					new FileChooser.ExtensionFilter("All Files", "*.*"));
			File selectedFile = fileChooser.showOpenDialog(imageView.getScene().getWindow());

			// 如果选择了文件
			if (selectedFile != null) {
				this.selectedFile = selectedFile;
				filePathLabel.setText(selectedFile.getAbsolutePath());
				outLabel.setText(selectedFile.getParentFile().getPath());
			}
		});

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
				outLabel.setText(selectedFile.getParentFile().getPath());
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
		//对多选选择器进行定义
		ObservableList<Integer> integerObservableList = FXCollections.observableArrayList(1, 2, 4, 8, 16, 32, 64, 128);
		comboBox.setItems(integerObservableList);
		comboBox.setValue(1);
		// 为ComboBox设置一个自定义的CellFactory
		comboBox.setCellFactory(new Callback<ListView<Integer>, ListCell<Integer>>() {
			@Override
			public ListCell<Integer> call(ListView<Integer> param) {
				return new ListCell<Integer>() {
					@Override
					protected void updateItem(Integer item, boolean empty) {
						super.updateItem(item, empty);
						if (item == null || empty) {
							setText(null);
						} else {
							// 将整数转换为字符串，并添加后缀
							setText(item.toString() + " Mb/S");
						}
					}
				};
			}
		});
		// 添加选择项变化监听器
		comboBox.valueProperty().addListener((observable, oldValue, newValue) -> EncryptDecryptUtil.ensOrDecSd = new byte[1024 * newValue]);

	}

	@FXML
	private void changeOutFilePath() {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		File desktop = new File(System.getProperty("user.home"), "Desktop");
		if (desktop.exists()) {
			directoryChooser.setInitialDirectory(desktop);
		}
		directoryChooser.setTitle("选择一个文件夹");
		File selectedDirectory = directoryChooser.showDialog(imageView.getScene().getWindow());
		// 输出所选择的文件夹路径
		if (selectedDirectory != null) {
			outLabel.setText(selectedDirectory.getAbsolutePath());
			outputDirectory = new File(selectedDirectory.getAbsolutePath());
		} else {
			System.out.println("没有选择文件夹");
		}
	}

	@FXML
	private void resetFile() throws IOException {
		EncryptDecryptUtil.fileOutputStream = true;
	}

	@FXML
	private void encryptFile() {
		if (selectedFile != null) {
			schedule.setVisible(true);
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

			task.setOnSucceeded(event -> {
				if (!EncryptDecryptUtil.fileOutputStream) {
					showAlert("加密完成", "文件已成功加密！");
				} else {
					EncryptDecryptUtil.fileOutputStream = false;
				}
			});
			progressBar.progressProperty().bind(task.progressProperty());
			new Thread(task).start();
		} else {
			showAlert("错误", "请先选择要加密的文件！");
		}
	}

	@FXML
	private void decryptFile() {
		if (selectedFile != null && selectedFile.getName().endsWith(".dragon")) {
			schedule.setVisible(true);
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

			task.setOnSucceeded(event -> {
				if (!EncryptDecryptUtil.fileOutputStream) {
					showAlert("解密完成", "文件已成功解密！");
				} else {
					EncryptDecryptUtil.fileOutputStream = false;
				}
			});
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
