package me.cepera.snake;

/**
 * Слушатель тиков мира игры
 * @author Cepera
 *
 */
public interface IWorldTickHandler {

	/**
	 * Обработчик тиков мира игры
	 */
	public void handle(World world, int tickNumber);
	
}
