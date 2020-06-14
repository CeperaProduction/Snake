package me.cepera.snake;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import me.cepera.snake.elements.Door;
import me.cepera.snake.elements.IMapElement;
import me.cepera.snake.elements.SnakeHead;

/**
 * Класс, описывающий мир игры
 * @author Cepera
 *
 */
public class World {

	/**
	 * Размеры мира
	 */
	private final int width, height;
	
	/**
	 * Пул объектов, расположенных в игровом мире
	 */
	private Set<IMapElement> elements = new CopyOnWriteArraySet<>();
	
	/**
	 * Счетчик тиков мира
	 */
	private int tickCounter = 0;
	
	/**
	 * Слушатели тиков мира
	 */
	private List<IWorldTickHandler> tickHandlers = new ArrayList<>();
	
	public World(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	/**
	 * Ширина мира
	 * @return
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * Высота мира
	 * @return
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * Добавление объекта в мир игры
	 * @param element
	 */
	public void addElement(IMapElement element) {
		elements.add(element);
	}
	
	/**
	 * Поиск единичного объекта указанного типа в мире игры
	 * @param <T> - Тип искомого объекта
	 * @param clazz - Класс искомого объекта
	 * @return
	 */
	public <T extends IMapElement> Optional<T> findOneElement(Class<T> clazz) {
		return (Optional<T>) elements.stream().filter(e->e.getClass().isAssignableFrom(clazz)).findAny();
	}
	
	/**
	 * Поиск головы змейки в игроком мире
	 * @return
	 */
	public SnakeHead findHead() {
		return findOneElement(SnakeHead.class).get();
	}
	
	/**
	 * Поиск двери в игровом мире
	 * @return
	 */
	public Door findDoor() {
		return findOneElement(Door.class).get();
	}
	
	/**
	 * Получение множества объектов в игровом мире по конкретным координатам
	 * @param location - координаты для поиска
	 * @return - итератор, перебирающий найденные объекты
	 */
	public Iterator<IMapElement> getElements(PairXY location) {
		return elements.stream().filter(e->e.getPosition().isSame(location)).iterator();
	}
	
	/**
	 * Получение единичного объекта в игровом мире по конкретным координатам
	 * @param location - координаты для поиска
	 * @return - Контейнер {@link Optional} с результатом поиска
	 */
	public Optional<IMapElement> getElement(PairXY location) {
		return elements.stream().filter(e->e.getPosition().isSame(location)).findAny();
	}
	
	/**
	 * Получение множества объектов в игровом мире по предыдущим координатам
	 * @param location - координаты для поиска
	 * @return - итератор, перебирающий найденные объекты
	 */
	public Iterator<IMapElement> getElementsByPrev(PairXY location) {
		return elements.stream().filter(e->e.getPrevPosition().isSame(location)).iterator();
	}
	
	/**
	 * Получение единичного объекта в игровом мире по предыдущим координатам
	 * @param location - координаты для поиска
	 * @return - Контейнер {@link Optional} с результатом поиска
	 */
	public Optional<IMapElement> getElementByPrev(PairXY location) {
		return elements.stream().filter(e->e.getPrevPosition().isSame(location)).findAny();
	}
	
	/**
	 * Удаление объекта из игрового мира
	 * @param element
	 */
	public void remove(IMapElement element) {
		elements.removeIf(e->e==element);
	}
	
	/**
	 * Множество объектов, расположенных в игровом мире
	 * @return
	 */
	public Set<IMapElement> getElements(){
		return elements;
	}
	
	/**
	 * Добавление слушателя игровых тиков
	 * @param handler
	 */
	public void addTickHandler(IWorldTickHandler handler) {
		tickHandlers.add(handler);
	}
	
	/**
	 * Удаление слушателей игровых тиков
	 * @param handler
	 */
	public void removeTickHandler(IWorldTickHandler handler) {
		tickHandlers.remove(handler);
	}
	
	/**
	 * Метод, реализущий тик игры
	 */
	public void doTick() {
		elements.removeIf(e->{
			e.onTick();
			PairXY pos = e.getPosition();
			return !e.exists() || pos.getX() < 0 || pos.getX() >= width || pos.getY() < 0 || pos.getY() >= height;
		});
		tickCounter++;
		tickHandlers.forEach(h->h.handle(this, tickCounter));
	}
	
	/**
	 * Расчет свободных позиций мира
	 * @return
	 */
	public List<PairXY> getFreePositions(){
		List<PairXY> res = new ArrayList<>();
		for(int y = 0; y < getHeight(); y++)
			for(int x = 0; x < getWidth(); x++) {
				PairXY pos = new PairXY(x, y);
				if(!getElement(pos).isPresent())
					res.add(pos);
			}
		return res;
	}

}
