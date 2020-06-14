package me.cepera.snake;

/**
 * Пара координат X,Y
 * @author Cepera
 *
 */
public class PairXY {

	public static final PairXY ZERO = new PairXY(0, 0);
	
	private int x, y;
	
	public PairXY(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public PairXY plus(PairXY pair) {
		return new PairXY(x+pair.x, y+pair.y);
	}
	
	public PairXY minus(PairXY pair) {
		return new PairXY(x-pair.x, y-pair.y);
	}
	
	public boolean isSame(PairXY pair) {
		return x == pair.x && y == pair.y;
	}

}
