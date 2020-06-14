package me.cepera.snake.elements;

import me.cepera.snake.EatResult;
import me.cepera.snake.PairXY;

/**
 * Класс, описывающий мяч (Реализация {@link IMapElement})
 * @author Cepera
 *
 */
public class Ball extends AbstractElement{

	/**
	 * Направление движения мяча
	 */
	private PairXY motion;
	
	public Ball(PairXY startPosition, PairXY motion) {
		super(startPosition);
		this.motion = motion;
	}

	@Override
	public EatResult getEatResult() {
		return EatResult.DEAD;
	}
	
	@Override
	public void onTick() {
		setPosition(getPosition().plus(motion));
	}

}
