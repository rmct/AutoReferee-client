//START CODE
package net.minecraft.src;

public class AutoRefereeMessage {

	private int ticksRemaining;
	private int lastTick;
	private String message;
	private String author;

	public AutoRefereeMessage(String message, String author) {
		this.message = message;
		this.author = author;
		this.ticksRemaining = AutoReferee.MESSAGE_DISPLAY_TICKS;

	}

	public int tick(int tick) {
		if (lastTick == 0) {
			lastTick = tick;
			--ticksRemaining;
		} else if (lastTick < tick) {
			--ticksRemaining;
			lastTick = tick;
		}
		return ticksRemaining;
	}

	public String getTotalMessage() {
		return "<" + author + "> " + message;
	}

}
//END CODE