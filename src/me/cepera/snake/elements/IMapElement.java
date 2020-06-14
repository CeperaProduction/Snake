package me.cepera.snake.elements;

import me.cepera.snake.EatResult;
import me.cepera.snake.PairXY;

/**
 * Минимальный интерфейс для объекта, распологающегося в игровом мире.
 * @author Cepera
 *
 */
public interface IMapElement {
	
	/**
	 * Результат "взаимодействия" с этим объектом
	 */
	public EatResult getEatResult();
	
	/**
	 * Обработка тика игрового мира
	 */
	public void onTick();
	
	/**
	 * Существует ли объект в игровом мире
	 * @return
	 */
	public boolean exists();
	
	/**
	 * Текущая позиция объекта в игровом мире
	 * @return
	 */
	public PairXY getPosition();
	
	/**
	 * Установка текущей позиции объекта в игровом мире
	 * @param position - новая позиция
	 */
	public void setPosition(PairXY position);
	
	/**
	 * Получение предыдущего положения объекта в игровом мире
	 * @return
	 */
	public PairXY getPrevPosition();
	
	/**
	 * Название файла с текстурой объекта (Без какого-либо пути, только название файла. Например "apple.png")
	 * @return
	 */
	public String textureFile();
	
}
