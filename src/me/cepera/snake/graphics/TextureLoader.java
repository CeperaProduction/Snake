package me.cepera.snake.graphics;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

import javafx.scene.image.Image;
import me.cepera.snake.elements.IMapElement;

/**
 * Утилитарный класс, помогающий загружать текстуры из файла программы
 * @author Cepera
 *
 */
public class TextureLoader {

	/**
	 * Кэш загруженных текстур
	 */
	private static HashMap<String, Image> textureCache = new HashMap<>();
	
	/**
	 * Загрузка запрашиваемой текстуры из файла программы либо её взятие из кэша, если она уже была загружена ранее.
	 * @param name - Имя или адресс текстуры. Корнем расположения искомой текстуры является "/assets/textures/"
	 * @return
	 */
	public static Image getTexture(String name) {
		String path = "/assets/textures/"+name;
		Image texture = textureCache.get(path);
		if(texture == null) {
			InputStream input = RenderLoop.class.getResourceAsStream(path);
			if(input == null) throw new RuntimeException(new FileNotFoundException("Resource not found: "+path));
			texture = new Image(input);
			textureCache.put(path, texture);
		}
		return texture;
	}
	
	/**
	 * Получение текстуры для объекта игрового мира
	 * @param element
	 * @return
	 */
	public static Image getTexture(IMapElement element) {
		return getTexture("elements/"+element.textureFile());
	}
	
	/**
	 * Очистка кэша загруженных текстур
	 */
	public static void clearTextureCache() {
		textureCache.clear();
	}
	
}
