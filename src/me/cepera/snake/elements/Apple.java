package me.cepera.snake.elements;

import me.cepera.snake.EatResult;
import me.cepera.snake.PairXY;

/**
 * Класс, описывающий яблоко (Реализация {@link IMapElement})
 * @author Cepera
 *
 */
public class Apple extends AbstractElement{

	/**
	 * Количество тиков, которое это яблоко ещё может прожить
	 */
	private int lifetime;
	
	public Apple(PairXY position, int lifetime) {
		super(position);
		this.lifetime = lifetime;
	}

	@Override
	public EatResult getEatResult() {
		return EatResult.ADD_SCORE;
	}
	
	/**
	 * Количество тиков, которое это яблоко ещё может прожить
	 */
	public int getLifetime() {
		return lifetime;
	}
	
	/**
	 * Установка количества тиков, которое это яблоко еще может прожить
	 * @param lifetime
	 */
	public void setLifetime(int lifetime) {
		this.lifetime = lifetime;
	}
	
	@Override
	public boolean exists() {
		return lifetime > 0;
	}
	
	@Override
	public void onTick() {
		lifetime--;
	}
	
	@Override
	public String textureFile() {
		if(lifetime > 7 || lifetime % 2 == 0) return "apple.png";
		return "apple_expiring.png";
	}

}
