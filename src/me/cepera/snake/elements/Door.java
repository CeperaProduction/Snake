package me.cepera.snake.elements;

import me.cepera.snake.EatResult;
import me.cepera.snake.PairXY;

/**
 * Класс, описывающий дверь (Реализация {@link IMapElement})
 * @author Cepera
 *
 */
public class Door extends AbstractElement{

	/**
	 * Открыта ли дверь
	 */
	private boolean open = false;
	
	public Door(PairXY position) {
		super(position);
	}

	@Override
	public EatResult getEatResult() {
		return open ? EatResult.FINISH : EatResult.DEAD;
	}
	
	/**
	 * Открыта ли дверь
	 */
	public boolean isOpen() {
		return open;
	}
	
	/**
	 * Изменение статуса двери
	 * @param open - true: открыта. false: закрыта
	 */
	public void setOpen(boolean open) {
		this.open = open;
	}
	
	@Override
	public String textureFile() {
		return isOpen() ? "door_open.png" : "door_close.png";
	}

}
