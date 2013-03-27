//START CODE
package net.minecraft.src;

public class AutoRefereeObjective {

	private int id;
	private int dataValue;
	private AutoRefereeObjectiveStatus status;

	public AutoRefereeObjective(int id) {
		this(id, 0, AutoRefereeObjectiveStatus.FLEECY_BOX);
	}

	public AutoRefereeObjective(int id, int dataValue) {
		this(id, dataValue, AutoRefereeObjectiveStatus.FLEECY_BOX);
	}

	public AutoRefereeObjective(int id, int dataValue, AutoRefereeObjectiveStatus status) {
		this.id = id;
		this.dataValue = dataValue;
		this.status = status;
	}

	public int getId() {
		return this.id;
	}

	public int getData() {
		return this.dataValue;
	}

	public void setStatus(AutoRefereeObjectiveStatus status) {
		this.status = status;
	}

	public AutoRefereeObjectiveStatus getStatus() {
		return this.status;
	}

}
// END CODE