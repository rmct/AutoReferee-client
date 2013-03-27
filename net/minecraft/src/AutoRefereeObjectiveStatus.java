//START CODE
package net.minecraft.src;

public enum AutoRefereeObjectiveStatus {
	FLEECY_BOX("Fleecy Box"), PLAYER("Player"), SAFE("Touched"), VICTORY_MONUMENT("Victory Monument");

	private String name;

	AutoRefereeObjectiveStatus(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}
}
// END CODE