package me.cepera.snake.graphics;

import java.util.Iterator;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BoxBlur;
import javafx.scene.paint.Color;
import me.cepera.snake.Game;
import me.cepera.snake.IGameListener;
import me.cepera.snake.PairXY;
import me.cepera.snake.Snake;
import me.cepera.snake.World;
import me.cepera.snake.elements.IMapElement;
import me.cepera.snake.sounds.SoundSystem;

/**
 * Класс, реализующий цикл графической обработки игрового мира а так же его связь с графической оболочкой программы.
 * @author Cepera
 *
 */
public class RenderLoop extends AnimationTimer{

	private Window window;
	private Canvas canvas;
	private long start = 0;
	
	private double cellWidth, cellHeight;
	private Game cachedGame;
	private long lastUnpaused;
	
	public RenderLoop(Window window, Canvas canvas) {
		this.window = window;
		this.canvas = canvas;
	}
	
	@Override
	public void start() {
		super.start();
		if(start == 0) start = System.nanoTime();
	}
	
	@Override
	public void handle(long now) {
		window.refreshBindings();
		Game game = getGame();
		if((game == null || game.isEnded() || game.isGamePaused()) && window.getCurrentGui() == null)
			window.showGui(Guis.MENU);
		GraphicsContext gr = canvas.getGraphicsContext2D();
		gr.setFill(Color.WHITE);
		gr.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		gr.drawImage(TextureLoader.getTexture("background.png"), 0, 0, canvas.getWidth(), canvas.getHeight());
		if(game != null) {
			World world = game.getWorld();
			if(game.isGamePaused() || game.isEnded()) {
				gr.setGlobalAlpha(0.5);
				gr.setEffect(new BoxBlur(5, 5, 5));
			}else lastUnpaused = now;
			for(int wy = 0; wy < world.getHeight(); wy++)
				for(int wx = 0; wx < world.getWidth(); wx++) {
					Iterator<IMapElement> elit = world.getElements(new PairXY(wx, wy));
					while(elit.hasNext()) {
						IMapElement el = elit.next();
						double p = 1.0*(lastUnpaused-game.getLastTickNano())/game.getTickPeriodNano();
						if(p < 0) p = 0;
						if(p > 1) p = 1;
						double px = el.getPrevPosition().getX();
						double py = el.getPrevPosition().getY();
						double x = (px + (wx-px)*p) * cellWidth;
						double y = (py + (wy-py)*p) * cellHeight;
						gr.drawImage(TextureLoader.getTexture(el), x, y, cellWidth, cellHeight);
					}
				}
			gr.setGlobalAlpha(1);
			gr.setEffect(null);
		}
	}
	
	@Override
	public void stop() {
		super.stop();
		start = 0;
	}
	
	/**
	 * Получение экземпляра игры. При первом обращении происходит связывание экземпляра игры с графической оболочкой.
	 * @return
	 */
	private Game getGame() {
		if(Snake.instance().getGame() != cachedGame) {
			cachedGame = Snake.instance().getGame();
			onResize();
			cachedGame.addGameListener(new IGameListener() {
				@Override
				public void onWin(Game game) {
					SoundSystem.loadSoundPlayer("victory.mp3").play();
				}
				
				@Override
				public void onLose(Game game) {
					SoundSystem.loadSoundPlayer("fail.mp3").play();
					
				}
				
				@Override
				public void onEat(Game game) {
					SoundSystem.loadSoundPlayer("eat.mp3").play();
					if(game.getScore() == game.getDifficulty().getTargetScore())
						SoundSystem.loadSoundPlayer("open_door.mp3").play();
				}
			});
		}
		return cachedGame;
	}
	
	/**
	 * Обработка события изменения размера окна
	 */
	public void onResize() {
		if(cachedGame != null) {
			cellWidth = canvas.getWidth() / cachedGame.getWorld().getWidth();
			cellHeight = canvas.getHeight() / cachedGame.getWorld().getHeight();
		}
	}

}
