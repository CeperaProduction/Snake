package me.cepera.snake.graphics;

import javafx.scene.Group;

/**
 * Минимальный интерфейс, описывающий графическое меню
 * @author Cepera
 *
 */
public interface IGui extends IBindingProvider{
	
	/**
	 * Отобразить меню
	 * @param window - окно игры
	 * @param parent - родитель, который должен содержать элементы меню
	 */
	public void show(Window window, Group parent);
	
	/**
	 * Закрытие меню
	 * @param window - окно игры
	 * @param parent - родитель, который содержит элементы меню
	 */
	public void close(Window window, Group parent);
	
}
