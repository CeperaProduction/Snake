package me.cepera.snake;

/**
 * Интерфейс, описывающий фабрику игр, создающую игры с некоторыми правилами.
 * @author Cepera
 *
 */
public interface IGameFactory {

	/**
	 * Создает новый готовый к старту экземпляр игры. 
	 * В ходе исполнения данного метода карта должна быть создана, заполнена и подготовлена к старту, 
	 * все слушатели должны быть зарегистрированы.
	 * 
	 * @param difficulty
	 * @return
	 */
	public Game makeGame(Difficulty difficulty);
	
}
