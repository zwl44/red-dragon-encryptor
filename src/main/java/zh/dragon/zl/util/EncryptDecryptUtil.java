package zh.dragon.zl.util;

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
	public static boolean fileOutputStream = false;
	public static byte[] ensOrDecSd = new byte[1024];
	private static byte[] ensOrDecSdNew = new byte[1024];

	// 加密文件
	public static void encryptFile(File inputFile, File outputFile, BiConsumer<Long, Long> progressCallback) throws IOException {
		try (FileInputStream fis = new FileInputStream(inputFile);
		     FileOutputStream fos = new FileOutputStream(outputFile)) {
			long totalBytes = inputFile.length();
			long bytesRead = 0;

			// 写入原始文件的扩展名到加密文件
			String originalExtension = getFileExtension(inputFile);
			fos.write((MARKER + originalExtension + "\n").getBytes());
			doSomething(progressCallback, fis, outputFile, fos, totalBytes, bytesRead);
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
				long totalBytes = inputFile.length();
				long bytesRead = 0;
				doSomething(progressCallback, fis, outputFile, fos, totalBytes, bytesRead);
			}
		}
	}

	private static void doSomething(BiConsumer<Long, Long> progressCallback, FileInputStream fis, File outputFile, FileOutputStream fos, long totalBytes, long bytesRead) throws IOException {
		fileOutputStream = false;
		int len;
		while ((len = fis.read(ensOrDecSdNew)) != -1) {
			if (fileOutputStream) {
				fos.close();
				if (outputFile.exists()) {
					outputFile.delete();
				}
				progressCallback.accept((long) 0, totalBytes);
				return;
			}
			// 解密逻辑 (此处直接写入原始字节)
			fos.write(ensOrDecSdNew, 0, len);
			bytesRead += len;
			progressCallback.accept(bytesRead, totalBytes);
			if (ensOrDecSd.length != ensOrDecSdNew.length) {
				ensOrDecSdNew = new byte[ensOrDecSd.length];
			}
		}
		ensOrDecSdNew = new byte[1024];
	}

	// 获取文件扩展名
	private static String getFileExtension(File file) {
		String name = file.getName();
		int lastDot = name.lastIndexOf('.');
		return (lastDot == -1) ? "" : name.substring(lastDot + 1);
	}
}
