package me.cepera.snake;

import javafx.application.Application;
import me.cepera.snake.graphics.Window;

/**
 * Главный класс программы. В нём находится метод main.
 * @author Cepera
 *
 */
public final class Snake {
	
	/**
	 * Версия программы
	 */
	public static final String VERSION = "1.1.0.0";
	
	/**
	 * Переменная, хранящая экземпляр главного класса
	 */
	private static Snake instance;
	
	public static void main(String[] args) {
		if(instance != null) throw new IllegalStateException("The application is already initialized");
		instance();
		Application.launch(Window.class, args);
	}
	
	/**
	 * Получение экземпляра. Реализация паттерна Singleton
	 * @return
	 */
	public static Snake instance() {
		if(instance == null) instance = new Snake();
		return instance;
	}
	
	/**
	 * Текущая используемая фабрика игр. Размещая здесь новые фабрики игр возможно создавать другие игры.
	 */
	public IGameFactory gameFactory;
	
	private Game game;
	
	private Snake() {
		gameFactory = new DefaultGameFactory();
	}
	
	/**
	 * Создание и запуск (или перезапуск) процесса игры
	 * @param difficulty
	 */
	public void startNewGame(Difficulty difficulty) {
		if(game != null) game.stop();
		game = gameFactory.makeGame(difficulty);
		game.start();
	}
	
	/**
	 * Получение объекта, описывающего текущий ход игры
	 * @return
	 */
	public Game getGame() {
		return game;
	}

}
