package me.cepera.snake;

/**
 * Слушатель событий игры
 * @author Cepera
 *
 */
public interface IGameListener {

	/**
	 * Обработчик события "взаимодействия"
	 * @param game
	 */
	public void onEat(Game game);
	
	/**
	 * Обработчик события победы
	 * @param game
	 */
	public void onWin(Game game);
	
	/**
	 * Обработчик события проигрыша
	 * @param game
	 */
	public void onLose(Game game);
	
}
