package zh.dragon.zl.util;

import zh.dragon.zl.controller.MainRkViewController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.function.BiConsumer;

/**
 * @author 16784
 */
public class EncryptDecryptUtil {

	private static final String MARKER = "ORIGINAL_EXT:";

	// 加密文件
	public static void encryptFile(File inputFile, File outputFile, BiConsumer<Long, Long> progressCallback) throws IOException {
		try (FileInputStream fis = new FileInputStream(inputFile);
		     FileOutputStream fos = new FileOutputStream(outputFile)) {

			byte[] buffer = new byte[MainRkViewController.dowJsDL * 1024 / 1000];
			long totalBytes = inputFile.length();
			long bytesRead = 0;

			// 写入原始文件的扩展名到加密文件
			String originalExtension = getFileExtension(inputFile);
			fos.write((MARKER + originalExtension + "\n").getBytes());

			int len;
			while ((len = fis.read(buffer)) != -1) {
				// 加密逻辑 (此处简单地写入原始字节，实际可使用加密算法)
				fos.write(buffer, 0, len);
				bytesRead += len;
				progressCallback.accept(bytesRead, totalBytes);
			}
		}
	}

	// 解密文件
	public static void decryptFile(File inputFile, File outputDirectory, BiConsumer<Long, Long> progressCallback) throws IOException {
		try (FileInputStream fis = new FileInputStream(inputFile)) {

			int byteData = fis.read();
			StringBuilder lineBuilder = new StringBuilder();

			while (byteData != -1 && byteData != '\n') {
				lineBuilder.append((char) byteData);
				byteData = fis.read();
			}
			String firstLine = lineBuilder.toString();
			String originalExtension = firstLine.substring(MARKER.length());
			File outputFile = new File(outputDirectory, inputFile.getName().replace(".dragon", "") + "." + originalExtension);
			try (FileOutputStream fos = new FileOutputStream(outputFile)) {

				byte[] buffer = new byte[MainRkViewController.dowJsDL * 1024];
				long totalBytes = inputFile.length();
				long bytesRead = 0;

				int len;
				while ((len = fis.read(buffer)) != -1) {
					// 解密逻辑 (此处直接写入原始字节)
					fos.write(buffer, 0, len);
					bytesRead += len;
					progressCallback.accept(bytesRead, totalBytes);
				}
			}
		}
	}

	// 获取文件扩展名
	private static String getFileExtension(File file) {
		String name = file.getName();
		int lastDot = name.lastIndexOf('.');
		return (lastDot == -1) ? "" : name.substring(lastDot + 1);
	}
}
