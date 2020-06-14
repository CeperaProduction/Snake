package me.cepera.snake.elements;

import me.cepera.snake.PairXY;

/**
 * Общий класс, описывающий объект в игровом мире. 
 * Имеет некоторую стандартную реализацию некоторых методов интерфейса {@link IMapElement}
 * @author Cepera
 *
 */
public abstract class AbstractElement implements IMapElement{

	/**
	 * Текущая позиция объекта
	 */
	private PairXY position;
	
	/**
	 * Предыдущая позиция объекта
	 */
	private PairXY prevPosition;
	
	public AbstractElement(PairXY position) {
		this.position = position;
		this.prevPosition = position;
	}

	@Override
	public void onTick() {}

	@Override
	public boolean exists() {
		return true;
	}

	@Override
	public PairXY getPosition() {
		return position;
	}
	
	@Override
	public void setPosition(PairXY position) {
		this.prevPosition = this.position;
		this.position = position;
	}
	
	@Override
	public PairXY getPrevPosition() {
		return prevPosition;
	}
	
	@Override
	public String textureFile() {
		return this.getClass().getSimpleName().toLowerCase()+".png";
	}

}
