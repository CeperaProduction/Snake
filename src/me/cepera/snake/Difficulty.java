package me.cepera.snake;

/**
 * Набор констант, описывающих уровни сложности игры.
 * @author Cepera
 *
 */
public enum Difficulty {
	EASY("Легко", 20, 400),
	NORMAL("Нормально", 40, 300),
	HARD("Тяжело", 75, 200),
	VERY_HARD("Очень тяжело", 150, 170);
	
	private int score, tickRate;
	private String localized;
	
	private Difficulty(String localized, int score, int tickRate) {
		this.score = score;
		this.tickRate = tickRate;
		this.localized = localized;
	}
	
	public int getTargetScore() {
		return score;
	}
	
	public int getTickRate() {
		return tickRate;
	}
	
	public String getLocalizedName() {
		return localized;
	}
	
}
