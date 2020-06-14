package me.cepera.snake.elements;

import me.cepera.snake.EatResult;
import me.cepera.snake.PairXY;

/**
 * Класс, описывающий элемент стены (Реализация {@link IMapElement и ISnakePart})
 * @author Cepera
 *
 */
public class Wall extends AbstractElement{

	public Wall(PairXY position) {
		super(position);
	}

	@Override
	public EatResult getEatResult() {
		return EatResult.DEAD;
	}

}
