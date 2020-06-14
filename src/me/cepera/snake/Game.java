package me.cepera.snake;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

import me.cepera.snake.elements.IMapElement;
import me.cepera.snake.elements.SnakeHead;

/**
 * Класс, реализующий процесс игры
 * @author Cepera
 *
 */
public class Game {

	/**
	 * Константы, описывающие направления
	 */
	private static final PairXY UP = new PairXY(0, -1),
								DOWN = new PairXY(0, 1),
								RIGHT = new PairXY(1, 0),
								LEFT = new PairXY(-1, 0);
	
	/**
	 * Направление движения
	 */
	private PairXY motion;
	
	/**
	 * Последний поворот
	 */
	private PairXY lastTurn;
	
	/**
	 * Мир игры
	 */
	private World world;
	
	/**
	 * Сложность игры
	 */
	private Difficulty difficulty;
	
	/**
	 * Текущий счет игры.
	 */
	private int score = 0;
	
	/**
	 * Переменные, описывающие статус игры
	 */
	private boolean isStarted = false, isStoped = false, isGamePaused = true, isWon = false;
	
	/**
	 * Время старта игры в милисекундах
	 */
	private long startTime;
	
	/**
	 * Время последнего тика игры в наносекундах
	 */
	private long lastTickNano;
	
	/**
	 * Период тиков игры в наносекундах
	 */
	private long tickPeriodNano;
	
	/**
	 * Очередь входящих команд изменения направления движения
	 */
	private ArrayBlockingQueue<PairXY> motionQueue = new ArrayBlockingQueue<>(2);
	
	/**
	 * Зарегистрированные слушатели
	 */
	private ArrayList<IGameListener> listeners = new ArrayList<>();
	
	public Game(World world, Difficulty difficulty) {
		this.world = world;
		motion = UP;
		lastTurn = UP;
		this.difficulty = difficulty;
		tickPeriodNano = difficulty.getTickRate()*1000000;
	}
	
	/**
	 * Старт игры.
	 * @throws IllegalStateException - в случаях, когда игра уже была запущена ранее.
	 */
	public void start() throws IllegalStateException{
		if(isStarted) throw new IllegalStateException("Game is already started");
		if(isStoped) throw new IllegalStateException("Game was finished. Can't start this one again.");
		isStarted = true;
		isGamePaused = false;
		startTime = System.currentTimeMillis();
		new Thread(()->{
			while(!isStoped) {
				motion = motionQueue.size() > 0 ? motionQueue.poll() : motion;
				if(!isGamePaused) world.doTick();
				SnakeHead head = world.findHead();
				world.getElements(head.getPosition()).forEachRemaining(el->{
					taste(head, el);
				});
				world.getElements(head.getPrevPosition()).forEachRemaining(el->{
					if(el.getPrevPosition().isSame(head.getPosition())) taste(head, el);
				});
				lastTickNano = System.nanoTime();
				try {
					Thread.sleep(difficulty.getTickRate());
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
		}, "game_thread").start();
	}
	
	private void taste(SnakeHead head, IMapElement el) {
		if(el instanceof SnakeHead) return;
		switch(el.getEatResult()) {
		case ADD_SCORE:
			score++;
			world.remove(el);
			head.increaseTail();
			listeners.forEach(l->l.onEat(this));
			break;
		case FINISH:
			isWon = true;
			listeners.forEach(l->l.onWin(this));
			stop();
			break;
		case DEAD:
			listeners.forEach(l->l.onLose(this));
			stop();
			break;
		}
	}
	
	/**
	 * Приостановка игры
	 */
	public void pause() {
		isGamePaused = !isGamePaused;
	}
	
	/**
	 * Приостановлена ли игра
	 * @return
	 */
	public boolean isGamePaused() {
		return isGamePaused;
	}
	
	/**
	 * Окончена ли игра
	 * @return
	 */
	public boolean isEnded() {
		return isStoped;
	}
	
	/**
	 * Проверка статуса победы
	 * @return
	 */
	public boolean isWon() {
		return isWon;
	}
	
	/**
	 * Время запуска игры в наносекундах
	 * @return
	 */
	public long getStartTime() {
		return startTime;
	}
	
	/**
	 * Остановить игру
	 */
	public void stop() {
		isStarted = false;
		isStoped = true;
	}
	
	/**
	 * Текущее направление движения
	 * @return
	 */
	public PairXY getMotion() {
		return motion;
	}
	
	/**
	 * Получение объекта, описывающего мир игры
	 * @return
	 */
	public World getWorld() {
		return world;
	}
	
	/**
	 * Текущий уровень сложности
	 * @return
	 */
	public Difficulty getDifficulty() {
		return difficulty;
	}
	
	/**
	 * Изменить текущее направление движения на направление 'вверх'
	 */
	public void moveUp() {
		if(world.findHead().getChild() == null || lastTurn != DOWN && lastTurn != UP)  {
			if(motionQueue.offer(UP)) lastTurn = UP;
		}
	}
	
	/**
	 * Изменить текущее направление движения на направление 'вниз'
	 */
	public void moveDown() {
		if(world.findHead().getChild() == null || lastTurn != UP && lastTurn != DOWN) {
			if(motionQueue.offer(DOWN)) lastTurn = DOWN;
		}
	}
	
	/**
	 * Изменить текущее направление движения на направление 'вправо'
	 */
	public void moveRight() {
		if(world.findHead().getChild() == null || lastTurn != LEFT && lastTurn != RIGHT) {
			if(motionQueue.offer(RIGHT)) lastTurn = RIGHT;
		}
	}
	
	/**
	 * Изменить текущее направление движения на направление 'влево'
	 */
	public void moveLeft() {
		if(world.findHead().getChild() == null || lastTurn != LEFT && lastTurn != RIGHT) {
			if(motionQueue.offer(LEFT)) lastTurn = LEFT;
		}
	}
	
	/**
	 * Текущий счет
	 * @return
	 */
	public int getScore() {
		return score;
	}
	
	/**
	 * Время последнего тика игры в наносекундах
	 * @return
	 */
	public long getLastTickNano() {
		return lastTickNano;
	}
	
	/**
	 * Период тиков игры в наносекундах
	 * @return
	 */
	public long getTickPeriodNano() {
		return tickPeriodNano;
	}
	
	/**
	 * Добавление слушателя игры.
	 * @param listener
	 */
	public void addGameListener(IGameListener listener) {
		listeners.add(listener);
	}

}
