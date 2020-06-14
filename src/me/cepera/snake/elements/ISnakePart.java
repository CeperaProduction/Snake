package me.cepera.snake.elements;

/**
 * Интерфейс, описывающий элемент змеи
 * @author Cepera
 *
 */
public interface ISnakePart extends IMapElement{

	/**
	 * Возвращает следующий элемент змеи
	 * @return
	 */
	public ISnakePart getChild();
	
	/**
	 * Устанавливает следующий элемент змеи
	 * @param part
	 */
	public <P extends ISnakePart & IMapElement> void setChild(P part);
	
}
