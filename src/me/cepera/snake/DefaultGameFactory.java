package me.cepera.snake;

import java.util.List;
import java.util.Random;

import me.cepera.snake.elements.Apple;
import me.cepera.snake.elements.Ball;
import me.cepera.snake.elements.Door;
import me.cepera.snake.elements.SnakeHead;
import me.cepera.snake.elements.Wall;

/**
 * Стандартная фабрика игр, создающая классическую игру Змейка
 * @author Cepera
 *
 */
public class DefaultGameFactory implements IGameFactory{

	@Override
	public Game makeGame(Difficulty difficulty) {
		World world = new World(21, 21);
		for(int x = 0; x < world.getWidth(); x++) {
			for(int y = 0; y < world.getHeight(); y++) {
				if(x == 0 || x == world.getWidth()-1) {
					world.addElement(new Wall(new PairXY(x, y)));
				}else if((x != world.getWidth()/2 && y == 0) || y == world.getHeight()-1) {
					world.addElement(new Wall(new PairXY(x, y)));
				}
			}
		}
		world.addElement(new Door(new PairXY(world.getWidth()/2, 0)));
		Game game = new Game(world, difficulty);
		world.addElement(new SnakeHead(new PairXY(world.getWidth()/2, world.getHeight()-4), game));
		world.addTickHandler((w, tick)->{
			if(game.getScore() >= difficulty.getTargetScore()) {
				w.findDoor().setOpen(true);
			}else if(!w.findOneElement(Apple.class).isPresent()) {
				Random rand = new Random();
				List<PairXY> free = w.getFreePositions();
				PairXY pos;
				if(free.isEmpty()) 
					pos = new PairXY(rand.nextInt(world.getWidth()-2)+1, rand.nextInt(world.getHeight()-2)+1);
				else pos = free.get(rand.nextInt(free.size()));
				w.addElement(new Apple(pos, 30));
			}
		});
		world.addTickHandler((w, tick)->{
			if(tick % 39 == 0) {
				Random rand = new Random();
				switch(rand.nextInt(4)) {
				case 0:
					w.addElement(new Ball(new PairXY(rand.nextInt(world.getWidth()-2)+1, 0), new PairXY(0, 1)));
					break;
				case 2:
					w.addElement(new Ball(new PairXY(rand.nextInt(world.getWidth()-2)+1, world.getHeight()-1), new PairXY(0, -1)));
					break;
				case 1:
					w.addElement(new Ball(new PairXY(world.getWidth()-1, rand.nextInt(world.getHeight()-2)+1), new PairXY(-1, 0)));
					break;
				case 3:
					w.addElement(new Ball(new PairXY(0, rand.nextInt(world.getHeight()-2)+1), new PairXY(1, 0)));
					break;
				}
			}
		});
		return game;
	}

	
}
