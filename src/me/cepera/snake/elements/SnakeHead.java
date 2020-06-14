package me.cepera.snake.elements;

import me.cepera.snake.EatResult;
import me.cepera.snake.Game;
import me.cepera.snake.PairXY;

/**
 * Класс, описывающий голову змеи (Реализация {@link IMapElement и ISnakePart})
 * @author Cepera
 *
 */
public class SnakeHead extends AbstractElement implements ISnakePart{

	/**
	 * Элемент змеи, следующий за головой
	 */
	private ISnakePart child;
	
	/**
	 * Ссылка на игру
	 */
	private Game game;
	
	public SnakeHead(PairXY position, Game game) {
		super(position);
		this.game = game;
	}

	@Override
	public EatResult getEatResult() {
		return EatResult.DEAD;
	}
	
	/**
	 * Производит обработку тика. Голова движется по заданному направлению и последовательно тянет за собой весь хвост.
	 */
	@Override
	public void onTick() {
		setPosition(getPosition().plus(game.getMotion()));
		ISnakePart part = this;
		while(part.getChild() != null) {
			ISnakePart child = part.getChild();
			child.setPosition(part.getPrevPosition());
			part = child;
		}
	}

	@Override
	public ISnakePart getChild() {
		return child;
	}
	
	/**
	 * Увеличить длинну змеи на 1
	 */
	public void increaseTail() {
		ISnakePart part = this;
		while(part.getChild() != null) part = part.getChild();
		SnakeTail el = new SnakeTail(part.getPrevPosition());
		part.setChild(el);
		game.getWorld().addElement(el);
	}

	@Override
	public <P extends ISnakePart & IMapElement> void setChild(P part) {
		this.child = part;
	}
	
	/**
	 * Подкласс, реализующий элемент хвоста змеи (Реализация {@link IMapElement и ISnakePart}
	 * @author Cepera
	 *
	 */
	public class SnakeTail extends AbstractElement implements ISnakePart{

		/**
		 * Элемент змеи, следующий за текущим
		 */
		private ISnakePart child;
		
		private SnakeTail(PairXY position) {
			super(position);
		}

		@Override
		public EatResult getEatResult() {
			return EatResult.DEAD;
		}

		@Override
		public ISnakePart getChild() {
			return child;
		}

		@Override
		public <P extends ISnakePart & IMapElement> void setChild(P part) {
			this.child = part;
		}

	}

}
