package zh.dragon.zl.util;

import javafx.scene.image.Image;
import javafx.scene.layout.*;


/**
 * @author zwl
 * @date 2024年09月08日 19:50:13
 * @packageName zh.dragon.zl.util
 * @className BackGroundUtil
 */
public class BackGroundUtil {
	public static Background addBackGroundImage(String path) {
		// 创建背景图片
		Image backgroundImage = new Image(path);

		BackgroundImage background = new BackgroundImage(
				backgroundImage,
				BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT,
				BackgroundPosition.DEFAULT,
				new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, false, true)
		);

		return new Background(background);
	}
}
