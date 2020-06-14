package me.cepera.snake.graphics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javafx.application.Application;
import javafx.beans.binding.Binding;
import javafx.beans.binding.StringBinding;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import me.cepera.snake.Game;
import me.cepera.snake.Snake;
import me.cepera.snake.sounds.SoundSystem;

/**
 * Основной класс модуля, отвечающего за графическую часть программы.
 * Наследует класс {@link Application} и передается в JavaFX при запуске программы
 * @author Cepera
 *
 */
public class Window extends Application implements IBindingProvider{
	
	/**
	 * Константы, содержащие минимальный размер экрана
	 */
	public final static int MIN_WIDTH = 640, MIN_HEIGHT = 480;
	
	private ArrayList<ObservableValue> observables = new ArrayList<>();
	private HashMap<IBindingProvider, ArrayList<ObservableValue>> providedObservables
					= new HashMap<>();
	
	/**
	 * Объект, реализующий цикл отрисовки игрового мира
	 */
	private RenderLoop renderLoop;
	
	/**
	 * Группа объектов сцены, содержащая элементы графического интерфейса (различные меню)
	 */
	private Group guiGroup;
	
	private Group currentGuiGroup;
	
	/**
	 * Текущий отображаемый графический интерфейс (меню)
	 */
	private IGui currentGui;
	
	/**
	 * Сцена
	 */
	private Scene scene;
	
	/**
	 * Звуковой плеер, проигрывающий фоновую музыку и прочие звури.
	 */
	private MediaPlayer backgroundPlayer;
	
	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("Змейка");
		stage.getIcons().add(TextureLoader.getTexture("icon.png"));
		Canvas canvas = new Canvas(MIN_WIDTH, MIN_HEIGHT);

		Text overlay = new Text(MIN_WIDTH-150, 40, "");
		overlay.setTranslateZ(10);
		overlay.setFont(Font.font(18));
		overlay.setFill(Color.WHITE);
		
		guiGroup = new Group();
		
		Group mainGroup = new Group(canvas, overlay, guiGroup);
		scene = new Scene(mainGroup, MIN_WIDTH, MIN_HEIGHT, Color.WHITE);
		canvas.widthProperty().bind(scene.widthProperty());
		canvas.heightProperty().bind(scene.heightProperty());
		stage.setScene(scene);
		
		scene.widthProperty().addListener((obs, oldVal, newVal)->{
			overlay.setX(newVal.doubleValue()-150);
		});

		
		overlay.textProperty().bind(pushObservable(this, new StringBinding() {
			@Override
			protected String computeValue() {
				Game game = Snake.instance().getGame();
				if(game != null) {
					return "Яблоки: "+game.getScore()+"/"+game.getDifficulty().getTargetScore();
				}
				return "";
			}
		}));
		
		renderLoop = new RenderLoop(this, canvas);
		renderLoop.start();
		
		canvas.widthProperty().addListener((obs, oldVal, newVal)->{
			renderLoop.onResize();
		});
		canvas.heightProperty().addListener((obs, oldVal, newVal)->{
			renderLoop.onResize();
		});
		
		scene.setOnKeyPressed(e->{
			if(e.getCode() == KeyCode.F11) {
				stage.setFullScreen(!stage.isFullScreen());
			}else if(Snake.instance().getGame() != null) {
				if(e.getCode() == KeyCode.ESCAPE) {
					if(currentGui == null) showGui(Guis.MENU);
					else closeGui();
				}else if(currentGui == null) {
					switch(e.getCode()) {
					case LEFT:
					case A:
						Snake.instance().getGame().moveLeft();
						break;
					case UP:
					case W:
						Snake.instance().getGame().moveUp();
						break;
					case RIGHT:
					case D:
						Snake.instance().getGame().moveRight();
						break;
					case DOWN:
					case S:
						Snake.instance().getGame().moveDown();
						break;
					default:
					}
				}
				
			}
				
		});
		
		scene.addEventFilter(KeyEvent.KEY_PRESSED, e->{
			if(currentGui != null && e.getTarget() instanceof Node) {
				KeyCode newCode = null;
				switch(e.getCode()) {
				case A:
					newCode = KeyCode.LEFT;
					break;
				case D:
					newCode = KeyCode.RIGHT;
					break;
				case S:
					newCode = KeyCode.DOWN;
					break;
				case W:
					newCode = KeyCode.UP;
					break;
				case ENTER:
					newCode = KeyCode.SPACE;
					break;
				default:
				}
				if(newCode != null) {
					e.consume();
					((Node)e.getTarget()).fireEvent(new KeyEvent(
							e.getSource(),
							e.getTarget(),
							KeyEvent.KEY_PRESSED,
							"",
							"",
							newCode,
							false,
							false,
							false,
							false));
				}
			}
		});
		scene.addEventFilter(KeyEvent.KEY_RELEASED, e->{
			if(currentGui != null && e.getTarget() instanceof Node && e.getCode() == KeyCode.ENTER) {
				e.consume();
				((Node)e.getTarget()).fireEvent(new KeyEvent(
						e.getSource(),
						e.getTarget(),
						KeyEvent.KEY_RELEASED,
						"",
						"",
						KeyCode.SPACE,
						false,
						false,
						false,
						false));
			}
		});
		
		stage.sizeToScene();
		stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
		stage.show();
		stage.setMinWidth(stage.getWidth());
		stage.setMinHeight(stage.getHeight());
		
		backgroundPlayer = SoundSystem.loadSoundPlayer("background.mp3");
		backgroundPlayer.setAutoPlay(true);
		backgroundPlayer.setCycleCount(MediaPlayer.INDEFINITE);
		backgroundPlayer.setStartTime(Duration.ZERO);
		backgroundPlayer.setStopTime(backgroundPlayer.getMedia().getDuration());
		backgroundPlayer.play();
		
	}
	
	/**
	 * Отображаемая сцена JavaFX
	 * @return
	 */
	public Scene getScene() {
		return scene;
	}
	
	@Override
	public void stop() throws Exception {
		if(Snake.instance().getGame() != null)
			Snake.instance().getGame().stop();
		renderLoop.stop();
		backgroundPlayer.setAutoPlay(false);
		backgroundPlayer.stop();
	}
	
	/**
	 * Регистрация прослушиваемого объекта
	 * @param <T> - тип обхекта
	 * @param provider - объект, предоставляющий прослушиваемый объект
	 * @param observable - прослушиваемый объект
	 * @return
	 */
	public <T extends ObservableValue> T pushObservable(IBindingProvider provider, T observable){
		ArrayList<ObservableValue> obs = providedObservables.get(provider);
		if(obs == null) {
			obs = new ArrayList<>();
			providedObservables.put(provider, obs);
		}
		obs.add(observable);
		observables.add(observable);
		return observable;
	}
	
	/**
	 * Удаление прослушиваемых объектов
	 * @param provider - объект, предоставляющий прослушиваемые объекты, которые следует удалить.
	 */
	public void clearObservables(IBindingProvider provider) {
		ArrayList<ObservableValue> obs = providedObservables.get(provider);
		if(obs != null) {
			Iterator<ObservableValue> it = obs.iterator();
			while(it.hasNext()) {
				observables.remove(it.next());
				it.remove();
			}
			providedObservables.remove(provider);
		}
	}
	
	/**
	 * Обновить состояния всех прослушиваемых объектов
	 */
	public void refreshBindings() {
		observables.forEach(o->{
			if(o instanceof Binding) ((Binding)o).invalidate();
		});
	}
	
	/**
	 * Отобразить меню
	 * @param gui - меню, которое нужно отобразить
	 */
	public void showGui(IGui gui) {
		closeGui();
		(currentGui = gui).show(this, currentGuiGroup = new Group());
		guiGroup.getChildren().add(currentGuiGroup);
		if(Snake.instance().getGame() != null && !Snake.instance().getGame().isGamePaused())
			Snake.instance().getGame().pause();
	}
	
	/**
	 * Закрыть текущее меню
	 */
	public void closeGui() {
		if(currentGuiGroup != null) {
			currentGui.close(this, currentGuiGroup);
			guiGroup.getChildren().remove(currentGuiGroup);
			clearObservables(currentGui);
		}
		currentGui = null;
		currentGuiGroup = null;
		if(Snake.instance().getGame() != null && Snake.instance().getGame().isGamePaused())
			Snake.instance().getGame().pause();
	}
	
	/**
	 * Получить текущее меню
	 * @return
	 */
	public IGui getCurrentGui() {
		return currentGui;
	}

}
