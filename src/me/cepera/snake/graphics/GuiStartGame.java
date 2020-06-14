package me.cepera.snake.graphics;

import javafx.beans.binding.DoubleBinding;
import javafx.collections.FXCollections;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.StringConverter;
import me.cepera.snake.Difficulty;
import me.cepera.snake.Snake;

/**
 * Меню создания новой игры
 * @author Cepera
 *
 */
public class GuiStartGame implements IGui{

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
		
		ChoiceBox<Difficulty> dif = new ChoiceBox<>();
		dif.setItems(FXCollections.observableArrayList(Difficulty.values()));
		dif.getSelectionModel().select(0);
		dif.setConverter(new StringConverter<Difficulty>() {
			@Override
			public String toString(Difficulty object) {
				return object.getLocalizedName();
			}
			
			@Override
			public Difficulty fromString(String string) {
				for(Difficulty dif : Difficulty.values())
					if(dif.getLocalizedName().equalsIgnoreCase(string)) return dif;
				return null;
			}
		});
		dif.setMinWidth(menuBox.getPrefWidth());
		
		Button start = new Button("Начать");
		start.setOnAction(e->{
			Snake.instance().startNewGame(dif.getSelectionModel().getSelectedItem());
			window.closeGui();
		});
		start.setMinWidth(menuBox.getPrefWidth());
		
		Button back = new Button("Назад");
		back.setOnAction(e->{
			window.showGui(Guis.MENU);
		});
		back.setMinWidth(menuBox.getPrefWidth());
		back.setTranslateY(20);
		
		Text difLabel = new Text("Сложность:");
		difLabel.setFill(Color.WHITE);
		menuBox.getChildren().add(difLabel);
		menuBox.getChildren().add(dif);
		menuBox.getChildren().add(start);
		menuBox.getChildren().add(back);
		
		parent.getChildren().add(menu);
	}

	@Override
	public void close(Window window, Group parent) {}

}
