package me.cepera.snake.graphics;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.StringBinding;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import me.cepera.snake.Game;
import me.cepera.snake.Snake;

/**
 * Меню паузы
 * @author Cepera
 *
 */
public class GuiMenu implements IGui{

	@Override
	public void show(Window window, Group parent) {
		VBox menuBox = new VBox(8);
		menuBox.setPrefWidth(100);
		Group menu = new Group(menuBox);
		menuBox.translateXProperty().bind(window.pushObservable(this, new DoubleBinding() {
			@Override
			protected double computeValue() {
				return window.getScene().getWidth()/2-50;
			}
		}));
		menuBox.translateYProperty().bind(window.pushObservable(this, new DoubleBinding() {
			@Override
			protected double computeValue() {
				return window.getScene().getHeight()/2-60;
			}
		}));
		menu.setTranslateZ(100);
		

		Text results = new Text();
		results.setTranslateY(-80);
		results.setTranslateX(-30);
		results.setFont(Font.font(25));
		results.setFill(Color.WHITE);
		menuBox.getChildren().add(results);
		
		results.textProperty().bind(window.pushObservable(this, new StringBinding() {
			@Override
			protected String computeValue() {
				if(game() != null) {
					if(game().isEnded()) {
						if(game().isWon()) return "Вы победили!";
						return "Вы проиграли!";
					}
					return "      Пауза";
				}
				return "     Змейка";
			}
		}));
		
		menu.visibleProperty().bind(Bindings.when(window.pushObservable(this, new BooleanBinding() {
			@Override
			protected boolean computeValue() {
				//System.out.println();
				return game() == null || game().isGamePaused() || game().isEnded();
			}
		})).then(true).otherwise(false));
		
		Button continueButton = new Button("Продолжить");
		continueButton.setOnAction(e->{
			window.closeGui();
		});
		
		continueButton.visibleProperty().bind(Bindings.when(window.pushObservable(this, new BooleanBinding() {
			@Override
			protected boolean computeValue() {
				return game() != null && !game().isEnded();
			}
		})).then(true).otherwise(false));
		Button newGame = new Button("Новая игра");
		newGame.setOnAction(e->{
			window.showGui(Guis.NEW_GAME);
		});
		Button exit = new Button("Выход");
		exit.setOnAction(e->{
			Platform.exit();
		});
		
		continueButton.setMinWidth(menuBox.getPrefWidth());
		newGame.setMinWidth(menuBox.getPrefWidth());
		exit.setMinWidth(menuBox.getPrefWidth());
		menuBox.getChildren().add(continueButton);
		menuBox.getChildren().add(newGame);
		menuBox.getChildren().add(exit);
		
		parent.getChildren().add(menuBox);
	}

	@Override
	public void close(Window window, Group parent) {}
	
	private Game game() {
		return Snake.instance().getGame();
	}

}
